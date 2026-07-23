package com.mbp.eng.test.system.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mbp.eng.framework.common.enums.CommonStatusEnum;
import com.mbp.eng.framework.common.util.collection.ArrayUtils;
import com.mbp.eng.framework.test.core.ut.BaseDbUnitTest;
import com.mbp.eng.test.system.domain.SystemUsers;
import com.mbp.eng.test.system.enums.SexEnum;
import com.mbp.eng.test.system.mapper.SystemUsersMapper;
import com.mbp.eng.test.system.service.impl.SystemUsersServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.context.annotation.Import;


import javax.annotation.Resource;
import java.util.function.Consumer;

import static cn.hutool.core.util.RandomUtil.randomEle;
import static com.mbp.eng.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.mbp.eng.framework.test.core.util.RandomUtils.randomPojo;

@Import(SystemUsersServiceImpl.class)
public class SystemUsersServiceImplTest extends BaseDbUnitTest {

    @Resource
    private SystemUsersService systemUsersService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testGetUser() throws JsonProcessingException {
        // mock 数据
        /*SystemUsers systemUsers = randomAdminUserDO();
        userMapper.insert(systemUsers);*/
        // 准备参数
        Long userId = Long.valueOf(1);

        // 调用
        SystemUsers systemUsers1 = systemUsersService.getUser(userId);
        System.out.println("==========" + objectMapper.writeValueAsString(systemUsers1));
        // 断言
        //assertPojoEquals(systemUsers, systemUsers1);
    }

    @SafeVarargs
    private static SystemUsers randomAdminUserDO(Consumer<SystemUsers>... consumers) {
        Consumer<SystemUsers> consumer = (o) -> {
            o.setStatus(randomEle(CommonStatusEnum.values()).getStatus()); // 保证 status 的范围
            o.setSex(randomEle(SexEnum.values()).getSex()); // 保证 sex 的范围
        };
        return randomPojo(SystemUsers.class, ArrayUtils.append(consumer, consumers));
    }
}
