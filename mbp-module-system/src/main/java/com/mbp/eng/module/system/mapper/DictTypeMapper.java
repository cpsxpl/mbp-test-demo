package com.mbp.eng.module.system.mapper;

import com.mbp.eng.framework.common.pojo.PageResult;
import com.mbp.eng.framework.mybatis.core.mappe.BaseMapperX;
import com.mbp.eng.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.DictTypePageReqVO;
import com.mbp.eng.module.system.domain.SystemDictType;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

import java.time.LocalDateTime;

@Mapper
public interface DictTypeMapper extends BaseMapperX<SystemDictType> {

    default PageResult<SystemDictType> selectPage(DictTypePageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SystemDictType>()
                .likeIfPresent(SystemDictType::getName, reqVO.getName())
                .likeIfPresent(SystemDictType::getType, reqVO.getType())
                .eqIfPresent(SystemDictType::getStatus, reqVO.getStatus())
                .betweenIfPresent(SystemDictType::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(SystemDictType::getId));
    }

    default SystemDictType selectByType(String type) {
        return selectOne(SystemDictType::getType, type);
    }

    default SystemDictType selectByName(String name) {
        return selectOne(SystemDictType::getName, name);
    }

    @Update("UPDATE system_dict_type SET deleted = 1, deleted_time = #{deletedTime} WHERE id = #{id}")
    void updateToDelete(@Param("id") Long id, @Param("deletedTime") LocalDateTime deletedTime);

}
