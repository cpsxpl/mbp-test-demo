package com.mbp.eng.framework.jdbc.sql.table;

import java.io.Serializable;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class Query implements Serializable {
    private String sortBy;

    protected String systemLanguage;

    protected String systemIndustry;

    private boolean isAscending;

    public Query() {
        sortBy = "";
        systemLanguage = "CN";
        systemIndustry = "BIZ";
        isAscending = false;
    }

    public String getSortBy() {
        return sortBy;
    }

    public boolean isSortAscending() {
        return isAscending;
    }

    public void setSortAscending(boolean newIsAscending) {
        isAscending = newIsAscending;
    }

    public void setSortBy(String newSortBy) {
        sortBy = newSortBy;
    }

    public String getSystemIndustry() {
        return systemIndustry;
    }

    public void setSystemIndustry(String systemIndustry) {
        this.systemIndustry = systemIndustry;
    }

    public String getSystemLanguage() {
        return systemLanguage;
    }

    public void setSystemLanguage(String systemLanguage) {
        this.systemLanguage = systemLanguage;
    }
}
