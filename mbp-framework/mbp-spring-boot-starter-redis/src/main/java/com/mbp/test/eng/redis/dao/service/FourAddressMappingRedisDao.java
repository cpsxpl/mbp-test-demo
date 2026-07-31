package com.mbp.test.eng.redis.dao.service;

import com.mbp.test.eng.redis.dao.base.BaseRedisDao;
import com.mbp.test.eng.redis.enums.FourAddressMapping;

/**
 * ExampleDao 接口
 *
 * @author J-ONE
 * @since 2014-01-15
 */
public interface FourAddressMappingRedisDao extends BaseRedisDao<FourAddressMapping, Integer> {
    FourAddressMapping getFourAddressMappingById(String id);
}