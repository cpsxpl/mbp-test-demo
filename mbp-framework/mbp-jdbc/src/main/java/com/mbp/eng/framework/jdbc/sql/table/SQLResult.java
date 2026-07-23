package com.mbp.eng.framework.jdbc.sql.table;

import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Vector;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class SQLResult extends MetaData {
    protected Vector vector;

    public SQLResult() {
        this.vector = new Vector();
    }

    public SQLResult(Vector rows, ResultSetMetaData resultSetMetaData) throws SQLException {
        super(resultSetMetaData);
        this.vector = rows;
    }

    public String getCellValue(int rowNo, int columnNo) {
        Vector row = new Vector();
        row = (Vector) this.vector.elementAt(rowNo);

        String cellValue = null;
        Object obj = row.elementAt(columnNo);

        if (obj == null)
            return null;
        cellValue = String.valueOf(obj);

        return cellValue;
    }

    public String getCellValue(int rowNo, String columnName) {
        int columnNo = super.getColumnNo(columnName);

        String cellValue = getCellValue(rowNo, columnNo);
        return cellValue;
    }

    public Vector getAllRows() {
        return this.vector;
    }

    public Vector getRowAt(int rowNo) {
        return (Vector) this.vector.elementAt(rowNo);
    }

    public int getRowCount() {
        return this.vector.size();
    }
}