package com.mbp.eng.framework.jdbc.sql.table;

import java.util.HashMap;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class LinkSelectList extends SelectList {
    private HashMap hashMap;

    public LinkSelectList() {
        hashMap = new HashMap();
    }

    public void addList(String code, SelectList list) {
        hashMap.put(code, list);
    }

    public SelectList getListByCode(String code) {
        Object object = hashMap.get(code);
        SelectList selectList = object != null ? (SelectList) object : null;
        return selectList;
    }
}
