package com.mbp.eng.framework.tracer.core.filter;

import com.mbp.eng.framework.common.util.date.DateUtil;
import com.mbp.eng.framework.common.util.monitor.TracerUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Trace 过滤器,打印 traceId 到 header 中返回
 */
public class TraceFilter extends OncePerRequestFilter {
    private static Logger logger = LoggerFactory.getLogger(TraceFilter.class);
    /**
     * Header 名 - 链路追踪编号
     */
    private static final String HEADER_NAME_TRACE_ID = "trace-id";

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        long time = System.currentTimeMillis();
        String methodName = Thread.currentThread().getStackTrace()[1].getMethodName();
        logger.info("类=====:{} 方法=====:{} time: is {}", this.getClass().getSimpleName(), methodName, DateUtil.getFormatTime(time));

        // 设置响应 traceId
        httpServletResponse.addHeader(HEADER_NAME_TRACE_ID, TracerUtils.getTraceId());
        // 继续过滤
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

}
