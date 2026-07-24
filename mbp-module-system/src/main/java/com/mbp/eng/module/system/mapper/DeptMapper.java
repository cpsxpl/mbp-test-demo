package com.mbp.eng.module.system.mapper;

import com.mbp.eng.framework.mybatis.core.mappe.BaseMapperX;
import com.mbp.eng.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.mbp.eng.module.system.domain.SystemDept;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.DeptListReqVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface DeptMapper extends BaseMapperX<SystemDept> {

    default List<SystemDept> selectList(DeptListReqVO reqVO) {
        return selectList(new LambdaQueryWrapperX<SystemDept>()
                .likeIfPresent(SystemDept::getName, reqVO.getName())
                .eqIfPresent(SystemDept::getStatus, reqVO.getStatus()));
    }

    default SystemDept selectByParentIdAndName(Long parentId, String name) {
        return selectOne(SystemDept::getParentId, parentId, SystemDept::getName, name);
    }

    default Long selectCountByParentId(Long parentId) {
        return selectCount(SystemDept::getParentId, parentId);
    }

    default List<SystemDept> selectListByParentId(Collection<Long> parentIds) {
        return selectList(SystemDept::getParentId, parentIds);
    }

    default List<SystemDept> selectListByLeaderUserId(Long id) {
        return selectList(SystemDept::getLeaderUserId, id);
    }

}
