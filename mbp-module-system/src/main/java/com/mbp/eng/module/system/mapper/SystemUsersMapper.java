package com.mbp.eng.module.system.mapper;

import com.mbp.eng.framework.common.pojo.PageResult;
import com.mbp.eng.framework.mybatis.core.mappe.BaseMapperX;
import com.mbp.eng.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.mbp.eng.module.system.controller.admin.user.vo.user.UserPageReqVO;
import com.mbp.eng.module.system.domain.SystemUsers;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface SystemUsersMapper extends BaseMapperX<SystemUsers> {

    default SystemUsers selectByUsername(String username) {
        return selectOne(SystemUsers::getUsername, username);
    }

    default SystemUsers selectByEmail(String email) {
        return selectOne(SystemUsers::getEmail, email);
    }

    default SystemUsers selectByMobile(String mobile) {
        return selectOne(SystemUsers::getMobile, mobile);
    }

    default PageResult<SystemUsers> selectPage(UserPageReqVO reqVO, Collection<Long> deptIds, Collection<Long> userIds) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SystemUsers>()
                .likeIfPresent(SystemUsers::getUsername, reqVO.getUsername())
                .likeIfPresent(SystemUsers::getMobile, reqVO.getMobile())
                .eqIfPresent(SystemUsers::getStatus, reqVO.getStatus())
                .betweenIfPresent(SystemUsers::getCreateTime, reqVO.getCreateTime())
                .inIfPresent(SystemUsers::getDeptId, deptIds)
                .inIfPresent(SystemUsers::getId, userIds)
                .orderByDesc(SystemUsers::getId));
    }

    default List<SystemUsers> selectListByNickname(String nickname) {
        return selectList(new LambdaQueryWrapperX<SystemUsers>().like(SystemUsers::getNickname, nickname));
    }

    default List<SystemUsers> selectListByStatus(Integer status) {
        return selectList(SystemUsers::getStatus, status);
    }

    default List<SystemUsers> selectListByDeptIds(Collection<Long> deptIds) {
        return selectList(SystemUsers::getDeptId, deptIds);
    }

}
