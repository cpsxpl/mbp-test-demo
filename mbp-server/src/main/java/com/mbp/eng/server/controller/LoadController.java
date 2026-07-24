/*
package com.mbp.eng.server.controller;

import com.baomidou.dynamic.datasource.creator.DataSourceCreator;
import com.baomidou.dynamic.datasource.DynamicRoutingDataSource;
import com.baomidou.dynamic.datasource.creator.DataSourceProperty;
import com.baomidou.dynamic.datasource.creator.basic.BasicDataSourceCreator;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.sql.DataSource;
import java.util.List;
import java.util.Map;
import java.util.Set;

*/
/**
 * 动态数据源添加删除
 *//*


@RestController
@RequestMapping("/datasources")
//@Api(tags = "添加删除数据源")
public class LoadController {

    @Autowired
    private DataSource dataSource;

    @Autowired
    private DataSourceCreator dataSourceCreator;

    @Autowired
    private BasicDataSourceCreator basicDataSourceCreator;


    */
/**
     * 获取当前数据源
     *
     * @return
     *//*

    @GetMapping
    //@ApiOperation("获取当前所有数据源")
    public Set<String> now() {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        return ds.getCurrentDataSources().keySet();
    }

    */
/**
     * 添加数据源
     * dataSourceProperty数据类型：{"pollName": "dataSource_1","username": "ssodata","password": "ssodata","driverClassName":"oracle.jdbc.OracleDriver","url": "jdbc:oracle:thin:@192.168.2.166:1521/orcl"}
     *
     * @return
     *//*

    @PostMapping("/add")
    //@ApiOperation("通用添加数据源（推荐）")
    public Set<String> add(@Validated @RequestBody DataSourceDTO dto) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        BeanUtils.copyProperties(dto, dataSourceProperty);
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DataSource dataSource = dataSourceCreator.createDataSource(dataSourceProperty);
        ds.addDataSource(dto.getPollName(), dataSource);
        return ds.getCurrentDataSources().keySet();
    }

    @PostMapping("/addBasic")
    //@ApiOperation(value = "添加基础数据源", notes = "调用Springboot内置方法创建数据源，兼容1,2")
    public Set<String> addBasic(@Validated @RequestBody DataSourceDTO dto) {
        DataSourceProperty dataSourceProperty = new DataSourceProperty();
        BeanUtils.copyProperties(dto, dataSourceProperty);
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        DataSource dataSource = basicDataSourceCreator.createDataSource(dataSourceProperty);
        ds.addDataSource(dto.getPollName(), dataSource);
        return ds.getCurrentDataSources().keySet();
    }

    @DeleteMapping
    //@ApiOperation("删除数据源")
    public String remove(String name) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        ds.removeDataSource(name);
        return "删除成功";
    }

    @GetMapping("/testNowDataSource")
    public List<Map<String, Object>> testNowDataSource(String dataSourcesName) {
        DynamicRoutingDataSource ds = (DynamicRoutingDataSource) dataSource;
        Map<String, DataSource> currentDataSources = ds.getCurrentDataSources();
        JdbcTemplate jdbcTemplate = new JdbcTemplate(currentDataSources.get(dataSourcesName));
        List<Map<String, Object>> mapList = jdbcTemplate.queryForList("select * from dba_tablespaces ");
        return mapList;
    }

}*/
