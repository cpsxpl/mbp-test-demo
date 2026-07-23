package com.mbp.eng.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档地址
 */
@Getter
@AllArgsConstructor
public enum DocumentEnum {

    REDIS_INSTALL("https://", "Redis 安装文档"),
    TENANT("https://", "SaaS 多租户文档");

    private final String url;
    private final String memo;

}
