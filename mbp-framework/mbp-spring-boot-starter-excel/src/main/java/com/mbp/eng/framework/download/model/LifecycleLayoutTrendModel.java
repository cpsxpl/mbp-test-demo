package com.mbp.eng.framework.download.model;

import cn.afterturn.easypoi.excel.annotation.Excel;
import com.mbp.eng.framework.download.base.ExcelDataModel;

import java.util.Date;

public class LifecycleLayoutTrendModel extends ExcelDataModel {
    @Excel(name = "时间", height = 7, width = 25, isImportField = "true_st", databaseFormat = "yyyyMMddHHmmss", format = "yyyy-MM-dd")
    private Date processDate;

    @Excel(name = "认知", height = 7, width = 20, isImportField = "true_st")
    private String aware;

    @Excel(name = "吸引", height = 7, width = 20, isImportField = "true_st")
    private String appeal;

    @Excel(name = "引入", height = 7, width = 20, isImportField = "true_st")
    private String firstPurchase;

    @Excel(name = "成长", height = 7, width = 20, isImportField = "true_st")
    private String growth;

    @Excel(name = "成熟", height = 7, width = 20, isImportField = "true_st")
    private String maturity;

    @Excel(name = "衰退", height = 7, width = 20, isImportField = "true_st")
    private String decline;

    @Excel(name = "沉睡", height = 7, width = 20, isImportField = "true_st")
    private String loss;

    public LifecycleLayoutTrendModel() {}

    public LifecycleLayoutTrendModel(Date processDate, String aware, String appeal, String firstPurchase, String growth, String maturity, String decline, String loss) {
        this.processDate = processDate;
        this.aware = aware;
        this.appeal = appeal;
        this.firstPurchase = firstPurchase;
        this.growth = growth;
        this.maturity = maturity;
        this.decline = decline;
        this.loss = loss;
    }

    public Date getProcessDate() {
        return processDate;
    }

    public void setProcessDate(Date processDate) {
        this.processDate = processDate;
    }

    public String getAware() {
        return aware;
    }

    public void setAware(String aware) {
        this.aware = aware;
    }

    public String getAppeal() {
        return appeal;
    }

    public void setAppeal(String appeal) {
        this.appeal = appeal;
    }

    public String getFirstPurchase() {
        return firstPurchase;
    }

    public void setFirstPurchase(String firstPurchase) {
        this.firstPurchase = firstPurchase;
    }

    public String getGrowth() {
        return growth;
    }

    public void setGrowth(String growth) {
        this.growth = growth;
    }

    public String getMaturity() {
        return maturity;
    }

    public void setMaturity(String maturity) {
        this.maturity = maturity;
    }

    public String getDecline() {
        return decline;
    }

    public void setDecline(String decline) {
        this.decline = decline;
    }

    public String getLoss() {
        return loss;
    }

    public void setLoss(String loss) {
        this.loss = loss;
    }

    @Override
    public String toString() {
        return "LifeCycleTrendModel{" +
                "processDate=" + processDate +
                ", aware='" + aware + '\'' +
                ", appeal='" + appeal + '\'' +
                ", firstPurchase='" + firstPurchase + '\'' +
                ", growth='" + growth + '\'' +
                ", maturity='" + maturity + '\'' +
                ", decline='" + decline + '\'' +
                ", loss='" + loss + '\'' +
                '}';
    }
}
