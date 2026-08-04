package com.mbp.eng.module.system.service.user;

import cn.hutool.core.util.RandomUtil;

import com.mbp.eng.framework.common.enums.CommonStatusEnum;
import com.mbp.eng.framework.common.exception.ServiceException;
import com.mbp.eng.framework.common.pojo.PageResult;
import com.mbp.eng.framework.common.util.collection.ArrayUtils;
import com.mbp.eng.framework.common.util.collection.CollectionUtils;
import com.mbp.eng.framework.test.core.ut.BaseDbUnitTest;
import com.mbp.eng.module.infra.api.config.ConfigApi;
import com.mbp.eng.module.infra.api.file.FileApi;
import com.mbp.eng.module.system.controller.admin.user.vo.profile.UserProfileUpdatePasswordReqVO;
import com.mbp.eng.module.system.controller.admin.user.vo.profile.UserProfileUpdateReqVO;
import com.mbp.eng.module.system.controller.admin.user.vo.user.UserImportExcelVO;
import com.mbp.eng.module.system.controller.admin.user.vo.user.UserImportRespVO;
import com.mbp.eng.module.system.controller.admin.user.vo.user.UserPageReqVO;
import com.mbp.eng.module.system.controller.admin.user.vo.user.UserSaveReqVO;
import com.mbp.eng.module.system.dal.dataobject.dept.DeptDO;
import com.mbp.eng.module.system.dal.dataobject.dept.PostDO;
import com.mbp.eng.module.system.dal.dataobject.dept.UserPostDO;
import com.mbp.eng.module.system.dal.dataobject.tenant.TenantDO;
import com.mbp.eng.module.system.dal.dataobject.user.AdminUserDO;
import com.mbp.eng.module.system.dal.mysql.dept.UserPostMapper;
import com.mbp.eng.module.system.dal.mysql.user.AdminUserMapper;
import com.mbp.eng.module.system.enums.common.SexEnum;
import com.mbp.eng.module.system.mq.producer.user.AdminUserProducer;
import com.mbp.eng.module.system.service.dept.DeptService;
import com.mbp.eng.module.system.service.dept.PostService;
import com.mbp.eng.module.system.service.oauth2.OAuth2TokenService;
import com.mbp.eng.module.system.service.permission.PermissionService;
import com.mbp.eng.module.system.service.tenant.TenantService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Resource;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

import static cn.hutool.core.util.RandomUtil.randomEle;
import static com.mbp.eng.framework.common.util.date.LocalDateTimeUtils.buildBetweenTime;
import static com.mbp.eng.framework.common.util.date.LocalDateTimeUtils.buildTime;
import static com.mbp.eng.framework.common.util.object.ObjectUtils.cloneIgnoreId;
import static com.mbp.eng.framework.test.core.util.AssertUtils.assertPojoEquals;
import static com.mbp.eng.framework.test.core.util.RandomUtils.randomPojo;
import static com.mbp.eng.module.system.service.user.AdminUserServiceImpl.USER_INIT_PASSWORD_KEY;
import static java.util.Collections.singleton;
import static java.util.Collections.singletonList;
import static org.assertj.core.util.Lists.newArrayList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@Import(AdminUserServiceImpl.class)
public class AdminUserServiceImplTest extends BaseDbUnitTest {

    @Resource
    private AdminUserServiceImpl userService;

    @Resource
    private AdminUserMapper userMapper;

    @MockBean
    private DeptService deptService;

    @MockBean
    private ConfigApi configApi;

    @BeforeEach
    public void before() {
        // mock 初始化密码
        when(configApi.getConfigValueByKey(USER_INIT_PASSWORD_KEY)).thenReturn("mbp");
    }


    @Test
    public void testGetUserPage() {
        // mock 数据
        AdminUserDO dbUser = initGetUserPageData();
        // 准备参数
        UserPageReqVO reqVO = new UserPageReqVO();
        reqVO.setUsername("tu");
        reqVO.setMobile("1560");
        reqVO.setStatus(CommonStatusEnum.ENABLE.getStatus());
        reqVO.setCreateTime(buildBetweenTime(2020, 12, 1, 2020, 12, 24));
        reqVO.setDeptId(1L); // 其中,1L 是 2L 的父部门
        // mock 方法
        List<DeptDO> deptList = newArrayList(randomPojo(DeptDO.class, o -> o.setId(2L)));
        when(deptService.getChildDeptList(eq(reqVO.getDeptId()))).thenReturn(deptList);

        // 调用
        PageResult<AdminUserDO> pageResult = userService.getUserPage(reqVO);
        // 断言
        assertEquals(1, pageResult.getTotal());
        assertEquals(1, pageResult.getList().size());
        assertPojoEquals(dbUser, pageResult.getList().get(0));
    }

    /**
     * 初始化 getUserPage 方法的测试数据
     */
    private AdminUserDO initGetUserPageData() {
        // mock 数据
        AdminUserDO dbUser = randomAdminUserDO(o -> { // 等会查询到
            o.setUsername("tudou");
            o.setMobile("15601691300");
            o.setStatus(CommonStatusEnum.ENABLE.getStatus());
            o.setCreateTime(buildTime(2020, 12, 12));
            o.setDeptId(2L);
        });
        userMapper.insert(dbUser);
        // 测试 username 不匹配
        userMapper.insert(cloneIgnoreId(dbUser, o -> o.setUsername("dou")));
        // 测试 mobile 不匹配
        userMapper.insert(cloneIgnoreId(dbUser, o -> o.setMobile("18818260888")));
        // 测试 status 不匹配
        userMapper.insert(cloneIgnoreId(dbUser, o -> o.setStatus(CommonStatusEnum.DISABLE.getStatus())));
        // 测试 createTime 不匹配
        userMapper.insert(cloneIgnoreId(dbUser, o -> o.setCreateTime(buildTime(2020, 11, 11))));
        // 测试 dept 不匹配
        userMapper.insert(cloneIgnoreId(dbUser, o -> o.setDeptId(0L)));
        return dbUser;
    }

    @Test
    public void testGetUser() {
        // mock 数据
        //AdminUserDO dbUser = randomAdminUserDO();
        AdminUserDO dbUser = new AdminUserDO();
        //userMapper.insert(dbUser);
        // 准备参数
        Long userId = Long.valueOf(1);

        // 调用
        AdminUserDO user = userService.getUser(userId);
        // 断言
        assertPojoEquals(dbUser, user);
    }

    // ========== 随机对象 ==========

    @SafeVarargs
    private static AdminUserDO randomAdminUserDO(Consumer<AdminUserDO>... consumers) {
        Consumer<AdminUserDO> consumer = (o) -> {
            o.setStatus(randomEle(CommonStatusEnum.values()).getStatus()); // 保证 status 的范围
            o.setSex(randomEle(SexEnum.values()).getSex()); // 保证 sex 的范围
        };
        return randomPojo(AdminUserDO.class, ArrayUtils.append(consumer, consumers));
    }

}
