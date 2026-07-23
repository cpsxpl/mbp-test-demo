package com.mbp.eng.server.controller;

import com.mbp.eng.framework.common.pojo.CommonResult;
import com.mbp.eng.framework.common.util.servlet.ServletUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.security.PermitAll;
import javax.servlet.http.HttpServletRequest;

import static com.mbp.eng.framework.common.exception.enums.GlobalErrorCodeConstants.NOT_IMPLEMENTED;

/**
 * 默认 Controller,解决部分 module 未开启时的 404 提示。
 * 例如说,/bpm/** 路径,工作流
 */
@RestController
@Slf4j
public class DefaultController {

    @RequestMapping("/admin-api/bpm/**")
    public CommonResult<Boolean> bpm404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "test1");
    }

    @RequestMapping("/admin-api/mp/**")
    public CommonResult<Boolean> mp404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "test2");
    }

    @RequestMapping(value = {"/admin-api/product/**", // 商品中心
            "/admin-api/trade/**", // 交易中心
            "/admin-api/promotion/**"}) // 营销中心
    public CommonResult<Boolean> mall404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "test3");
    }

    @RequestMapping("/admin-api/erp/**")
    public CommonResult<Boolean> erp404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "test4");
    }

    @RequestMapping(value = {"/admin-api/wms/**"})
    public CommonResult<Boolean> wms404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "test5");
    }

    @RequestMapping("/admin-api/crm/**")
    public CommonResult<Boolean> crm404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "test6");
    }

    @RequestMapping(value = {"/admin-api/mes/**"})
    public CommonResult<Boolean> mes404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "test7");
    }

    @RequestMapping(value = {"/admin-api/im/**"})
    public CommonResult<Boolean> im404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "test8");
    }

    @RequestMapping(value = {"/admin-api/report/**"})
    public CommonResult<Boolean> report404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "test9");
    }

    @RequestMapping(value = {"/admin-api/pay/**"})
    public CommonResult<Boolean> pay404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "test10");
    }

    @RequestMapping(value = {"/admin-api/ai/**"})
    public CommonResult<Boolean> ai404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "test11");
    }

    @RequestMapping(value = {"/admin-api/iot/**"})
    public CommonResult<Boolean> iot404() {
        return CommonResult.error(NOT_IMPLEMENTED.getCode(),
                "test12");
    }

    /**
     * 测试接口：打印 query、header、body
     */
    @RequestMapping(value = {"/test"})
    @PermitAll
    public CommonResult<Boolean> test(HttpServletRequest request) {
        // 打印查询参数
        log.info("Query: {}", ServletUtils.getParamMap(request));
        // 打印请求头
        log.info("Header: {}", ServletUtils.getHeaderMap(request));
        // 打印请求体
        log.info("Body: {}", ServletUtils.getBody(request));
        return CommonResult.success(true);
    }

}
