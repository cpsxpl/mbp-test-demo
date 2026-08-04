package com.mbp.eng.framework.web.core.filter;

import cn.hutool.core.util.StrUtil;
import com.mbp.eng.framework.common.util.date.DateUtil;
import com.mbp.eng.framework.common.util.servlet.ServletUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Request Body 缓存 Filter,实现它的可重复读取
 */
public class CacheRequestBodyFilter extends OncePerRequestFilter {

    private static Logger logger = LoggerFactory.getLogger(CacheRequestBodyFilter.class);

    /**
     * 需要排除的 URI
     *
     * 1. 排除 Spring Boot Admin 相关请求,避免客户端连接中断导致的异常.
     */
    private static final String[] IGNORE_URIS = {"/admin/", "/actuator/"};

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {
        long time = System.currentTimeMillis();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("类=====:{} 方法=====:{} time: is {}", this.getClass().getSimpleName(), methodName, DateUtil.getFormatTime(time));
        filterChain.doFilter(new CacheRequestBodyWrapper(request), response);
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        long time = System.currentTimeMillis();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("类=====:{} 方法=====:{} time: is {}", this.getClass().getSimpleName(), methodName, DateUtil.getFormatTime(time));

        // 1. 校验是否为排除的 URL
        String requestURI = request.getRequestURI();
        if (StrUtil.startWithAny(requestURI, IGNORE_URIS)) {
            return true;
        }

        // 2. 只处理 json 请求内容
        return !ServletUtils.isJsonRequest(request);
    }

}
