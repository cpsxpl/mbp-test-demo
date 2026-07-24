package com.mbp.eng.framework.tracer.config;


import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * BizTracer配置类
 */
@ConfigurationProperties("mbp.tracer")
@Data
public class TracerProperties {
}
