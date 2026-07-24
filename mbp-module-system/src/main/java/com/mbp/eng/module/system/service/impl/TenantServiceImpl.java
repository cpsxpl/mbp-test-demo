package com.mbp.eng.module.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ObjectUtil;
import com.baomidou.dynamic.datasource.annotation.DSTransactional;
import com.mbp.eng.framework.common.enums.CommonStatusEnum;
import com.mbp.eng.framework.common.pojo.PageResult;
import com.mbp.eng.framework.common.util.collection.CollectionUtils;
import com.mbp.eng.framework.common.util.date.DateUtils;
import com.mbp.eng.framework.common.util.object.BeanUtils;
import com.mbp.eng.framework.tenant.core.context.TenantContextHolder;
import com.mbp.eng.framework.tenant.core.util.TenantUtils;
import com.mbp.eng.module.system.service.MenuService;
import com.mbp.eng.module.system.service.RoleService;
import com.mbp.eng.module.system.service.TenantPackageService;
import com.mbp.eng.module.system.service.TenantService;
import com.mbp.eng.module.system.convert.tenant.TenantConvert;
import com.mbp.eng.module.system.dataobject.permission.MenuDO;
import com.mbp.eng.module.system.dataobject.permission.RoleDO;
import com.mbp.eng.module.system.domain.TenantDO;
import com.mbp.eng.module.system.domain.TenantPackageDO;
import com.mbp.eng.module.system.enums.RoleCodeEnum;
import com.mbp.eng.module.system.mapper.TenantMapper;
import com.mbp.eng.module.system.service.PermissionService;
import com.mbp.eng.module.system.service.SystemUsersService;
import com.mbp.eng.module.system.service.tenant.handler.TenantInfoHandler;
import com.mbp.eng.module.system.service.tenant.handler.TenantMenuHandler;
import com.mbp.eng.framework.tenant.core.config.TenantProperties;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.TenantPageReqVO;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.TenantSaveReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import static com.mbp.eng.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.TENANT_CAN_NOT_UPDATE_SYSTEM;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.TENANT_DISABLE;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.TENANT_EXPIRE;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.TENANT_NAME_DUPLICATE;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.TENANT_NOT_EXISTS;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.TENANT_WEBSITE_DUPLICATE;
import static java.util.Collections.singleton;

/**
 * 租户 Service 实现类
 */
@Service("tenantService")
@Validated
@Slf4j
public class TenantServiceImpl implements TenantService {

    @SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
    @Autowired(required = false) // 由于 yudao.tenant.enable 配置项，可以关闭多租户的功能，所以这里只能不强制注入
    private TenantProperties tenantProperties;

    @Resource
    private TenantMapper tenantMapper;

    @Resource
    private TenantPackageService tenantPackageService;

    @Resource
    @Lazy // 延迟，避免循环依赖报错
    private SystemUsersService systemUsersService;

    @Resource
    private RoleService roleService;

    @Resource
    private MenuService menuService;

    @Resource
    private PermissionService permissionService;

    @Override
    public List<Long> getTenantIdList() {
        List<TenantDO> tenants = tenantMapper.selectList();
        return CollectionUtils.convertList(tenants, TenantDO::getId);
    }

    @Override
    public void validTenant(Long id) {
        TenantDO tenant = getTenant(id);
        if (tenant == null) {
            throw exception(TENANT_NOT_EXISTS);
        }
        if (tenant.getStatus().equals(CommonStatusEnum.DISABLE.getStatus())) {
            throw exception(TENANT_DISABLE, tenant.getName());
        }
        if (DateUtils.isExpired(tenant.getExpireTime())) {
            throw exception(TENANT_EXPIRE, tenant.getName());
        }
    }

    private Long createUser(Long roleId, TenantSaveReqVO createReqVO) {
        // 创建用户
        Long userId = systemUsersService.createUser(TenantConvert.INSTANCE.convert02(createReqVO));
        // 分配角色
        permissionService.assignUserRole(userId, singleton(roleId));
        return userId;
    }

    @Override
    @DSTransactional // 多数据源，使用 @DSTransactional 保证本地事务，以及数据源的切换
    public void updateTenant(TenantSaveReqVO updateReqVO) {
        // 校验存在
        TenantDO tenant = validateUpdateTenant(updateReqVO.getId());
        // 校验租户名称是否重复
        validTenantNameDuplicate(updateReqVO.getName(), updateReqVO.getId());
        // 校验租户域名是否重复
        validTenantWebsiteDuplicate(updateReqVO.getWebsites(), updateReqVO.getId());
        // 校验套餐被禁用
        TenantPackageDO tenantPackage = tenantPackageService.validTenantPackage(updateReqVO.getPackageId());

        // 更新租户
        TenantDO updateObj = BeanUtils.toBean(updateReqVO, TenantDO.class);
        tenantMapper.updateById(updateObj);
        // 如果套餐发生变化，则修改其角色的权限
        if (ObjectUtil.notEqual(tenant.getPackageId(), updateReqVO.getPackageId())) {
            updateTenantRoleMenu(tenant.getId(), tenantPackage.getMenuIds());
        }
    }

    private void validTenantNameDuplicate(String name, Long id) {
        TenantDO tenant = tenantMapper.selectByName(name);
        if (tenant == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同名字的租户
        if (id == null) {
            throw exception(TENANT_NAME_DUPLICATE, name);
        }
        if (!tenant.getId().equals(id)) {
            throw exception(TENANT_NAME_DUPLICATE, name);
        }
    }

    private void validTenantWebsiteDuplicate(List<String> websites, Long excludeId) {
        if (CollUtil.isEmpty(websites)) {
            return;
        }
        websites.forEach(website -> {
            List<TenantDO> tenants = tenantMapper.selectListByWebsite(website);
            if (excludeId != null) {
                tenants.removeIf(tenant -> tenant.getId().equals(excludeId));
            }
            if (CollUtil.isNotEmpty(tenants)) {
                throw exception(TENANT_WEBSITE_DUPLICATE, website);
            }
        });
    }

    @Override
    @DSTransactional
    public void updateTenantRoleMenu(Long tenantId, Set<Long> menuIds) {
        TenantUtils.execute(tenantId, () -> {
            // 获得所有角色
            List<RoleDO> roles = roleService.getRoleList();
            roles.forEach(role -> Assert.isTrue(tenantId.equals(role.getTenantId()), "角色({}/{}) 租户不匹配",
                    role.getId(), role.getTenantId(), tenantId)); // 兜底校验
            // 重新分配每个角色的权限
            roles.forEach(role -> {
                // 如果是租户管理员，重新分配其权限为租户套餐的权限
                if (Objects.equals(role.getCode(), RoleCodeEnum.TENANT_ADMIN.getCode())) {
                    permissionService.assignRoleMenu(role.getId(), menuIds);
                    log.info("[updateTenantRoleMenu][租户管理员({}/{}) 的权限修改为({})]", role.getId(), role.getTenantId(), menuIds);
                    return;
                }
                // 如果是其他角色，则去掉超过套餐的权限
                Set<Long> roleMenuIds = permissionService.getRoleMenuListByRoleId(role.getId());
                roleMenuIds = CollUtil.intersectionDistinct(roleMenuIds, menuIds);
                permissionService.assignRoleMenu(role.getId(), roleMenuIds);
                log.info("[updateTenantRoleMenu][角色({}/{}) 的权限修改为({})]", role.getId(), role.getTenantId(), roleMenuIds);
            });
        });
    }

    @Override
    public void deleteTenant(Long id) {
        // 校验存在
        validateUpdateTenant(id);
        // 删除
        tenantMapper.deleteById(id);
    }

    @Override
    public void deleteTenantList(List<Long> ids) {
        // 1. 校验存在
        ids.forEach(this::validateUpdateTenant);

        // 2. 批量删除
        tenantMapper.deleteByIds(ids);
    }

    private TenantDO validateUpdateTenant(Long id) {
        TenantDO tenant = tenantMapper.selectById(id);
        if (tenant == null) {
            throw exception(TENANT_NOT_EXISTS);
        }
        // 内置租户，不允许删除
        if (isSystemTenant(tenant)) {
            throw exception(TENANT_CAN_NOT_UPDATE_SYSTEM);
        }
        return tenant;
    }

    @Override
    public TenantDO getTenant(Long id) {
        return tenantMapper.selectById(id);
    }

    @Override
    public PageResult<TenantDO> getTenantPage(TenantPageReqVO pageReqVO) {
        return tenantMapper.selectPage(pageReqVO);
    }

    @Override
    public TenantDO getTenantByName(String name) {
        return tenantMapper.selectByName(name);
    }

    @Override
    public TenantDO getTenantByWebsite(String website) {
        List<TenantDO> tenants = tenantMapper.selectListByWebsite(website);
        return CollUtil.getFirst(tenants);
    }

    @Override
    public Long getTenantCountByPackageId(Long packageId) {
        return tenantMapper.selectCountByPackageId(packageId);
    }

    @Override
    public List<TenantDO> getTenantListByPackageId(Long packageId) {
        return tenantMapper.selectListByPackageId(packageId);
    }

    @Override
    public List<TenantDO> getTenantListByStatus(Integer status) {
        return tenantMapper.selectListByStatus(status);
    }

    @Override
    public void handleTenantInfo(TenantInfoHandler handler) {
        // 如果禁用，则不执行逻辑
        if (isTenantDisable()) {
            return;
        }
        // 获得租户
        TenantDO tenant = getTenant(TenantContextHolder.getRequiredTenantId());
        // 执行处理器
        handler.handle(tenant);
    }

    @Override
    public void handleTenantMenu(TenantMenuHandler handler) {
        // 如果禁用，则不执行逻辑
        if (isTenantDisable()) {
            return;
        }
        // 获得租户，然后获得菜单
        TenantDO tenant = getTenant(TenantContextHolder.getRequiredTenantId());
        Set<Long> menuIds;
        if (isSystemTenant(tenant)) { // 系统租户，菜单是全量的
            menuIds = CollectionUtils.convertSet(menuService.getMenuList(), MenuDO::getId);
        } else {
            menuIds = tenantPackageService.getTenantPackage(tenant.getPackageId()).getMenuIds();
        }
        // 执行处理器
        handler.handle(menuIds);
    }

    private static boolean isSystemTenant(TenantDO tenant) {
        return Objects.equals(tenant.getPackageId(), TenantDO.PACKAGE_ID_SYSTEM);
    }

    private boolean isTenantDisable() {
        return tenantProperties == null || Boolean.FALSE.equals(tenantProperties.getEnable());
    }

}
