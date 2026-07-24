package com.mbp.eng.module.system.mapper;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.mbp.eng.framework.common.pojo.PageResult;
import com.mbp.eng.framework.mybatis.core.mappe.BaseMapperX;
import com.mbp.eng.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.mbp.eng.module.system.domain.SystemDictData;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.DictDataPageReqVO;
import org.apache.ibatis.annotations.Mapper;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Mapper
public interface DictDataMapper extends BaseMapperX<SystemDictData> {

    default SystemDictData selectByDictTypeAndValue(String dictType, String value) {
        return selectOne(SystemDictData::getDictType, dictType, SystemDictData::getValue, value);
    }

    default SystemDictData selectByDictTypeAndLabel(String dictType, String label) {
        return selectOne(SystemDictData::getDictType, dictType, SystemDictData::getLabel, label);
    }

    default List<SystemDictData> selectByDictTypeAndValues(String dictType, Collection<String> values) {
        return selectList(new LambdaQueryWrapper<SystemDictData>().eq(SystemDictData::getDictType, dictType)
                .in(SystemDictData::getValue, values));
    }

    default long selectCountByDictType(String dictType) {
        return selectCount(SystemDictData::getDictType, dictType);
    }

    default PageResult<SystemDictData> selectPage(DictDataPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<SystemDictData>()
                .likeIfPresent(SystemDictData::getLabel, reqVO.getLabel())
                .eqIfPresent(SystemDictData::getDictType, reqVO.getDictType())
                .eqIfPresent(SystemDictData::getStatus, reqVO.getStatus())
                .orderByDesc(Arrays.asList(SystemDictData::getDictType, SystemDictData::getSort)));
    }

    default List<SystemDictData> selectListByStatusAndDictType(Integer status, String dictType) {
        return selectList(new LambdaQueryWrapperX<SystemDictData>()
                .eqIfPresent(SystemDictData::getStatus, status)
                .eqIfPresent(SystemDictData::getDictType, dictType));
    }

}
