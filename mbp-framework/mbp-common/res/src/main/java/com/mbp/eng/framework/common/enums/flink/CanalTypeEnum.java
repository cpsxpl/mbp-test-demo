package com.mbp.eng.framework.common.enums.flink;

import com.google.common.base.Enums;

public enum CanalTypeEnum {
    CANALTYPE("0", "canalType", "canalType"),
    INSERT("1", "insert", "insert"),
    DELETE("2", "delete", "delete"),
    UPDATE("3", "update", "update"),
    BEFORE("4", "before", "before"),
    AFTER("5", "after", "after"),
    UPDATEBEFORE("6", "update_before", "update_before"),
    UPDATEAFTER("7", "update_after", "update_after");

    private String operationCode;
    private String operationType;
    private String operationName;

    CanalTypeEnum(String operationCode, String operationType, String operationName) {
        this.operationCode = operationCode;
        this.operationType = operationType;
        this.operationName = operationName;
    }

    /**
     * 按照serviceCode获得枚举值
     */
    public static CanalTypeEnum valueOf(Integer serviceCode) {
        if (String.valueOf(serviceCode) != null) {
            for (CanalTypeEnum canalTypeEnum : CanalTypeEnum.values()) {
                if (canalTypeEnum.getOperationCode() == String.valueOf(serviceCode)) {
                    return canalTypeEnum;
                }
            }
        }
        return null;
    }

    /**
     * 判断枚举类是否存在传入的枚举值
     */
    public static CanalTypeEnum getIfPresent(String name) {
        return Enums.getIfPresent(CanalTypeEnum.class, name).orNull();
    }

    public String getOperationCode() {
        return operationCode;
    }

    public void setOperationCode(String operationCode) {
        this.operationCode = operationCode;
    }

    public String getOperationType() {
        return operationType;
    }

    public void setOperationType(String operationType) {
        this.operationType = operationType;
    }

    public String getOperationName() {
        return operationName;
    }

    public void setOperationName(String operationName) {
        this.operationName = operationName;
    }
}
