package com.mbp.eng.framework.common.enums;

public enum BssJobStatus {
    DEFAULT("-1", "DEFAULT_CREATE"),
    SUCCESS("0", "SUCCESS"),
    FAILED("1", "FAILED"),
    ERROR("2", "ERROR"),
    READY("3", "READY"),
    SQL_PARSER("4", "SQL_PARSER"),
    CREATE_SPARK_SESSION("5", "CREATE_SPARK_SESSION"),
    EXECUTE_QUERY("6", "EXECUTE_QUERY"),
    CLEAN_OLD_AUDIENCE_JFS("7", "CLEAN_OLD_AUDIENCE_JFS"),
    WRITE_AUDIENCE_JFS("8", "WRITE_AUDIENCE_JFS"),
    GENERATE_AUDIENCE_PROFILE("16", "GENERATE_AUDIENCE_PROFILE"),
    ACTIVITY_READ_TABLE("1", "ACTIVITY_READ_TABLE"),
    UNKNOWN("99", "UNKNOWN");

    private String statusCode;
    private String statusName;

    /**
     * 按照serviceCode获得枚举值
     */
    public static BssJobStatus valueOf(Integer serviceCode) {
        if (String.valueOf(serviceCode) != null) {
            for (BssJobStatus audienceGenJobStatus : BssJobStatus.values()) {
                if (audienceGenJobStatus.getStatusCode() == String.valueOf(serviceCode)) {
                    return audienceGenJobStatus;
                }
            }
        }
        return null;
    }

    BssJobStatus(String statusCode, String statusName) {
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
