package com.mbp.eng.framework.jdbc.sql.table;

import java.io.Serializable;
import java.util.Vector;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class SelectList implements Serializable {
    private Vector vector;

    private int index;

    private static SelectList instance;

    public SelectList() {
        this.vector = new Vector();
        this.index = 0;
    }

    public void addItem(CodeBean codeBean) {
        this.vector.addElement(codeBean);
        this.index += 1;
    }

    public void addItem(String aCode, String aValue) {
        CodeBean code = new CodeBean();
        code.setCode(aCode);
        code.setValue(aValue);
        this.vector.addElement(code);
        this.index += 1;
    }

    public void addItem(String aCode, String aValue, String aType) {
        CodeBean codeBean = new CodeBean();
        codeBean.setCode(aCode);
        codeBean.setValue(aValue);
        codeBean.setType(aType);
        this.vector.addElement(codeBean);
        this.index += 1;
    }

    public int getIndex() {
        return this.index;
    }

    public CodeBean getItem(int aIndex) {
        return ((CodeBean) this.vector.elementAt(aIndex));
    }

    public String getItemCode(int aIndex) {
        CodeBean code = getItem(aIndex);
        return code.getCode();
    }

    public int getItemCount() {
        return this.vector.size();
    }

    public String getItemType(int aIndex) {
        CodeBean code = getItem(aIndex);
        return code.getType();
    }

    public String getItemValue(int aIndex) {
        CodeBean code = getItem(aIndex);
        return code.getValue();
    }

    public String getValueByCode(String code) {
        for (int i = 0; i < getItemCount(); ++i) {
            CodeBean obj = (CodeBean) this.vector.elementAt(i);
            if ((code != null) && (code.equals(obj.getCode()))) {
                return obj.getValue();
            }
        }
        return "";
    }

    public void insertItem(CodeBean codeBean, int i) {
        this.vector.insertElementAt(codeBean, i);
    }

    public void insertItem(String itemCode, String itemValue, int i) {
        CodeBean aItem = new CodeBean();
        aItem.setCode(itemCode);
        aItem.setValue(itemValue);
        this.vector.insertElementAt(aItem, i);
    }

    public void removeItem(int i) {
        this.vector.removeElementAt(i);
    }
}