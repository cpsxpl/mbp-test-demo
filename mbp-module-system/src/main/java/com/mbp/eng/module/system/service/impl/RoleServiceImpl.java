package com.mbp.eng.module.system.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.google.common.annotations.VisibleForTesting;
import com.mbp.eng.framework.common.enums.CommonStatusEnum;
import com.mbp.eng.framework.common.exception.util.ServiceExceptionUtil;
import com.mbp.eng.framework.common.pojo.PageResult;
import com.mbp.eng.framework.common.util.collection.CollectionUtils;
import com.mbp.eng.framework.common.util.object.BeanUtils;
import com.mbp.eng.module.system.dataobject.permission.RoleDO;
import com.mbp.eng.module.system.enums.ErrorCodeConstants;
import com.mbp.eng.module.system.enums.LogRecordConstants;
import com.mbp.eng.module.system.enums.RedisKeyConstants;
import com.mbp.eng.module.system.enums.RoleTypeEnum;
import com.mbp.eng.module.system.service.PermissionService;
import com.mbp.eng.module.system.service.RoleService;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.RolePageReqVO;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.RoleSaveReqVO;
import com.mbp.eng.module.system.enums.RoleCodeEnum;
import com.mbp.eng.module.system.mapper.RoleMapper;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.service.impl.DiffParseFunction;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.*;

import static com.mbp.eng.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.mbp.eng.framework.common.util.collection.CollectionUtils.convertMap;

/**
 * 角色 Service 实现类
 */
@Service("roleService")
@Slf4j
public class RoleServiceImpl implements RoleService {

    @Resource
    private PermissionService permissionService;

    @Resource
    private RoleMapper roleMapper;


    @Override
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#updateReqVO.id")
    @LogRecord(type = LogRecordConstants.SYSTEM_ROLE_TYPE, subType = LogRecordConstants.SYSTEM_ROLE_UPDATE_SUB_TYPE, bizNo = "{{#updateReqVO.id}}",
            success = LogRecordConstants.SYSTEM_ROLE_UPDATE_SUCCESS)
    public void updateRole(RoleSaveReqVO updateReqVO) {
        // 1.1 校验是否可以更新
        RoleDO role = validateRoleForUpdate(updateReqVO.getId());
        // 1.2 校验角色的唯一字段是否重复
        validateRoleDuplicate(updateReqVO.getName(), updateReqVO.getCode(), updateReqVO.getId());

        // 2. 更新到数据库
        RoleDO updateObj = BeanUtils.toBean(updateReqVO, RoleDO.class);
        roleMapper.updateById(updateObj);

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable(DiffParseFunction.OLD_OBJECT, BeanUtils.toBean(role, RoleSaveReqVO.class));
        LogRecordContext.putVariable("role", role);
    }

    @Override
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    public void updateRoleDataScope(Long id, Integer dataScope, Set<Long> dataScopeDeptIds) {
        // 校验是否可以更新
        validateRoleForUpdate(id);

        // 更新数据范围
        RoleDO updateObject = new RoleDO();
        updateObject.setId(id);
        updateObject.setDataScope(dataScope);
        updateObject.setDataScopeDeptIds(dataScopeDeptIds);
        roleMapper.updateById(updateObject);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @CacheEvict(value = RedisKeyConstants.ROLE, key = "#id")
    @LogRecord(type = LogRecordConstants.SYSTEM_ROLE_TYPE, subType = LogRecordConstants.SYSTEM_ROLE_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = LogRecordConstants.SYSTEM_ROLE_DELETE_SUCCESS)
    public void deleteRole(Long id) {
        // 1. 校验是否可以更新
        RoleDO role = validateRoleForUpdate(id);

        // 2.1 标记删除
        roleMapper.deleteById(id);
        // 2.2 删除相关数据
        permissionService.processRoleDeleted(id);

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("role", role);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRoleList(List<Long> ids) {
        // 1. 校验是否可以删除
        ids.forEach(this::validateRoleForUpdate);

        // 2.1 标记删除
        roleMapper.deleteByIds(ids);
        // 2.2 删除相关数据
        ids.forEach(id -> permissionService.processRoleDeleted(id));
    }

    /**
     * 校验角色的唯一字段是否重复
     *
     * 1. 是否存在相同名字的角色
     * 2. 是否存在相同编码的角色
     *
     * @param name 角色名字
     * @param code 角色额编码
     * @param id 角色编号
     */
    @VisibleForTesting
    void validateRoleDuplicate(String name, String code, Long id) {
        // 0. 超级管理员，不允许创建
        if (RoleCodeEnum.isSuperAdmin(code)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_ADMIN_CODE_ERROR, code);
        }
        // 1. 该 name 名字被其它角色所使用
        RoleDO role = roleMapper.selectByName(name);
        if (role != null && !role.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_NAME_DUPLICATE, name);
        }
        // 2. 是否存在相同编码的角色
        if (!StringUtils.hasText(code)) {
            return;
        }
        // 该 code 编码被其它角色所使用
        role = roleMapper.selectByCode(code);
        if (role != null && !role.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_CODE_DUPLICATE, code);
        }
    }

    /**
     * 校验角色是否可以被更新
     *
     * @param id 角色编号
     */
    @VisibleForTesting
    RoleDO validateRoleForUpdate(Long id) {
        RoleDO role = roleMapper.selectById(id);
        if (role == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_NOT_EXISTS);
        }
        // 内置角色，不允许删除
        if (RoleTypeEnum.SYSTEM.getType().equals(role.getType())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_CAN_NOT_UPDATE_SYSTEM_TYPE_ROLE);
        }
        return role;
    }

    @Override
    public RoleDO getRole(Long id) {
        return roleMapper.selectById(id);
    }

    @Override
    @Cacheable(value = RedisKeyConstants.ROLE, key = "#id",
            unless = "#result == null")
    public RoleDO getRoleFromCache(Long id) {
        return roleMapper.selectById(id);
    }


    @Override
    public List<RoleDO> getRoleListByStatus(Collection<Integer> statuses) {
        return roleMapper.selectListByStatus(statuses);
    }

    @Override
    public List<RoleDO> getRoleList() {
        return roleMapper.selectList();
    }

    @Override
    public List<RoleDO> getRoleList(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        return roleMapper.selectByIds(ids);
    }

    @Override
    public List<RoleDO> getRoleListFromCache(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return Collections.emptyList();
        }
        // 这里采用 for 循环从缓存中获取，主要考虑 Spring CacheManager 无法批量操作的问题
        RoleServiceImpl self = getSelf();
        return CollectionUtils.convertList(ids, self::getRoleFromCache);
    }

    @Override
    public PageResult<RoleDO> getRolePage(RolePageReqVO reqVO) {
        return roleMapper.selectPage(reqVO);
    }

    @Override
    public boolean hasAnySuperAdmin(Collection<Long> ids) {
        if (CollectionUtil.isEmpty(ids)) {
            return false;
        }
        RoleServiceImpl self = getSelf();
        return ids.stream().anyMatch(id -> {
            RoleDO role = self.getRoleFromCache(id);
            return role != null && RoleCodeEnum.isSuperAdmin(role.getCode());
        });
    }

    @Override
    public void validateRoleList(Collection<Long> ids) {
        if (CollUtil.isEmpty(ids)) {
            return;
        }
        // 获得角色信息
        List<RoleDO> roles = roleMapper.selectByIds(ids);
        Map<Long, RoleDO> roleMap = convertMap(roles, RoleDO::getId);
        // 校验
        ids.forEach(id -> {
            RoleDO role = roleMap.get(id);
            if (role == null) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_NOT_EXISTS);
            }
            if (!CommonStatusEnum.ENABLE.getStatus().equals(role.getStatus())) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.ROLE_IS_DISABLE, role.getName());
            }
        });
    }

    /**
     * 获得自身的代理对象，解决 AOP 生效问题
     *
     * @return 自己
     */
    private RoleServiceImpl getSelf() {
        return SpringUtil.getBean(getClass());
    }

}
