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
public class TableKey extends Vector implements Serializable {
    public void addKey(Object object) {
        super.addElement(object);
    }

    public Object getKeyAt(int rowNo) {
        return super.elementAt(rowNo);
    }

    public int getKeyCount() {
        return super.size();
    }

    public void removeKeyAt(int rowNo) {
        super.removeElementAt(rowNo);
    }

    public void setKeyAt(int rowNo, Object key) {
        super.setElementAt(key, rowNo);
    }
}
