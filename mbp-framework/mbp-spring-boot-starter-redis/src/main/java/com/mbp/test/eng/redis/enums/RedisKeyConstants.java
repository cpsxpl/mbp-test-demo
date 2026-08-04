package com.mbp.test.eng.redis.enums;


import com.mbp.test.eng.domain.system.FourAddressMapping;

/**
 * System Redis Key 枚举类
 */
public interface RedisKeyConstants {
    /**
     * 四级地址的缓存
     * <p>
     * KEY 格式:four_address_mapping_ken:{ken}
     * VALUE 数据类型:String 访问令牌信息 {@link FourAddressMapping}
     * <p>
     * 由于动态过期时间,使用 RedisTemplate 操作
     */
    String FOUR_ADDRESS_MAPPING_KEN = "four_address_mapping_ken:%s";
}
