package com.mbp.eng.module.system.mapper;

import com.mbp.eng.framework.common.pojo.PageResult;
import com.mbp.eng.framework.mybatis.core.mappe.BaseMapperX;
import com.mbp.eng.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.mbp.eng.module.system.domain.SystemPost;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.PostPageReqVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Collection;
import java.util.List;

@Mapper
public interface PostMapper extends BaseMapperX<SystemPost> {

    default List<SystemPost> selectList(Collection<Long> ids, Collection<Integer> statuses) {
        return selectList(new LambdaQueryWrapperX<SystemPost>()
                .inIfPresent(SystemPost::getId, ids)
                .inIfPresent(SystemPost::getStatus, statuses));
    }

    default PageResult<SystemPost> selectPage(PostPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SystemPost>()
                .likeIfPresent(SystemPost::getCode, reqVO.getCode())
                .likeIfPresent(SystemPost::getName, reqVO.getName())
                .eqIfPresent(SystemPost::getStatus, reqVO.getStatus())
                .orderByDesc(SystemPost::getId));
    }

    default SystemPost selectByName(String name) {
        return selectOne(SystemPost::getName, name);
    }

    default SystemPost selectByCode(String code) {
        return selectOne(SystemPost::getCode, code);
    }

}
