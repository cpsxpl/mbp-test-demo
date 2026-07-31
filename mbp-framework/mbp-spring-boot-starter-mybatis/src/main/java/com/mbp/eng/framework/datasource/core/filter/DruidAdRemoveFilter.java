package com.mbp.eng.framework.datasource.core.filter;

import com.alibaba.druid.util.Utils;
import com.mbp.eng.framework.common.util.date.DateUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Druid 底部广告过滤器
 */
public class DruidAdRemoveFilter extends OncePerRequestFilter {
    private static Logger logger = LoggerFactory.getLogger(DruidAdRemoveFilter.class);
    /**
     * common.js 的路径
     */
    private static final String COMMON_JS_ILE_PATH = "support/http/resources/js/common.js";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws ServletException, IOException {
        long time = System.currentTimeMillis();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("类=====:{} 方法=====:{} time: is {}", this.getClass().getSimpleName(), methodName, DateUtil.getFormatTime(time));

        filterChain.doFilter(httpServletRequest, httpServletResponse);
        // 重置缓冲区,响应头不会被重置
        httpServletResponse.resetBuffer();
        // 获取 common.js
        String text = Utils.readFromResource(COMMON_JS_ILE_PATH);
        // 正则替换 banner, 除去底部的广告信息
        text = text.replaceAll("<a.*?banner\"></a><br/>", "");
        text = text.replaceAll("powered.*?shrek.wang</a>", "");
        httpServletResponse.getWriter().write(text);
    }

}
