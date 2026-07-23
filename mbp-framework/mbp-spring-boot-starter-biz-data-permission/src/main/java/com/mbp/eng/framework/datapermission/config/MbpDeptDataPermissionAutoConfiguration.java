package com.mbp.eng.framework.datapermission.config;

import com.mbp.eng.framework.common.biz.system.permission.PermissionCommonApi;
import com.mbp.eng.framework.datapermission.core.rule.dept.DeptDataPermissionRule;
import com.mbp.eng.framework.datapermission.core.rule.dept.DeptDataPermissionRuleCustomizer;
import com.mbp.eng.framework.security.core.LoginUser;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * 基于部门的数据权限 AutoConfiguration
 */
@AutoConfiguration
@ConditionalOnClass(LoginUser.class)
@ConditionalOnBean(value = {DeptDataPermissionRuleCustomizer.class})
public class MbpDeptDataPermissionAutoConfiguration {

    @Bean
    public DeptDataPermissionRule deptDataPermissionRule(PermissionCommonApi permissionCommonApi,
                                                         List<DeptDataPermissionRuleCustomizer> deptDataPermissionRuleCustomizerList) {
        // 创建 DeptDataPermissionRule 对象
        DeptDataPermissionRule rule = new DeptDataPermissionRule(permissionCommonApi);
        // 补全表配置
        deptDataPermissionRuleCustomizerList.forEach(customizer -> customizer.customize(rule));
        return rule;
    }

}
