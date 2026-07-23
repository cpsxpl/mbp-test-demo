package com.mbp.eng.framework.common;

import java.util.List;

public class Address {
    private Integer adcode;
    private String name;
    private List<Double> center;
    private String level;
    private List<Address> districts;

    public Integer getAdcode() {
        return adcode;
    }

    public void setAdcode(Integer adcode) {
        this.adcode = adcode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Double> getCenter() {
        return center;
    }

    public void setCenter(List<Double> center) {
        this.center = center;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public List<Address> getDistricts() {
        return districts;
    }

    public void setDistricts(List<Address> districts) {
        this.districts = districts;
    }
}
