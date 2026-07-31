package com.mbp.eng.framework.dict.config;

import com.mbp.eng.framework.common.biz.system.dict.DictDataCommonApi;
import com.mbp.eng.framework.common.util.date.DateUtil;
import com.mbp.eng.framework.dict.core.DictFrameworkUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;

@AutoConfiguration
public class MbpDictAutoConfiguration {
    private static Logger logger = LoggerFactory.getLogger(MbpDictAutoConfiguration.class);
    @Bean
    @SuppressWarnings("InstantiationOfUtilityClass")
    public DictFrameworkUtils dictUtils(DictDataCommonApi dictDataCommonApi) {
        long time = System.currentTimeMillis();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("类=====:{} 方法=====:{} time: is {}", this.getClass().getSimpleName(), methodName, DateUtil.getFormatTime(time));

        DictFrameworkUtils.init(dictDataCommonApi);
        return new DictFrameworkUtils();
    }

}
