package com.mbp.eng.module.system.mapper;

import com.mbp.eng.framework.common.pojo.PageResult;
import com.mbp.eng.framework.mybatis.core.mappe.BaseMapperX;
import com.mbp.eng.framework.mybatis.core.query.LambdaQueryWrapperX;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.ConfigPageReqVO;
import com.mbp.eng.module.system.domain.InfraConfig;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface ConfigMapper extends BaseMapperX<InfraConfig> {

    default InfraConfig selectByKey(String key) {
        return selectOne(InfraConfig::getConfigKey, key);
    }

    default PageResult<InfraConfig> selectPage(ConfigPageReqVO reqVO) {
        return selectPage(reqVO, new LambdaQueryWrapperX<InfraConfig>()
                .likeIfPresent(InfraConfig::getName, reqVO.getName())
                .likeIfPresent(InfraConfig::getConfigKey, reqVO.getKey())
                .eqIfPresent(InfraConfig::getType, reqVO.getType())
                .betweenIfPresent(InfraConfig::getCreateTime, reqVO.getCreateTime())
                .orderByDesc(InfraConfig::getId));
    }

}
