package com.mbp.eng.framework.jdbc.sql.manager;

import com.mbp.eng.framework.jdbc.sql.table.NullConnectionException;
import com.mbp.eng.framework.jdbc.sql.table.NullPreStatementException;
import com.mbp.eng.framework.jdbc.sql.table.Table;

import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class PreStmtManager extends SQLManager {
    protected PreparedStatement preparedStatement = null;

    public PreStmtManager() {
    }

    public PreStmtManager(String dsName) {
        super(dsName);
    }

    private void closePreStmt() throws SQLException {
        if (this.preparedStatement != null) {
            try {
                this.preparedStatement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        this.preparedStatement = null;
    }

    public void setPreStmt(String sql) throws SQLException, NullConnectionException {
        if (this.preparedStatement != null) {
            this.preparedStatement.close();
            this.preparedStatement = null;
        }
        if ((this.connection == null) || (this.connIsClosed)) {
            getDBConnection();
        }
        this.preparedStatement = this.connection.prepareStatement(sql);
        DBConnection.debug(sql);
    }

    public void setObject(int index, Object param) throws SQLException, NullPreStatementException {
        if (this.preparedStatement == null)
            throw new NullPreStatementException("PreparedStatement is null");
        param = (param == null) ? "" : param.toString().trim();
        this.preparedStatement.setObject(index, param);
        DBConnection.debug(index + ":  " + param);
    }

    public Table search() throws SQLException {
        try {
            ResultSet resultSet = this.preparedStatement.executeQuery();
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            Vector rows = new Vector();
            Vector row;
            for (; resultSet.next(); rows.addElement(row)) {
                row = new Vector();
                for (int j = 0; j < resultSetMetaData.getColumnCount(); ++j)
                    row.addElement(resultSet.getObject(j + 1));
            }
            Table t = new Table(rows, resultSetMetaData);

            resultSet.close();

            return t;
        } finally {
            closePreStmt();
            release();
        }
    }

    public int update() throws SQLException {
        try {
            return this.preparedStatement.executeUpdate();
        } finally {
            closePreStmt();
            release();
        }
    }

    public void transformToPreStmt(String query)
            throws SQLException, NullConnectionException, NullPreStatementException {
        if (query.indexOf("'") == -1) {
            setPreStmt(query);
            return;
        }

        ArrayList targetStrings = new ArrayList();

        String processedQuerry = "";

        int startIndex = 0;

        int index = startIndex;

        int literalStart = -1;
        int iQueryLen = query.length();

        while (index < iQueryLen) {
            if (query.charAt(index) == '\'') {
                if ((literalStart == -1) && (index + 1 < query.length())) {
                    literalStart = index + 1;
                } else {
                    String targetString = query.substring(literalStart, index);
                    targetStrings.add(targetString);

                    literalStart = -1;
                    processedQuerry = processedQuerry + "?";
                    ++index;
                }
            }
            if ((index < query.length()) && (literalStart == -1)) {
                processedQuerry = processedQuerry + query.charAt(index);
            }

            ++index;
        }

        setPreStmt(processedQuerry + " ");

        Iterator it = targetStrings.iterator();
        int counter = 1;
        while (it.hasNext()) {
            String arg = (String) it.next();

            setObject(counter++, arg);
        }
    }

    public void transformToPreStmt(String query, char separator)
            throws SQLException, NullConnectionException, NullPreStatementException {
        if (query.indexOf(String.valueOf(separator)) == -1) {
            setPreStmt(query);
            return;
        }

        ArrayList arrayList = new ArrayList();

        String processedQuerry = "";

        int startIndex = 0;

        int index = startIndex;

        int literalStart = -1;
        int iQueryLen = query.length();

        while (index < iQueryLen) {
            if (query.charAt(index) == separator) {
                if ((literalStart == -1) && (index + 1 < query.length())) {
                    literalStart = index + 1;
                } else {
                    String targetString = query.substring(literalStart, index);
                    arrayList.add(targetString);

                    literalStart = -1;
                    processedQuerry = processedQuerry + "?";
                    ++index;
                }
            }
            if ((index < query.length()) && (literalStart == -1)) {
                processedQuerry = processedQuerry + query.charAt(index);
            }

            ++index;
        }

        setPreStmt(processedQuerry + " ");

        Iterator iterator = arrayList.iterator();
        int counter = 1;
        while (iterator.hasNext()) {
            String arg = (String) iterator.next();

            setObject(counter++, arg);
        }
    }

    public void addBatch() throws SQLException {
        try {
            this.preparedStatement.addBatch();
        } catch (SQLException se) {
            DBConnection.debug(se);
            release();
            throw se;
        }
    }

    public int[] executeBatch() throws SQLException, BatchUpdateException {
        try {
            int[] count = this.preparedStatement.executeBatch();
            return count;
        } catch (BatchUpdateException b) {
            throw b;
        } catch (SQLException se) {
            throw se;
        } finally {
            release();
        }
    }
}
