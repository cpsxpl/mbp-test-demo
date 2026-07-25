package com.mbp.eng.framework.common.enums;

public enum QuantumStatus {
    DEFAULT("-1", "DEFAULT_CREATE"),
    SUCCESS("0", "SUCCESS"),
    FAILED("1", "FAILED"),
    ERROR("2", "ERROR"),
    READY("3", "READY"),
    UNKNOWN("99", "UNKNOWN");

    private String statusCode;
    private String statusName;

    /**
     * 按照serviceCode获得枚举值
     */
    public static QuantumStatus valueOf(Integer serviceCode) {
        if (String.valueOf(serviceCode) != null) {
            for (QuantumStatus audienceGenJobStatus : QuantumStatus.values()) {
                if (audienceGenJobStatus.getStatusCode() == String.valueOf(serviceCode)) {
                    return audienceGenJobStatus;
                }
            }
        }
        return null;
    }

    QuantumStatus(String statusCode, String statusName) {
        this.statusCode = statusCode;
        this.statusName = statusName;
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
