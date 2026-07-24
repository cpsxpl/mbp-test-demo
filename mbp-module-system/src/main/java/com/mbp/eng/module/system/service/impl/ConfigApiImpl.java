package com.mbp.eng.module.system.service.impl;

import com.mbp.eng.module.system.domain.InfraConfig;
import com.mbp.eng.module.system.service.ConfigApi;
import com.mbp.eng.module.system.service.ConfigService;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.annotation.Resource;

/**
 * 参数配置 API 实现类
 */
@Service("configApi")
@Validated
public class ConfigApiImpl implements ConfigApi {

    @Resource
    private ConfigService configService;

    @Override
    public String getConfigValueByKey(String key) {
        InfraConfig config = configService.getConfigByKey(key);
        return config != null ? config.getValue() : null;
    }

}
