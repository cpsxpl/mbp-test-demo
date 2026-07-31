package com.mbp.test.eng.domain.system;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Date;

/*
 * 四级地址映射
 */
@JsonInclude(value = JsonInclude.Include.NON_EMPTY)
public class FourAddressMapping implements Serializable {
    private Long id;
    private Integer type;
    private String code;
    private String name;
    private String sname;
    private String ocode;
    private Integer level;
    private String parentCode;
    private Date createTime;
    private String lng;
    private String lat;

    public FourAddressMapping() {}

    public FourAddressMapping(FourAddressMapping fourAddressMapping) {
        this.id = fourAddressMapping.getId();
        this.type = fourAddressMapping.getType();
        this.name = fourAddressMapping.getName();
        //this.code = fourAddressMapping.getCode();
        this.sname = fourAddressMapping.getSname();
        this.ocode = fourAddressMapping.getOcode();
        this.level = fourAddressMapping.getLevel();
        this.parentCode = fourAddressMapping.getParentCode();
        this.createTime = fourAddressMapping.getCreateTime();
        this.lng = fourAddressMapping.getLng();
        this.lat = fourAddressMapping.getLat();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    /*public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }*/

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSname() {
        return sname;
    }

    public void setSname(String sname) {
        this.sname = sname;
    }

    public String getOcode() {
        return ocode;
    }

    public void setOcode(String ocode) {
        this.ocode = ocode;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    @Override
    public String toString() {
        return "FourAddressMapping [id=" + id + ", type=" + type + ", code=" + code + ", name=" + name + ", sname=" + sname + ", ocode=" + ocode + ", level="
                + level + ", parentCode=" + parentCode + ", createTime=" + createTime + ", lng=" + lng + ", lat=" + lat + "]";
    }
}
