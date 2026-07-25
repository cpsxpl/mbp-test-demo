package com.mbp.eng.framework.jdbc.sql.table;

import java.io.Serializable;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class CodeBean implements Serializable {
    private String code;
    private String value;
    private String type;

    public CodeBean() {
        code = "";
        value = "";
        type = "";
    }

    public String getCode() {
        return code;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setCode(String s) {
        code = s;
    }

    public void setType(String s) {
        type = s;
    }

    public void setValue(String s) {
        value = s;
    }
}
