package com.mbp.eng.module.system.service;

import com.mbp.eng.module.system.controller.admin.user.vo.user.UserSaveReqVO;
import com.mbp.eng.module.system.domain.SystemUsers;
import com.mbp.eng.module.system.controller.admin.auth.vo.AuthRegisterReqVO;

import javax.validation.Valid;
import java.util.List;

/**
 * 后台用户 Service 接口
 */
public interface SystemUsersService {

    /**
     * 创建用户
     *
     * @param createReqVO 用户信息
     * @return 用户编号
     */
    Long createUser(@Valid UserSaveReqVO createReqVO);

    /**
     * 注册用户
     *
     * @param registerReqVO 用户信息
     * @return 用户编号
     */
    Long registerUser(@Valid AuthRegisterReqVO registerReqVO);

    /**
     * 删除用户
     *
     * @param id 用户编号
     */
    void deleteUser(Long id);

    /**
     * 批量删除用户
     *
     * @param ids 用户编号数组
     */
    void deleteUserList(List<Long> ids);

    /**
     * 通过用户名查询用户
     *
     * @param username 用户名
     * @return 用户对象信息
     */
    SystemUsers getUserByUsername(String username);

    /**
     * 通过用户 ID 查询用户
     *
     * @param id 用户ID
     * @return 用户对象信息
     */
    SystemUsers getUser(Long id);

    /**
     * 判断密码是否匹配
     *
     * @param rawPassword 未加密的密码
     * @param encodedPassword 加密后的密码
     * @return 是否匹配
     */
    boolean isPasswordMatch(String rawPassword, String encodedPassword);

}
