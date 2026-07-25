package com.mbp.eng.framework.common.enums;

import com.google.common.base.Enums;

public enum DeviceTypeEnum {
    PHONE("0", "PHONE"),
    IMEI("8", "IMEI"),
    IMEI_MD5("64", "IMEI_MD5"),
    IDFA("32", "IDFA"),
    IDFA_MD5("128", "IDFA_MD5"),
    UNKNOWN("99", "UNKNOWN");

    private String serviceCode;
    private String serviceName;

    DeviceTypeEnum(String serviceCode, String serviceName) {
        this.serviceCode = serviceCode;
        this.serviceName = serviceName;
    }

    /**
     * 按照serviceCode获得枚举值
     */
    public static DeviceTypeEnum valueOf(Integer serviceCode) {
        if (String.valueOf(serviceCode) != null) {
            for (DeviceTypeEnum deviceTypeEnum : DeviceTypeEnum.values()) {
                if (deviceTypeEnum.getServiceCode() == String.valueOf(serviceCode)) {
                    return deviceTypeEnum;
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

    public String getServiceCode() {
        return serviceCode;
    }

    public void setServiceCode(String serviceCode) {
        this.serviceCode = serviceCode;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
