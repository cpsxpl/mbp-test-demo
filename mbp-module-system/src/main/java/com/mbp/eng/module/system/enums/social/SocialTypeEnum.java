package com.mbp.eng.module.system.enums.social;

import cn.hutool.core.util.ArrayUtil;
import com.mbp.eng.framework.common.core.ArrayValuable;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;

/**
 * 社交平台的类型枚举
 */
@Getter
@AllArgsConstructor
public enum SocialTypeEnum implements ArrayValuable<Integer> {

    /**
     * Gitee
     */
    GITEE(10, "GITEE"),
    /**
     * 钉钉
     */
    DINGTALK(20, "DINGTALK"),

    /**
     * 企业微信
     */
    WECHAT_ENTERPRISE(30, "WECHAT_ENTERPRISE"),
    /**
     * 微信公众平台 - 移动端 H5
     */
    WECHAT_MP(31, "WECHAT_MP"),
    /**
     * 微信开放平台 - 网站应用 PC 端扫码授权登录
     */
    WECHAT_OPEN(32, "WECHAT_OPEN"),
    /**
     * 微信小程序
     */
    WECHAT_MINI_PROGRAM(34, "WECHAT_MINI_PROGRAM"),
    /**
     * 支付宝小程序
     */
    ALIPAY_MINI_PROGRAM(40, "ALIPAY"),
    ;

    public static final Integer[] ARRAYS = Arrays.stream(values()).map(SocialTypeEnum::getType).toArray(Integer[]::new);

    /**
     * 类型
     */
    private final Integer type;
    /**
     * 类型的标识
     */
    private final String source;

    @Override
    public Integer[] array() {
        return ARRAYS;
    }

    public static SocialTypeEnum valueOfType(Integer type) {
        return ArrayUtil.firstMatch(o -> o.getType().equals(type), values());
    }

}
