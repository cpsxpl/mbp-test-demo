package com.mbp.eng.framework.jdbc.sql.table;

import java.sql.ResultSet;
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
public class TableRowCreater {
    private ResultSet resultSet;

    private ResultSetMetaData resultSetMetaData;

    public TableRowCreater(ResultSet resultSet) throws SQLException {
        this.resultSet = resultSet;
        this.resultSetMetaData = resultSet.getMetaData();
    }

    public Vector create() throws SQLException {
        Vector rows = new Vector();

        int colCount = this.resultSetMetaData.getColumnCount();
        while (this.resultSet.next()) {
            Vector row = new Vector();
            for (int j = 0; j < colCount; j++) {
                int column = j + 1;
                Object value = this.resultSet.getObject(column);
                String tableName = this.resultSetMetaData.getTableName(column);
                String columnName = this.resultSetMetaData.getColumnName(column);
                row.addElement(value);
            }

            rows.addElement(row);
        }

        return rows;
    }
}