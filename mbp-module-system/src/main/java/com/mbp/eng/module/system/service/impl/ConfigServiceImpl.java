package com.mbp.eng.module.system.service.impl;

import com.google.common.annotations.VisibleForTesting;
import javax.annotation.Resource;

import com.mbp.eng.framework.common.exception.util.ServiceExceptionUtil;
import com.mbp.eng.framework.common.pojo.PageResult;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.ConfigPageReqVO;
import com.mbp.eng.module.system.domain.InfraConfig;
import com.mbp.eng.module.system.enums.ConfigTypeEnum;
import com.mbp.eng.module.system.enums.other.ErrorCodeConstants;
import com.mbp.eng.module.system.mapper.ConfigMapper;
import com.mbp.eng.module.system.service.ConfigConvert;
import com.mbp.eng.module.system.service.ConfigService;
import com.mbp.eng.module.system.controller.admin.dept.vo.dept.ConfigSaveReqVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

import static com.mbp.eng.framework.common.exception.util.ServiceExceptionUtil.exception;


/**
 * 参数配置 Service 实现类
 */
@Service("configService")
@Slf4j
@Validated
public class ConfigServiceImpl implements ConfigService {

    @Resource
    private ConfigMapper configMapper;

    @Override
    public Long createConfig(ConfigSaveReqVO createReqVO) {
        // 校验参数配置 key 的唯一性
        validateConfigKeyUnique(null, createReqVO.getKey());

        // 插入参数配置
        InfraConfig config = ConfigConvert.INSTANCE.convert(createReqVO);
        config.setType(ConfigTypeEnum.CUSTOM.getType());
        configMapper.insert(config);
        return config.getId();
    }

    @Override
    public void updateConfig(ConfigSaveReqVO updateReqVO) {
        // 校验自己存在
        validateConfigExists(updateReqVO.getId());
        // 校验参数配置 key 的唯一性
        validateConfigKeyUnique(updateReqVO.getId(), updateReqVO.getKey());

        // 更新参数配置
        InfraConfig updateObj = ConfigConvert.INSTANCE.convert(updateReqVO);
        configMapper.updateById(updateObj);
    }

    @Override
    public void deleteConfig(Long id) {
        // 校验配置存在
        InfraConfig config = validateConfigExists(id);
        // 内置配置，不允许删除
        if (ConfigTypeEnum.SYSTEM.getType().equals(config.getType())) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CONFIG_CAN_NOT_DELETE_SYSTEM_TYPE);
        }
        // 删除
        configMapper.deleteById(id);
    }

    @Override
    public void deleteConfigList(List<Long> ids) {
        // 校验是否有内置配置
        List<InfraConfig> configs = configMapper.selectByIds(ids);
        configs.forEach(config -> {
            if (ConfigTypeEnum.SYSTEM.getType().equals(config.getType())) {
                throw ServiceExceptionUtil.exception(ErrorCodeConstants.CONFIG_CAN_NOT_DELETE_SYSTEM_TYPE);
            }
        });

        // 批量删除
        configMapper.deleteByIds(ids);
    }

    @Override
    public InfraConfig getConfig(Long id) {
        return configMapper.selectById(id);
    }

    @Override
    public InfraConfig getConfigByKey(String key) {
        return configMapper.selectByKey(key);
    }

    @Override
    public PageResult<InfraConfig> getConfigPage(ConfigPageReqVO pageReqVO) {
        return configMapper.selectPage(pageReqVO);
    }

    @VisibleForTesting
    public InfraConfig validateConfigExists(Long id) {
        if (id == null) {
            return null;
        }
        InfraConfig config = configMapper.selectById(id);
        if (config == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CONFIG_NOT_EXISTS);
        }
        return config;
    }

    @VisibleForTesting
    public void validateConfigKeyUnique(Long id, String key) {
        InfraConfig config = configMapper.selectByKey(key);
        if (config == null) {
            return;
        }
        // 如果 id 为空，说明不用比较是否为相同 id 的参数配置
        if (id == null) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CONFIG_KEY_DUPLICATE);
        }
        if (!config.getId().equals(id)) {
            throw ServiceExceptionUtil.exception(ErrorCodeConstants.CONFIG_KEY_DUPLICATE);
        }
    }

}
