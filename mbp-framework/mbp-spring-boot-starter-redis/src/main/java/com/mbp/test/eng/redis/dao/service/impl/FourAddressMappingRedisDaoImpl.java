package com.mbp.test.eng.redis.dao.service.impl;

import com.mbp.eng.framework.common.util.json.JsonUtils;
import com.mbp.test.eng.redis.dao.base.BaseRedisDaoImpl;
import com.mbp.test.eng.redis.dao.service.FourAddressMappingRedisDao;
import com.mbp.test.eng.domain.system.FourAddressMapping;
import org.springframework.stereotype.Repository;

import java.io.Serializable;

/**
 * fourAddressMappingRedisDao 实现类
 */
@Repository("fourAddressMappingRedisDao")
public class FourAddressMappingRedisDaoImpl extends BaseRedisDaoImpl<FourAddressMapping, Integer> implements Serializable, FourAddressMappingRedisDao {
    private static final String NAMESPACE = "com.mbp.test.eng.redis.dao.service.FourAddressMappingRedisDao.";

    //返回本DAO命名空间,并添加statement
    public String getNameSpace(String statement) {
        return NAMESPACE + statement;
    }

    @Override
    public FourAddressMapping getFourAddressMappingById(String id) {
        //redis获取的数据为字符串类型
        String value = this.selectRedis(getNameSpace("selectFourAddressMappingById"), id);
        return JsonUtils.parseObject(String.valueOf(value), FourAddressMapping.class);
    }
}