package com.mbp.eng.framework.download.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.mbp.eng.framework.download.base.ExcelDataModel;

public class ProfileModel extends ExcelDataModel {
    @Excel(name = "标签名", height = 7, width = 25, isImportField = "true_st", mergeVertical = true)
    private String type;

    public ProfileModel() {}

    public ProfileModel(String type, String valueMap, String percent, String tgiPercent) {
        this.type = type;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "ProfileModel{" +
                "type='" + type + '\'' +
                '}';
    }
}
