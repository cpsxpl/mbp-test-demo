package com.mbp.eng.module.system.service;

import com.mbp.eng.framework.common.pojo.PageResult;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.ConfigRespVO;
import com.mbp.eng.module.system.domain.InfraConfig;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.ConfigSaveReqVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper
public interface ConfigConvert {

    ConfigConvert INSTANCE = Mappers.getMapper(ConfigConvert.class);

    PageResult<ConfigRespVO> convertPage(PageResult<InfraConfig> page);

    List<ConfigRespVO> convertList(List<InfraConfig> list);

    @Mapping(source = "configKey", target = "key")
    ConfigRespVO convert(InfraConfig bean);

    @Mapping(source = "key", target = "configKey")
    InfraConfig convert(ConfigSaveReqVO bean);

}
