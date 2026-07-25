package com.mbp.eng.framework.signature.config;

import com.mbp.eng.framework.redis.config.MbpRedisAutoConfiguration;
import com.mbp.eng.framework.signature.core.aop.ApiSignatureAspect;
import com.mbp.eng.framework.signature.core.redis.ApiSignatureRedisDAO;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;

/**
 * HTTP API 签名的自动配置类
 */
@AutoConfiguration(after = MbpRedisAutoConfiguration.class)
public class MbpApiSignatureAutoConfiguration {

    @Bean
    public ApiSignatureAspect signatureAspect(ApiSignatureRedisDAO signatureRedisDAO) {
        return new ApiSignatureAspect(signatureRedisDAO);
    }

    @Bean
    public ApiSignatureRedisDAO signatureRedisDAO(StringRedisTemplate stringRedisTemplate) {
        return new ApiSignatureRedisDAO(stringRedisTemplate);
    }

}
