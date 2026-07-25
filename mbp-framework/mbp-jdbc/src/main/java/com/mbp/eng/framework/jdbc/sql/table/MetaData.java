package com.mbp.eng.framework.jdbc.sql.table;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Vector;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class MetaData implements Serializable, ResultSetMetaData {
    protected int columnCount = 0;

    protected Properties columns = new Properties();

    protected Vector catalogNames = new Vector();

    protected Vector columnMaxSizes = new Vector();

    protected Vector columnDisplaySizes = new Vector();

    protected Vector columnLabels = new Vector();

    protected Vector columnNames = new Vector();

    protected Vector columnTypes = new Vector();

    protected Vector columnTypeNames = new Vector();

    protected Vector precisions = new Vector();

    protected Vector scales = new Vector();

    protected Vector schemaNames = new Vector();

    protected Vector tableNames = new Vector();

    protected Vector isAutoIncrements = new Vector();

    protected Vector isCaseSensitives = new Vector();

    protected Vector isCurrencys = new Vector();

    protected Vector isDefinitelyWritables = new Vector();

    protected Vector isNullables = new Vector();

    protected Vector isReadOnlys = new Vector();

    protected Vector isSearchables = new Vector();

    protected Vector isSigneds = new Vector();

    protected Vector isWritables = new Vector();

    protected Vector isVisibles = new Vector();

    protected Vector isSelectables = new Vector();

    protected Vector options = new Vector();

    protected int columnMaxDisplaySize = 30;

    protected Vector columnClassNames = new Vector();

    public MetaData() {
    }

    public MetaData(ResultSetMetaData resultSetMetaData) throws SQLException {
        this.columnCount = resultSetMetaData.getColumnCount();
        for (int i = 0; i < resultSetMetaData.getColumnCount(); ++i) {
            this.columns.put(resultSetMetaData.getColumnName(i + 1).toLowerCase(), String.valueOf(i));

            //获取指定列的表目录名称
            //this.catalogNames.addElement(resultSetMetaData.getCatalogName(i + 1).toLowerCase());
            //如果调用方法 ResultSet.getObject 从列中获取值,则返回构造其实例的 Java 类的完全限定名称
            this.columnClassNames.addElement(resultSetMetaData.getColumnClassName(i + 1).toLowerCase());

            //指示指定列的最大标准宽度,以字符为单位
            this.columnDisplaySizes.addElement(new Integer(resultSetMetaData.getColumnDisplaySize(i + 1)));
            this.columnMaxSizes.addElement(new Integer(resultSetMetaData.getColumnDisplaySize(i + 1)));

            //获取用于打印输出和显示的指定列的建议标题
            this.columnLabels.addElement(resultSetMetaData.getColumnLabel(i + 1).toLowerCase());
            //获取指定列的名称
            this.columnNames.addElement(resultSetMetaData.getColumnName(i + 1).toLowerCase());

            //获取指定列的 SQL 类型
            this.columnTypes.addElement(new Integer(resultSetMetaData.getColumnType(i + 1)));
            //获取指定列的数据库特定的类型名称
            this.columnTypeNames.addElement(resultSetMetaData.getColumnTypeName(i + 1).toLowerCase());

            this.precisions.addElement(new Integer(0));
            //获取指定列的小数点右边的位数
            this.scales.addElement(new Integer(resultSetMetaData.getScale(i + 1)));

            //获取指定列的表模式
            //this.schemaNames.addElement(resultSetMetaData.getSchemaName(i + 1).toLowerCase());
            //获取指定列的名称
            //this.tableNames.addElement(resultSetMetaData.getTableName(i + 1).toLowerCase());

            //指示是否自动为指定列进行编号
            this.isAutoIncrements.addElement(new Boolean(resultSetMetaData.isAutoIncrement(i + 1)));
            //指示列的大小写是否有关系
            this.isCaseSensitives.addElement(new Boolean(resultSetMetaData.isCaseSensitive(i + 1)));

            //指示指定的列是否是一个哈希代码值
            this.isCurrencys.addElement(new Boolean(resultSetMetaData.isCurrency(i + 1)));
            //指示在指定的列上进行写操作是否明确可以获得成功
            //this.isDefinitelyWritables.addElement(new Boolean(resultSetMetaData.isDefinitelyWritable(i + 1)));

            //指示指定列中的值是否可以为 null
            this.isNullables.addElement(new Integer(resultSetMetaData.isNullable(i + 1)));
            //指示指定的列是否明确不可写入
            this.isReadOnlys.addElement(new Boolean(resultSetMetaData.isReadOnly(i + 1)));

            //指示是否可以在 where 子句中使用指定的列
            //this.isSearchables.addElement(new Boolean(resultSetMetaData.isSearchable(i + 1)));
            //指示指定列中的值是否带正负号
            //this.isSigneds.addElement(new Boolean(resultSetMetaData.isSigned(i + 1)));

            //指示在指定的列上进行写操作是否可以获得成功
            //this.isWritables.addElement(new Boolean(resultSetMetaData.isWritable(i + 1)));

            this.isVisibles.addElement(new Boolean(true));
            this.isSelectables.addElement(new Boolean(false));
        }
    }

    public void addColumn(String newColumnName) {
        this.columnCount += 1;

        this.columns.put(newColumnName, String.valueOf(this.columnCount));

        this.catalogNames.addElement("");
        this.columnClassNames.addElement("");

        this.columnDisplaySizes.addElement(new Integer(30));
        this.columnMaxSizes.addElement(new Integer(30));

        this.columnLabels.addElement("");
        this.columnNames.addElement(newColumnName);

        this.columnTypes.addElement("");
        this.columnTypeNames.addElement("");

        this.precisions.addElement(null);
        this.scales.addElement(null);

        this.schemaNames.addElement("");
        this.tableNames.addElement("");

        this.isAutoIncrements.addElement(new Boolean(false));
        this.isCaseSensitives.addElement(new Boolean(false));

        this.isCurrencys.addElement(new Boolean(false));
        this.isDefinitelyWritables.addElement(new Boolean(false));

        this.isNullables.addElement(null);
        this.isReadOnlys.addElement(new Boolean(false));

        this.isSearchables.addElement(new Boolean(false));
        this.isSigneds.addElement(new Boolean(false));

        this.isWritables.addElement(new Boolean(false));

        this.isVisibles.addElement(new Boolean(true));
        this.isSelectables.addElement(new Boolean(false));
    }

    public String getCatalogName(int columnNo) {
        return ((String) this.catalogNames.elementAt(columnNo));
    }

    public String getCatalogName(String columnName) {
        int colNo = getColumnNo(columnName);
        return ((String) this.catalogNames.elementAt(colNo));
    }

    public String getColumnClassName(int columnNo) {
        return ((String) this.columnClassNames.elementAt(columnNo));
    }

    public String getColumnClassName(String columnName) {
        int colNo = getColumnNo(columnName);
        return ((String) this.columnClassNames.elementAt(colNo));
    }

    public int getColumnCount() {
        return this.columns.size();
    }

    public int getColumnDisplaySize(int columnNo) {
        int r = ((Integer) this.columnDisplaySizes.elementAt(columnNo))
                .intValue();

        if (r >= this.columnMaxDisplaySize)
            r = this.columnMaxDisplaySize;
        else if (getPrecision(columnNo) != 0) {
            r = getPrecision(columnNo);
        }

        return r;
    }

    public int getColumnDisplaySize(String column) {
        int colNo = getColumnNo(column);
        int r = getColumnDisplaySize(colNo);
        return r;
    }

    public String getColumnLabel(int columnNo) {
        return ((String) this.columnLabels.elementAt(columnNo));
    }

    public String getColumnLabel(String column) {
        int i = getColumnNo(column);
        return ((String) this.columnLabels.elementAt(i));
    }

    public int getColumnMaxSize(int columnNo) {
        int r = ((Integer) this.columnMaxSizes.elementAt(columnNo)).intValue();

        if (getPrecision(columnNo) != 0) {
            r = getPrecision(columnNo);
        }

        return r;
    }

    public int getColumnMaxSize(String column) {
        int colNo = getColumnNo(column);

        int r = getColumnMaxSize(colNo);

        return r;
    }

    public String getColumnName(int columnNo) {
        return ((String) this.columnNames.elementAt(columnNo));
    }

    public int getColumnNo(String column) {
        int i = Integer.parseInt(this.columns.getProperty(column.trim().toLowerCase()));
        return i;
    }

    public int getColumnType(int columnNo) {
        return ((Integer) this.columnTypes.elementAt(columnNo)).intValue();
    }

    public int getColumnType(String column) {
        int i = getColumnNo(column);
        return ((Integer) this.columnTypes.elementAt(i)).intValue();
    }

    public String getColumnTypeName(int columnNo) {
        return ((String) this.columnTypeNames.elementAt(columnNo));
    }

    public String getColumnTypeName(String column) {
        int i = getColumnNo(column);
        return ((String) this.columnTypeNames.elementAt(i));
    }

    public int getPrecision(int columnNo) {
        return ((Integer) this.precisions.elementAt(columnNo)).intValue();
    }

    public int getPrecision(String column) {
        int i = getColumnNo(column);
        return ((Integer) this.precisions.elementAt(i)).intValue();
    }

    public int getScale(int columnNo) {
        return ((Integer) this.columnDisplaySizes.elementAt(columnNo))
                .intValue();
    }

    public int getScale(String column) {
        int colNo = getColumnNo(column);
        return ((Integer) this.columnDisplaySizes.elementAt(colNo)).intValue();
    }

    public String getSchemaName(int columnNo) {
        return ((String) this.schemaNames.elementAt(columnNo));
    }

    public String getSchemaName(String columnName) {
        int column = getColumnNo(columnName);
        return ((String) this.schemaNames.elementAt(column));
    }

    public String getTableName(int columnNo) {
        return ((String) this.columnDisplaySizes.elementAt(columnNo));
    }

    public String getTableName(String columnName) {
        int column = getColumnNo(columnName);

        return ((String) this.columnDisplaySizes.elementAt(column));
    }

    public int getVisibleColumnCount() {
        int r = 0;
        for (int i = 0; i < this.columnCount; ++i) {
            if (isVisible(i)) {
                ++r;
            }
        }
        return r;
    }

    public boolean isAutoIncrement(int columnNo) {
        return ((Boolean) this.isAutoIncrements.elementAt(columnNo))
                .booleanValue();
    }

    public boolean isAutoIncrement(String columnName) {
        int column = getColumnNo(columnName);
        return ((Boolean) this.isAutoIncrements.elementAt(column))
                .booleanValue();
    }

    public boolean isBoolean(int columnNo) {
        return (this.columnTypeNames.elementAt(columnNo).equals("BOOLEAN"));
    }

    public boolean isBoolean(String column) {
        int i = getColumnNo(column);

        return (this.columnTypeNames.elementAt(i).equals("BOOLEAN"));
    }

    public boolean isCaseSensitive(int columnNo) {
        return ((Boolean) this.isCaseSensitives.elementAt(columnNo))
                .booleanValue();
    }

    public boolean isCaseSensitive(String columnName) {
        int column = getColumnNo(columnName);
        return ((Boolean) this.isCaseSensitives.elementAt(column))
                .booleanValue();
    }

    public boolean isCurrency(int columnNo) {
        return ((Boolean) this.isCurrencys.elementAt(columnNo)).booleanValue();
    }

    public boolean isCurrency(String columnName) {
        int column = getColumnNo(columnName);
        return ((Boolean) this.isCurrencys.elementAt(column)).booleanValue();
    }

    public boolean isDate(int columnNo) {
        return (this.columnTypeNames.elementAt(columnNo).equals("date"));
    }

    public boolean isDate(String columnName) {
        int column = getColumnNo(columnName);

        return (this.columnTypeNames.elementAt(column).equals("date"));
    }

    public boolean isDefinitelyWritable(int columnNo) {
        return ((Boolean) this.isDefinitelyWritables.elementAt(columnNo))
                .booleanValue();
    }

    public boolean isDefinitelyWritable(String columnName) {
        int column = getColumnNo(columnName);
        return ((Boolean) this.isDefinitelyWritables.elementAt(column))
                .booleanValue();
    }

    public int isNullable(int columnNo) {
        return ((Integer) this.isNullables.elementAt(columnNo)).intValue();
    }

    public int isNullable(String column) {
        int i = getColumnNo(column);
        return ((Integer) this.isNullables.elementAt(i)).intValue();
    }

    public boolean isReadOnly(int columnNo) {
        return ((Boolean) this.isReadOnlys.elementAt(columnNo)).booleanValue();
    }

    public boolean isReadOnly(String columnName) {
        int column = getColumnNo(columnName);
        return ((Boolean) this.isReadOnlys.elementAt(column)).booleanValue();
    }

    public boolean isSearchable(int columnNo) {
        return ((Boolean) this.isSearchables.elementAt(columnNo))
                .booleanValue();
    }

    public boolean isSearchable(String columnName) {
        int column = getColumnNo(columnName);
        return ((Boolean) this.isSearchables.elementAt(column)).booleanValue();
    }

    public boolean isSelectable(int columnNo) {
        return ((Boolean) this.isSelectables.elementAt(columnNo))
                .booleanValue();
    }

    public boolean isSelectable(String column) {
        int i = getColumnNo(column);
        return ((Boolean) this.isSelectables.elementAt(i)).booleanValue();
    }

    public boolean isSigned(int columnNo) {
        return ((Boolean) this.isSigneds.elementAt(columnNo)).booleanValue();
    }

    public boolean isSigned(String columnName) {
        int column = getColumnNo(columnName);
        return ((Boolean) this.isSigneds.elementAt(column)).booleanValue();
    }

    public boolean isVisible(int columnNo) {
        boolean r = ((Boolean) this.isVisibles.elementAt(columnNo))
                .booleanValue();
        return r;
    }

    public boolean isVisible(String column) {
        int i = getColumnNo(column);
        return ((Boolean) this.isVisibles.elementAt(i)).booleanValue();
    }

    public boolean isWritable(int columnNo) {
        return ((Boolean) this.isWritables.elementAt(columnNo)).booleanValue();
    }

    public boolean isWritable(String columnName) {
        int column = getColumnNo(columnName);
        return ((Boolean) this.isWritables.elementAt(column)).booleanValue();
    }

    public void setAllCaseSensitive(boolean value) {
        for (int i = 0; i < this.isCaseSensitives.size(); ++i)
            this.isCaseSensitives.setElementAt(new Boolean(value), i);
    }

    public void setAllColumnDisplaySize(int value) {
        for (int i = 0; i < this.columnCount; ++i) {
            this.columnDisplaySizes.setElementAt(new Integer(value), i);
        }
    }

    public void setAllColumnMaxDisplaySize(int value) {
        this.columnMaxDisplaySize = value;
    }

    public void setAllNullable(boolean value) {
        for (int i = 0; i < this.isNullables.size(); ++i)
            this.isNullables.setElementAt(new Boolean(value), i);
    }

    public void setAllReadOnly(boolean value) {
        for (int i = 0; i < this.isReadOnlys.size(); ++i)
            this.isReadOnlys.setElementAt(new Boolean(value), i);
    }

    public void setAllSearchable(boolean value) {
        for (int i = 0; i < this.isSearchables.size(); ++i)
            this.isSearchables.setElementAt(new Boolean(value), i);
    }

    public void setAllSelectable(boolean value) {
        for (int i = 0; i < this.isSelectables.size(); ++i)
            this.isSelectables.setElementAt(new Boolean(value), i);
    }

    public void setAllVisible(boolean value) {
        for (int i = 0; i < this.isVisibles.size(); ++i)
            this.isVisibles.setElementAt(new Boolean(value), i);
    }

    public void setAllWritable(boolean value) {
        for (int i = 0; i < this.isWritables.size(); ++i)
            this.isWritables.setElementAt(new Boolean(value), i);
    }

    public void setCatalogName(int columnNo, String value) {
        this.catalogNames.setElementAt(value, columnNo);
    }

    public void setCatalogName(String columnName, String value) {
        int column = getColumnNo(columnName);
        this.catalogNames.setElementAt(value, column);
    }

    public void setColumnClassName(int columnNo, String value) {
        this.columnClassNames.setElementAt(value, columnNo);
    }

    public void setColumnClassName(String columnName, String value) {
        int column = getColumnNo(columnName);
        this.columnClassNames.setElementAt(value, column);
    }

    public void setColumnDisplaySize(int columnNo, int value) {
        this.columnDisplaySizes.setElementAt(new Integer(value), columnNo);
    }

    public void setColumnDisplaySize(String column, int value) {
        int i = getColumnNo(column);

        this.columnDisplaySizes.setElementAt(new Integer(value), i);
    }

    public void setColumnLabel(int columnNo, String value) {
        this.columnLabels.setElementAt(value, columnNo);
    }

    public void setColumnLabel(String column, String value) {
        int colNo = getColumnNo(column);

        this.columnLabels.setElementAt(value, colNo);
    }

    public void setColumnMaxSize(int columnNo, int value) {
        this.columnMaxSizes.setElementAt(new Integer(value), columnNo);
    }

    public void setColumnMaxSize(String column, int value) {
        int colNo = getColumnNo(column);
        this.columnMaxSizes.setElementAt(new Integer(value), colNo);
    }

    public void setColumnType(int columnNo, String value) {
        this.columnTypes.setElementAt(value, columnNo);
    }

    public void setColumnType(int columnNo, int type) {
        this.columnTypes.setElementAt(new Integer(type), columnNo);
    }

    public void setColumnType(String column, String value) {
        int i = getColumnNo(column);

        this.columnTypes.setElementAt(value, i);
    }

    public void setColumnType(String column, int type) {
        int i = getColumnNo(column);

        this.columnTypes.setElementAt(new Integer(type), i);
    }

    public void setColumnTypeName(int columnNo, String value) {
        this.columnTypeNames.setElementAt(value, columnNo);
    }

    public void setColumnTypeName(String column, String value) {
        int i = getColumnNo(column);

        this.columnTypeNames.setElementAt(value, i);
    }

    public void setSchemaName(int columnNo, String value) {
        this.schemaNames.setElementAt(value, columnNo);
    }

    public void setSchemaName(String column, String value) {
        int colNo = getColumnNo(column);

        this.schemaNames.setElementAt(value, colNo);
    }

    public void setSelectable(int columnNo, boolean value) {
        this.isSelectables.setElementAt(new Boolean(value), columnNo);
    }

    public void setSelectable(String column, boolean value) {
        int i = getColumnNo(column);

        this.isSelectables.setElementAt(new Boolean(value), i);
    }

    public void setTableName(int columnNo, String value) {
        this.tableNames.setElementAt(value, columnNo);
    }

    public void setTableName(String column, String value) {
        int colNo = getColumnNo(column);

        this.tableNames.setElementAt(value, colNo);
    }

    public void setVisible(int columnNo, boolean value) {
        this.isVisibles.setElementAt(new Boolean(value), columnNo);
    }

    public void setVisible(String column, boolean value) {
        int i = getColumnNo(column);

        this.isVisibles.setElementAt(new Boolean(value), i);
    }

    public void setWritable(int columnNo, boolean value) {
        this.isWritables.setElementAt(new Boolean(value), columnNo);
    }

    public void setWritable(String column, boolean value) {
        int i = getColumnNo(column);

        this.isWritables.setElementAt(new Boolean(value), i);
    }

    @Override
    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public <T> T unwrap(Class<T> iface) throws SQLException {
        // TODO Auto-generated method stub
        return null;
    }
}