package com.mbp.eng.framework.download.bean;

import cn.afterturn.easypoi.excel.entity.ExportParams;

import java.io.Serializable;
import java.util.List;

/**
 * 多sheet写出时，单sheet内容配置
 */
public class ExportView implements Serializable {
    private static final long serialVersionUID = -8156773468877218009L;

    /**
     * 该sheet的基础风格
     */
    private ExportParams exportParams;

    /**
     * 该sheet数据
     */
    private List<?> dataList;

    /**
     * excel对象CLASS
     */
    private Class<?> cls;

    public ExportView() {
    }

    public ExportView(ExportParams exportParams, List<?> dataList, Class<?> cls) {
        this.exportParams = exportParams;
        this.dataList = dataList;
        this.cls = cls;
    }

    public ExportParams getExportParams() {
        return exportParams;
    }

    public void setExportParams(ExportParams exportParams) {
        this.exportParams = exportParams;
    }

    public List<?> getDataList() {
        return dataList;
    }

    public void setDataList(List<?> dataList) {
        this.dataList = dataList;
    }

    public Class<?> getCls() {
        return cls;
    }

    public void setCls(Class<?> cls) {
        this.cls = cls;
    }
}
