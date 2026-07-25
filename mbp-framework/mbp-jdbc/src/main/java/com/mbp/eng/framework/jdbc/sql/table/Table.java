package com.mbp.eng.framework.jdbc.sql.table;

import java.io.Serializable;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.Properties;
import java.util.Vector;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class Table extends MetaData implements Serializable {
    public static final int SORT_ASC = 0;

    public static final int SORT_DESC = 1;

    protected int rowNum = 1;

    protected Vector vector;

    protected TableKey tableKey = new TableKey();

    protected Object newRow = null;

    protected TablePage tablePage = new TablePage();

    public Table() {
        this.vector = new Vector();
    }

    public Table(Vector vector, ResultSetMetaData resultSetMetaData) throws SQLException {
        super(resultSetMetaData);
        this.vector = vector;
    }

    public void setCellValue(int rowNo, int colNo, String value) {
        Vector row = (Vector) this.vector.elementAt(rowNo);

        row.setElementAt(value, colNo);
    }

    public void setCellValue(int rowNo, String column, String value) {
        int colNo = super.getColumnNo(column);

        setCellValue(rowNo, colNo, value);
    }

    public void createNewRow() {
        Vector vector = new Vector();
        for (int i = 0; i < getColumnCount(); ++i) {
            vector.addElement("");
        }
        this.vector.addElement(vector);
    }

    public String getCellValue(int rowNo, int colNo, boolean encode) {
        try {
            Vector vector = (Vector) this.vector.elementAt(rowNo);
            Object object = vector.elementAt(colNo);
            if (object == null) {
                return "";
            }

            String value = String.valueOf(vector.elementAt(colNo));

            //此处是对字符串值进行特殊处理
            /*if (encode) {
                value=HTMLHelper.encode(value);
            }*/
            return value;
        } catch (ArrayIndexOutOfBoundsException e) {
        }
        return "";
    }

    public byte[] getCellByteArray(int rowNo, int colNo) {
        Vector row = (Vector) this.vector.elementAt(rowNo);
        Object obj = row.elementAt(colNo);
        if (obj == null) {
            return null;
        }

        byte[] data = (byte[]) row.elementAt(colNo);

        return data;
    }

    public String getCellValue(int rowNo, String column, boolean encode) {
        int colNo = getColumnNo(column);

        String r = getCellValue(rowNo, colNo, encode);

        return r;
    }

    public byte[] getCellByteArray(int rowNo, String column) {
        int colNo = getColumnNo(column);

        byte[] data = getCellByteArray(rowNo, colNo);

        return data;
    }

    public TableKey getKey() {
        return this.tableKey;
    }

    public Object getNewRow() {
        return this.newRow;
    }

    public TablePage getPage() {
        return this.tablePage;
    }

    public void setKey(TableKey tableKey) {
        this.tableKey = tableKey;
    }

    public void setNewRow(Object newRow) {
        this.newRow = newRow;
    }

    public void setPage(TablePage tablePage) {
        this.tablePage = tablePage;
    }

    public void removeRowAt(int rowIndex) {
        this.vector.removeElementAt(rowIndex);
    }

    public String getColumnAlign(String column) {
        int i = getColumnNo(column);

        int columnType = ((Integer) this.columnTypes.elementAt(i)).intValue();

        if (columnType == 2) {
            return "right";
        }
        return "left";
    }

    public void addColumn(String newColumnName) {
        this.columnCount += 1;

        this.columns.put(newColumnName.toLowerCase(), String
                .valueOf(this.columnCount - 1));

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

        for (int i = 0; i <= getRowCount() - 1; ++i) {
            Vector row = (Vector) this.vector.elementAt(i);
            row.addElement("");
            this.vector.setElementAt(row, i);
        }
    }

    public Object getCellObjectValue(int rowNo, int colNo, boolean encode) {
        Vector row = (Vector) this.vector.elementAt(rowNo);
        Object obj = row.elementAt(colNo);
        if (obj == null) {
            return "";
        }
        return obj;
    }

    public Object getCellObjectValue(int rowNo, String column, boolean encode) {
        int colNo = getColumnNo(column);

        Object r = getCellObjectValue(rowNo, colNo, encode);

        return r;
    }

    public void setCellObjectValue(int rowNo, int colNo, Object value) {
        Vector row = (Vector) this.vector.elementAt(rowNo);

        row.setElementAt(value, colNo);
    }

    public void setCellObjectValue(int rowNo, String column, Object value) {
        int colNo = super.getColumnNo(column);

        Vector row = (Vector) this.vector.elementAt(rowNo);

        row.setElementAt(value, colNo);
    }

    public boolean isEmptyRow(int rowIndex) {
        boolean isEmpty = true;
        Vector vRow = (Vector) this.vector.get(rowIndex);
        Iterator iter = vRow.iterator();
        while (iter.hasNext()) {
            Object o = iter.next();
            if ((o != null) && (!(o.toString().equals("")))) {
                isEmpty = false;
                break;
            }
        }
        return isEmpty;
    }

    public Vector getAllRows() {
        return this.vector;
    }

    public Vector getRowAt(int rowNo) {
        return ((Vector) this.vector.elementAt(rowNo));
    }

    public int getRowCount() {
        return this.vector.size();
    }

    public static void main(String[] a) {
        Table table = new Table();
        table.createNewRow();
        table.addColumn("BRIEF");
        table.addColumn("accountno");
        table.addColumn("accountname");
        table.addColumn("Damt");
        table.addColumn("Camt");
    }

    public Table getSortTableForTree(int level, String parentNodeId) {
        Table treeTable = new Table();

        boolean hasLevel = false;
        boolean hasRownum = false;
        int columnCount = getColumnCount();
        for (int i = 0; i < columnCount; ++i) {
            String columnName = getColumnName(i);
            if (columnName.equalsIgnoreCase("level")) {
                hasLevel = true;
            }
            if (columnName.equalsIgnoreCase("rownum")) {
                hasRownum = true;
            }
            treeTable.addColumn(columnName);
        }

        if (!(hasLevel)) {
            treeTable.addColumn("level");
        }

        if (!(hasRownum)) {
            treeTable.addColumn("rownum");
        }

        this.rowNum = 1;
        writeNode(treeTable, level, parentNodeId);

        return treeTable;
    }

    protected void writeNode(Table treeTable, int level, String parentNodeID) {
        int rowCount = getRowCount();
        for (int i = 0; i < rowCount; ++i)
            if (getCellValue(i, "parentNodeID", true).equals(parentNodeID)) {
                treeTable.createNewRow();
                int rowNo = treeTable.getRowCount() - 1;
                String nodeId = getCellValue(i, "nodeID", true);

                int colCount = getColumnCount();
                for (int j = 0; j < colCount; ++j) {
                    treeTable.setCellValue(rowNo, j, getCellValue(i, j, true));
                }

                treeTable.setCellValue(rowNo, "level", String.valueOf(level));
                treeTable.setCellValue(rowNo, "rownum", String
                        .valueOf(this.rowNum++));
                writeNode(treeTable, level + 1, nodeId);
            }
    }

    private double revertNumber(String value) {
        String newValue = new String(value.replaceAll(",", ""));
        try {
            return Double.parseDouble(newValue);
        } catch (Exception ex) {
        }
        return 0.0D;
    }

    private boolean isNumberType(int type) {
        return ((type == -5) || (type == 3) || (type == 8) || (type == 6)
                || (type == 4) || (type == 2) || (type == 5) || (type == -6));
    }

    protected void sortAsc(String colName) {
        int rowCount = getRowCount();
        if (rowCount <= 0) {
            return;
        }

        for (int i = 0; i < rowCount; ++i)
            for (int j = i + 1; j < rowCount; ++j) {
                String iValue = getCellValue(i, colName, true);
                String jValue = getCellValue(j, colName, true);

                boolean change = false;

                int type = 0;
                try {
                    type = getColumnType(colName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (isNumberType(type))
                    try {
                        Double iD = new Double(iValue);
                        Double jD = new Double(jValue);
                        change = iD.compareTo(jD) > 0;
                    } catch (Exception ex) {
                        change = iValue.compareTo(jValue) > 0;
                    }
                else {
                    change = iValue.compareTo(jValue) > 0;
                }

                if (change) {
                    Vector iRow = new Vector(getRowAt(i));
                    Vector jRow = new Vector(getRowAt(j));
                    this.vector.set(i, jRow);
                    this.vector.set(j, iRow);
                }
            }
    }

    protected void sortDesc(String colName) {
        int rowCount = getRowCount();
        if (rowCount <= 0) {
            return;
        }

        for (int i = 0; i < rowCount; ++i)
            for (int j = i + 1; j < rowCount; ++j) {
                String iValue = getCellValue(i, colName, true);
                String jValue = getCellValue(j, colName, true);

                boolean change = false;

                int type = 0;
                try {
                    type = getColumnType(colName);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (isNumberType(type))
                    try {
                        Double iD = new Double(iValue);
                        Double jD = new Double(jValue);
                        change = iD.compareTo(jD) < 0;
                    } catch (Exception ex) {
                        change = iValue.compareTo(jValue) < 0;
                    }
                else {
                    change = iValue.compareTo(jValue) < 0;
                }

                if (change) {
                    Vector iRow = new Vector(getRowAt(i));
                    Vector jRow = new Vector(getRowAt(j));
                    this.vector.set(i, jRow);
                    this.vector.set(j, iRow);
                }
            }
    }

    protected void sortAsc(int colNum) {
        int rowCount = getRowCount();
        if (rowCount <= 0) {
            return;
        }

        for (int i = 0; i < rowCount; ++i)
            for (int j = i + 1; j < rowCount; ++j) {
                String iValue = getCellValue(i, colNum, true);
                String jValue = getCellValue(j, colNum, true);

                boolean change = false;

                int type = 0;
                try {
                    type = getColumnType(colNum);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (isNumberType(type))
                    try {
                        Double iD = new Double(iValue);
                        Double jD = new Double(jValue);
                        change = iD.compareTo(jD) > 0;
                    } catch (Exception ex) {
                        change = iValue.compareTo(jValue) > 0;
                    }
                else {
                    change = iValue.compareTo(jValue) > 0;
                }

                if (change) {
                    Vector iRow = new Vector(getRowAt(i));
                    Vector jRow = new Vector(getRowAt(j));
                    this.vector.set(i, jRow);
                    this.vector.set(j, iRow);
                }
            }
    }

    protected void sortDesc(int colNum) {
        int rowCount = getRowCount();
        if (rowCount <= 0) {
            return;
        }

        for (int i = 0; i < rowCount; ++i)
            for (int j = i + 1; j < rowCount; ++j) {
                String iValue = getCellValue(i, colNum, true);
                String jValue = getCellValue(j, colNum, true);

                boolean change = false;

                int type = 0;
                try {
                    type = getColumnType(colNum);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }

                if (isNumberType(type))
                    try {
                        Double iD = new Double(iValue);
                        Double jD = new Double(jValue);
                        change = iD.compareTo(jD) < 0;
                    } catch (Exception ex) {
                        change = iValue.compareTo(jValue) < 0;
                    }
                else {
                    change = iValue.compareTo(jValue) < 0;
                }

                if (change) {
                    Vector iRow = new Vector(getRowAt(i));
                    Vector jRow = new Vector(getRowAt(j));
                    this.vector.set(i, jRow);
                    this.vector.set(j, iRow);
                }
            }
    }

    public void sortBy(int colNum, int type) {
        if (type == 0)
            sortAsc(colNum);
        else if (type == 1)
            sortDesc(colNum);
    }

    public void sortBy(String colName, int type) {
        if (type == 0)
            sortAsc(colName);
        else if (type == 1)
            sortDesc(colName);
    }

    public void sortBy(String colName, boolean isAscending) {
        if (isAscending)
            sortAsc(colName);
        else
            sortDesc(colName);
    }

    public void sortBy(int colNum, boolean isAscending) {
        if (isAscending)
            sortAsc(colNum);
        else
            sortDesc(colNum);
    }

    public Properties getColProp() {
        return this.columns;
    }
}
