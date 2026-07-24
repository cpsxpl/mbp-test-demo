package com.mbp.eng.module.system.service.impl;

import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.annotations.VisibleForTesting;
import com.mbp.eng.framework.common.enums.CommonStatusEnum;
import com.mbp.eng.framework.common.util.collection.CollectionUtils;
import com.mbp.eng.framework.common.util.object.BeanUtils;
import com.mbp.eng.framework.datapermission.core.util.DataPermissionUtils;
import com.mbp.eng.module.system.service.TenantService;
import com.mbp.eng.module.system.controller.admin.user.vo.user.UserSaveReqVO;
import com.mbp.eng.module.system.domain.SystemUsers;
import com.mbp.eng.module.system.mapper.SystemUsersMapper;
import com.mbp.eng.module.system.mapper.UserPostMapper;
import com.mbp.eng.module.system.service.ConfigApi;
import com.mbp.eng.module.system.service.DeptService;
import com.mbp.eng.module.system.service.PermissionService;
import com.mbp.eng.module.system.service.PostService;
import com.mbp.eng.module.system.service.SystemUsersService;
import com.mbp.eng.module.system.controller.admin.auth.vo.AuthRegisterReqVO;
import com.mzt.logapi.context.LogRecordContext;
import com.mzt.logapi.starter.annotation.LogRecord;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import javax.annotation.Resource;

import java.util.List;
import java.util.Set;

import static com.mbp.eng.framework.common.exception.util.ServiceExceptionUtil.exception;
import static com.mbp.eng.framework.common.util.collection.CollectionUtils.convertList;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.USER_COUNT_MAX;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.USER_EMAIL_EXISTS;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.USER_MOBILE_EXISTS;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.USER_NOT_EXISTS;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.USER_PASSWORD_FAILED;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.USER_REGISTER_DISABLED;
import static com.mbp.eng.module.system.enums.ErrorCodeConstants.USER_USERNAME_EXISTS;
import static com.mbp.eng.module.system.enums.LogRecordConstants.SYSTEM_USER_CREATE_SUB_TYPE;
import static com.mbp.eng.module.system.enums.LogRecordConstants.SYSTEM_USER_CREATE_SUCCESS;
import static com.mbp.eng.module.system.enums.LogRecordConstants.SYSTEM_USER_DELETE_SUB_TYPE;
import static com.mbp.eng.module.system.enums.LogRecordConstants.SYSTEM_USER_DELETE_SUCCESS;
import static com.mbp.eng.module.system.enums.LogRecordConstants.SYSTEM_USER_TYPE;


/**
 * 后台用户 Service 实现类
 */
@Service("systemUsersService")
@Slf4j
public class SystemUsersServiceImpl implements SystemUsersService {

    static final String USER_INIT_PASSWORD_KEY = "system.user.init-password";

    static final String USER_REGISTER_ENABLED_KEY = "system.user.register-enabled";

    @Resource
    private SystemUsersMapper userMapper;

    @Resource
    private DeptService deptService;

    @Resource
    private PostService postService;

    @Resource
    private UserPostMapper userPostMapper;

    @Resource
    private ConfigApi configApi;

    @Resource
    private PermissionService permissionService;

    @Resource
    private PasswordEncoder passwordEncoder;

    @Resource
    @Lazy // 延迟，避免循环依赖报错
    private TenantService tenantService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = SYSTEM_USER_TYPE, subType = SYSTEM_USER_CREATE_SUB_TYPE, bizNo = "{{#user.id}}",
            success = SYSTEM_USER_CREATE_SUCCESS)
    public Long createUser(UserSaveReqVO createReqVO) {
        // 1.1 校验账户配合
        tenantService.handleTenantInfo(tenant -> {
            long count = userMapper.selectCount();
            if (count >= tenant.getAccountCount()) {
                throw exception(USER_COUNT_MAX, tenant.getAccountCount());
            }
        });
        // 1.2 校验正确性
        validateUserForCreateOrUpdate(null, createReqVO.getUsername(),
                createReqVO.getMobile(), createReqVO.getEmail(), createReqVO.getDeptId(), createReqVO.getPostIds());
        // 2.1 插入用户
        SystemUsers user = BeanUtils.toBean(createReqVO, SystemUsers.class);
        user.setStatus(CommonStatusEnum.ENABLE.getStatus()); // 默认开启
        user.setPassword(encodePassword(createReqVO.getPassword())); // 加密密码
        userMapper.insert(user);
        // 2.2 插入关联岗位
        /*if (CollectionUtil.isNotEmpty(user.getPostIds())) {
            userPostMapper.insertBatch(convertList(user.getPostIds(),
                    postId -> new UserPostDO().setUserId(user.getId()).setPostId(postId)));
        }*/

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("user", user);
        return user.getId();
    }

    @Override
    public Long registerUser(AuthRegisterReqVO registerReqVO) {
        // 1.1 校验是否开启注册
        if (ObjUtil.notEqual(configApi.getConfigValueByKey(USER_REGISTER_ENABLED_KEY), "true")) {
            throw exception(USER_REGISTER_DISABLED);
        }
        // 1.2 校验账户配合
        tenantService.handleTenantInfo(tenant -> {
            long count = userMapper.selectCount();
            if (count >= tenant.getAccountCount()) {
                throw exception(USER_COUNT_MAX, tenant.getAccountCount());
            }
        });
        // 1.3 校验正确性
        validateUserForCreateOrUpdate(null, registerReqVO.getUsername(), null, null, null, null);

        // 2. 插入用户
        SystemUsers user = BeanUtils.toBean(registerReqVO, SystemUsers.class);
        user.setStatus(CommonStatusEnum.ENABLE.getStatus()); // 默认开启
        user.setPassword(encodePassword(registerReqVO.getPassword())); // 加密密码
        userMapper.insert(user);
        return user.getId();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    @LogRecord(type = SYSTEM_USER_TYPE, subType = SYSTEM_USER_DELETE_SUB_TYPE, bizNo = "{{#id}}",
            success = SYSTEM_USER_DELETE_SUCCESS)
    public void deleteUser(Long id) {
        // 1. 校验用户存在
        SystemUsers user = validateUserExists(id);

        // 2.1 删除用户
        userMapper.deleteById(id);
        // 2.2 删除用户关联数据
        permissionService.processUserDeleted(id);
        // 2.2 删除用户岗位
        userPostMapper.deleteByUserId(id);

        // 3. 记录操作日志上下文
        LogRecordContext.putVariable("user", user);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUserList(List<Long> ids) {
        // 1. 批量删除用户
        userMapper.deleteByIds(ids);

        // 2. 批量删除用户关联数据
        ids.forEach(id -> {
            permissionService.processUserDeleted(id);
            userPostMapper.deleteByUserId(id);
        });
    }


    @Override
    public SystemUsers getUserByUsername(String username) {
        return userMapper.selectByUsername(username);
    }
    
    private SystemUsers validateUserForCreateOrUpdate(Long id, String username, String mobile, String email,
                                                      Long deptId, Set<Long> postIds) {
        // 关闭数据权限，避免因为没有数据权限，查询不到数据，进而导致唯一校验不正确
        return DataPermissionUtils.executeIgnore(() -> {
            // 校验用户存在
            SystemUsers user = validateUserExists(id);
            // 校验用户名唯一
            validateUsernameUnique(id, username);
            // 校验手机号唯一
            validateMobileUnique(id, mobile);
            // 校验邮箱唯一
            validateEmailUnique(id, email);
            // 校验部门处于开启状态
            deptService.validateDeptList(CollectionUtils.singleton(deptId));
            // 校验岗位处于开启状态
            postService.validatePostList(postIds);
            return user;
        });
    }
    
    @Override
    public SystemUsers getUser(Long id) {
        return userMapper.selectById(id);
    }


    @VisibleForTesting
    SystemUsers validateUserExists(Long id) {
        if (id == null) {
            return null;
        }
        SystemUsers user = userMapper.selectById(id);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
        return user;
    }


    @VisibleForTesting
    void validateUsernameUnique(Long id, String username) {
        if (StrUtil.isBlank(username)) {
            return;
        }
        SystemUsers user = userMapper.selectByUsername(username);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw exception(USER_USERNAME_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw exception(USER_USERNAME_EXISTS);
        }
    }

    @VisibleForTesting
    void validateEmailUnique(Long id, String email) {
        if (StrUtil.isBlank(email)) {
            return;
        }
        SystemUsers user = userMapper.selectByEmail(email);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw exception(USER_EMAIL_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw exception(USER_EMAIL_EXISTS);
        }
    }

    @VisibleForTesting
    void validateMobileUnique(Long id, String mobile) {
        if (StrUtil.isBlank(mobile)) {
            return;
        }
        SystemUsers user = userMapper.selectByMobile(mobile);
        if (user == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的用户
        if (id == null) {
            throw exception(USER_MOBILE_EXISTS);
        }
        if (!user.getId().equals(id)) {
            throw exception(USER_MOBILE_EXISTS);
        }
    }

    /**
     * 校验旧密码
     * @param id          用户 id
     * @param oldPassword 旧密码
     */
    @VisibleForTesting
    void validateOldPassword(Long id, String oldPassword) {
        SystemUsers user = userMapper.selectById(id);
        if (user == null) {
            throw exception(USER_NOT_EXISTS);
        }
        if (!isPasswordMatch(oldPassword, user.getPassword())) {
            throw exception(USER_PASSWORD_FAILED);
        }
    }

    @Override
    public boolean isPasswordMatch(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    /**
     * 对密码进行加密
     *
     * @param password 密码
     * @return 加密后的密码
     */
    private String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }
}
