package com.mbp.eng.framework.jdbc.sql.manager;

import com.mbp.eng.framework.jdbc.sql.table.ApplicationException;
import com.mbp.eng.framework.jdbc.sql.table.NullConnectionException;
import com.mbp.eng.framework.jdbc.sql.table.SQLResult;
import com.mbp.eng.framework.jdbc.sql.table.Table;

import java.sql.BatchUpdateException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Vector;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve
 * @Date: 2016-05-18
 */
public class SQLManager {
    protected Connection connection = null;

    protected Statement statement = null;

    protected boolean connIsClosed = false;

    String dsName = null;

    public SQLManager() {
    }

    public SQLManager(String dsName) {
        this.dsName = dsName;
    }

    protected void getDBConnection() throws SQLException, NullConnectionException {
        if (null == connection || connIsClosed) {
            connection = DBConnection.getConnection();
            connIsClosed = false;
        }
    }

    /**
     * 得到Statement对象
     *
     * @throws NullConnectionException
     * @throws SQLException
     */
    protected void getStatement() throws SQLException, NullConnectionException {
        if (null == connection || connIsClosed) {
            getDBConnection();
        }
        statement = connection.createStatement();
    }

    protected void release() {
        try {
            if (null != statement) {
                statement.close();
                statement = null;
            }
            if (null != connection && !connIsClosed) {
                if (isJoinTrans()) {
                    connection.setAutoCommit(true);
                }
                connection.close();
                connIsClosed = true;
            }
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
            DBConnection.debug("closing connection throws Exception!");
            DBConnection.debug(sqlException);
        }
    }

    protected void finalize() {
        try {
            super.finalize();

            release();
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
    public static SQLResult find(String sql) throws SQLException, NullConnectionException {
        return execQueryStmt(sql, true);
    }
    public static SQLResult search(String sql) throws SQLException, NullConnectionException {
        return execQueryStmt(sql, false);
    }
    public Table search(String queryStr, int currentPage, int linesPerPage)
            throws SQLException, NullConnectionException, ApplicationException {
        return search(queryStr, currentPage, linesPerPage);
    }
    /**
     * 执行删除操作
     *
     * @param sql String 删除语句
     * @return int 返回删除成功的数量
     * @throws NullConnectionException
     * @throws SQLException
     */
    public int delete(String sql) throws SQLException, NullConnectionException {
        return execUpdateStmt(sql);
    }

    /**
     * 执行插入语句操作
     *
     * @param sql String 插入语句
     * @return int 操做返回插入成功的数量
     * @throws NullConnectionException
     * @throws SQLException
     */
    public int insert(String sql) throws SQLException, NullConnectionException {
        return execUpdateStmt(sql);
    }
    /**
     * 执行更新造作
     *
     * @param sql String 更新语句
     * @return int 返回的更新数量
     * @throws NullConnectionException
     * @throws SQLException
     */
    public int update(String sql) throws SQLException, NullConnectionException {
        return execUpdateStmt(sql);
    }

    public void begin() throws SQLException, NullConnectionException {
        if (null == statement) {
            getStatement();
        }
        connection.setAutoCommit(false);
        DBConnection.debug(">>>>>>>>>>>> transaction begin <<<<<<<<<<<<<");
    }

    /**
     * 提交事务
     */
    public void commit() throws SQLException, DAOException {
        if (connection == null)
            throw new DAOException("DB connection is not avaiable.");
        try {
            connection.commit();
            DBConnection.debug(">>>>>>>>>>>> transaction commit <<<<<<<<<<<<<");
        } catch (SQLException sqle) {
            rollback();
        } finally {
            connection.setAutoCommit(true);
            release();
        }
    }

    /**
     * 回滚事务
     */
    public void rollback() throws SQLException {
        try {
            connection.rollback();
            DBConnection.debug(">>>>>>>>>>>> transaction rollback <<<<<<<<<<<<<");
        } catch (SQLException sqle) {
            throw sqle;
        } finally {
            connection.setAutoCommit(true);
            release();
        }
    }

    public static SQLResult execQueryStmt(String sql, boolean isFind) throws SQLException, NullConnectionException {
        Connection connection = DBConnection.getConnection();
        Statement statement = null;
        ResultSet resultSet = null;

        String randomNo = getRandomNo();
        try {
            statement = connection.createStatement();

            DBConnection.debug(randomNo + sql);

            long startTime = System.currentTimeMillis();

            resultSet = statement.executeQuery(sql);

            long endTime = System.currentTimeMillis();

            DBConnection.debug(randomNo + "use time: " + (endTime - startTime) + "ms");

            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();

            Vector rows = new Vector();
            while (resultSet.next()) {
                Vector row = new Vector();
                for (int j = 0; j < resultSetMetaData.getColumnCount(); j++) {
                    row.addElement(resultSet.getObject(j + 1));
                }
                rows.addElement(row);
                if (isFind) {
                    break;
                }
            }

            SQLResult sqlResult = new SQLResult(rows, resultSetMetaData);

            DBConnection.debug(randomNo + "search row number: " + sqlResult.getRowCount());

            return sqlResult;
        } catch (SQLException sqle) {
            DBConnection.debug(randomNo + " exception: " + sqle);
            DBConnection.debug(sqle);
            throw sqle;
        } finally {
            try {
                if (null != resultSet) {
                    resultSet.close();
                }
                if (null != statement) {
                    statement.close();
                }
                if (null != connection) {
                    connection.close();
                }
            } catch (SQLException s) {
                throw s;
            }
        }
    }

    public int execUpdateStmt(String sql) throws SQLException, NullConnectionException {
        String randomNo = getRandomNo();

        try {
            if (null == statement) {
                getStatement();
            }

            DBConnection.debug(randomNo + sql);

            long startTime = System.currentTimeMillis();

            int rs = statement.executeUpdate(sql);

            long endTime = System.currentTimeMillis();

            DBConnection.debug(randomNo + "use time: " + (endTime - startTime) + "ms");

            DBConnection.debug(randomNo + "update row number: " + rs);

            return rs;
        } catch (SQLException se) {
            if (isJoinTrans()) {
                rollback();
            }
            DBConnection.debug(randomNo + " exception: " + se);
            DBConnection.debug(se);
            throw se;
        } finally {
            if (!isJoinTrans()) {
                release();
            }
        }
    }

    public int execUpdateProcedure(String procudureStr) throws SQLException,
            NullConnectionException {
        String randomNo = getRandomNo();

        try {
            // if (null == connection) {
            getDBConnection();
            // }
            CallableStatement callStmt = connection.prepareCall(procudureStr);

            DBConnection.debug(randomNo + procudureStr);

            long startTime = System.currentTimeMillis();

            int rs = callStmt.executeUpdate();

            long endTime = System.currentTimeMillis();

            DBConnection.debug(randomNo + "use time: " + (endTime - startTime)
                    + "ms");
            DBConnection.debug(randomNo + "update row number: " + rs);
            callStmt.close();
            return rs;
        } catch (SQLException se) {
            if (isJoinTrans()) {
                rollback();
            }
            DBConnection.debug(randomNo + " exception: " + se);
            DBConnection.debug(se);
            throw se;
        } finally {
            if (!isJoinTrans()) {
                release();
            }
        }
    }
    public boolean isJoinTrans() throws SQLException {
        if (connIsClosed) {
            return false;
        } else {
            return !connection.getAutoCommit();
        }
    }

    protected static String getRandomNo() {
        return "SQL " + String.valueOf((int) (100000 * Math.random())) + ": ";
    }
    /**
     * 添加批处理语句
     *
     * @param sql Stirng 批处理的一条语句
     * @throws SQLException
     * @throws NullConnectionException
     */
    public void addBatch(String sql) throws SQLException, NullConnectionException {
        try {
            if (null == statement) {
                getStatement();
            }
            statement.addBatch(sql);
            DBConnection.debug("addBatch:" + sql);
        } catch (SQLException se) {
            DBConnection.debug(se);
            release();
            throw se;
        }
    }

    /**
     * 清空所有的批处理语句
     *
     * @throws SQLException
     */
    public void clearBatch() throws SQLException {
        try {
            statement.clearBatch();
        } catch (SQLException se) {
            DBConnection.debug(se);
            release();
            throw se;
        }
    }

    /**
     * 执行批处理语句
     *
     * @return int
     * @throws BatchUpdateException
     * @throws SQLException
     */
    public int[] executeBatch() throws BatchUpdateException, SQLException {
        try {
            int[] count = statement.executeBatch();
            return count;
        } catch (BatchUpdateException b) {
            b.printStackTrace();
            throw b;
        } catch (SQLException se) {
            se.printStackTrace();
            throw se;
        } finally {
            this.release();
        }
    }
}