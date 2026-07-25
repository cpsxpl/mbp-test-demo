package com.mbp.eng.framework.common.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 文档地址
 */
@Getter
@AllArgsConstructor
public enum DocumentEnum {

    REDIS_INSTALL("https://google.com", "Redis 安装文档"),
    TENANT("https://doc.google.cn", "SaaS 多租户文档");

    private final String url;
    private final String memo;

}
