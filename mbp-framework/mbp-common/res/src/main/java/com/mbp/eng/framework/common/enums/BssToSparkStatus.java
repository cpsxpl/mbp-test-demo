package com.mbp.eng.framework.common.enums;

import com.google.common.base.Enums;

public enum BssToSparkStatus {
    appName("appName", "Mbp-Bss-Gen"),
    UNKNOWN("99", "UNKNOWN");
    private String statusCode;
    private String statusName;

    BssToSparkStatus(String statusCode, String statusName) {
        this.statusCode = statusCode;
        this.statusName = statusName;
    }

    /**
     * 按照serviceCode获得枚举值
     */
    public static BssToSparkStatus valueOf(Integer serviceCode) {
        if (String.valueOf(serviceCode) != null) {
            for (BssToSparkStatus bssToSparkStatus : BssToSparkStatus.values()) {
                if (bssToSparkStatus.getStatusCode() == String.valueOf(serviceCode)) {
                    return bssToSparkStatus;
                }
            }
        }
        return null;
    }

    /**
     * 判断枚举类是否存在传入的枚举值
     */
    public static ClientTypeEnum getIfPresent(String name) {
        return Enums.getIfPresent(ClientTypeEnum.class, name).orNull();
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatusName() {
        return statusName;
    }

    public void setStatusName(String statusName) {
        this.statusName = statusName;
    }
}
