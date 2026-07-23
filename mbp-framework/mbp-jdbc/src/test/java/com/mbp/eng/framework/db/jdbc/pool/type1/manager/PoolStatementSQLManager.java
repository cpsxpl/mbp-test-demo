package com.mbp.eng.framework.db.jdbc.pool.type1.manager;

import com.mbp.eng.framework.db.jdbc.pool.type1.DBConnectionManager;
import com.mbp.eng.framework.common.util.time.TimeUtil;
import com.mbp.eng.framework.jdbc.sql.table.NullConnectionException;
import com.mbp.eng.framework.jdbc.sql.table.Table;

import java.io.PrintWriter;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Vector;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * This class should preserve.
 *
 * @author Chen Pei
 * @preserve 此类里面所有方法
 * 一半需要Connection参数,
 * 一半需要通过DBConnectionManager类
 * 读取配置文件方式获取链接.
 * MySQLJdbcSQLmanager里面的所有方法在此类里必须存在
 * @Date: 2016-05-18
 */
public class PoolStatementSQLManager {
    private static boolean isLog;

    public static boolean isDebug;

    private static PrintWriter logPrint;

    private static final Random random = new Random();

    protected static Connection connection = null;

    protected static PreparedStatement preparedStatement = null;

    protected static Statement statement = null;

    public PoolStatementSQLManager() {
    }

    /**
     * 把要查询的SQL结果集返回到List<Map < String, Object>>
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static BlockingQueue<Map<String, Object>> searchBlockingQueueData(String dbName, String sql) throws SQLException {
        BlockingQueue<Map<String, Object>> searchSQL = searchBlockingQueueData(dbName, sql);
        /*while (searchSQL.size() > 0) {
            try {
                //获取数据对象
                Map<String, Object> item = searchSQL.take();
                //System.out.println(item);
                //String colValue = item.get("colName").toString();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }*/
        return searchSQL;
    }

    /**
     * 把要查询的SQL结果集返回到BlockingQueue<Map < String, Object>>
     *
     * @param searchSQL
     * @param dbName
     * @param param
     * @return
     * @throws SQLException
     */
    protected static BlockingQueue<Map<String, Object>> searchBlockingQueueData(String dbName, String searchSQL, Object... param) throws SQLException {
        isDebug = true;
        Connection connection = DBConnectionManager.getInstance().getConnection(dbName);
        String randomNo = getRandomNo();

        int rowCount = getRowCount(connection, searchSQL, randomNo);

        long startTime = System.currentTimeMillis();
        PreparedStatement preparedStatement = connection.prepareStatement(searchSQL);

        connection = connection;
        preparedStatement = preparedStatement;

        //给参数赋值
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                preparedStatement.setObject(i, param[i]);
            }
        }
        debug(randomNo + "Running:" + searchSQL);
        //ResultSet resultSet = preparedStatement.executeQuery(searchSQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        long endTime = System.currentTimeMillis();
        //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
        debug(randomNo + "查询数据 use time: " + TimeUtil.formatTime(endTime - startTime));
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int colCount = resultSetMetaData.getColumnCount();

        /**
         使用BlockingQueue的关键技术点如下:
         1.BlockingQueue定义的常用方法如下:
         1)add(anObject):把anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则招聘异常
         2)offer(anObject):表示如果可能的话,将anObject加到BlockingQueue里,即如果BlockingQueue可以容纳,则返回true,否则返回false.
         3)put(anObject):把anObject加到BlockingQueue里,如果BlockQueue没有空间,则调用此方法的线程被阻断直到BlockingQueue里面有空间再继续.
         4)poll(time):取走BlockingQueue里排在首位的对象,若不能立即取出,则可以等time参数规定的时间,取不到时返回null
         5)take():取走BlockingQueue里排在首位的对象,若BlockingQueue为空,阻断进入等待状态直到Blocking有新的对象被加入为止
         2.BlockingQueue有四个具体的实现类,根据不同需求,选择不同的实现类
         1)ArrayBlockingQueue:规定大小的BlockingQueue,其构造函数必须带一个int参数来指明其大小.其所含的对象是以FIFO(先入先出)顺序排序的.
         2)LinkedBlockingQueue:大小不定的BlockingQueue,若其构造函数带一个规定大小的参数,生成的BlockingQueue有大小限制,若不带大小参数,所生成的BlockingQueue的大小由Integer.MAX_VALUE来决定.其所含的对象是以FIFO(先入先出)顺序排序的
         3)PriorityBlockingQueue:类似于LinkedBlockQueue,但其所含对象的排序不是FIFO,而是依据对象的自然排序顺序或者是构造函数的Comparator决定的顺序.
         4)SynchronousQueue:特殊的BlockingQueue,对其的操作必须是放和取交替完成的.
         3.LinkedBlockingQueue和ArrayBlockingQueue比较起来,它们背后所用的数据结构不一样,导致LinkedBlockingQueue的数据吞吐量要大于ArrayBlockingQueue,但在线程数量很大时其性能的可预见性低于ArrayBlockingQueue.
         */
        BlockingQueue<Map<String, Object>> queue = new ArrayBlockingQueue<>(rowCount);
        //BlockingQueue<Map<String, Object>> queue = new LinkedBlockingQueue<>();
        //BlockingQueue<Map<String, Object>> queue = new PriorityBlockingQueue<>();
        //BlockingQueue<Map<String, Object>> queue = new SynchronousQueue<>();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            Object[] rsStr = new Object[colCount];
            for (int i = 0; i < colCount; i++) {
                // 获得指定列的列名
                String columnName = resultSetMetaData.getColumnName(i + 1);
                if (columnName.lastIndexOf(".") > -1) {
                    columnName = columnName.substring(columnName.lastIndexOf(".") + 1, columnName.length());
                }
                rsStr[i] = resultSet.getObject(i + 1);
                map.put(columnName, rsStr[i]);
            }
            //数据放入BlockingQueue队列
            queue.add(map);
        }
        long addListTime = System.currentTimeMillis();
        //debug(randomNo + "addList use time: " + (addListTime - endTime)+ "ms");
        debug(randomNo + "加载数据 use time: " + TimeUtil.formatTime(addListTime - endTime));
        debug(randomNo + "search row number: " + queue.size());
        // System.out.println("list:"+list.size());
        // System.out.println("cols:"+cols);
        resultSet.close();
        preparedStatement.close();
        DBConnectionManager.getInstance().freeConnection(dbName, connection);
        return queue;
    }

    /**
     * 把要查询的SQL结果集返回到List<Map < String, Object>>
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> searchSQL(String dbName, String sql) throws SQLException {
        List<Map<String, Object>> searchSQL = searchSQL(dbName, sql, null);
        //for (int i = 0; i < searchSQL.size(); i++) {
        //	System.out.println("===" + searchSQL.get(i).get("colName"));
        //}
        return searchSQL;
    }

    /**
     * 把要查询的SQL结果集返回到List<Map < String, Object>>
     *
     * @param searchSQL
     * @param dbName
     * @param param
     * @return
     * @throws SQLException
     */
    protected static List<Map<String, Object>> searchSQL(String dbName, String searchSQL, Object... param) throws SQLException {
        isDebug = true;
        Connection connection = DBConnectionManager.getInstance().getConnection(dbName);
        String randomNo = getRandomNo();
        long startTime = System.currentTimeMillis();
        PreparedStatement preparedStatement = connection.prepareStatement(searchSQL);

        connection = connection;
        preparedStatement = preparedStatement;

        //给参数赋值
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                preparedStatement.setObject(i, param[i]);
            }
        }
        debug(randomNo + "Running:" + searchSQL);
        //ResultSet resultSet = preparedStatement.executeQuery(searchSQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        long endTime = System.currentTimeMillis();
        //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
        debug(randomNo + "查询数据 use time: " + TimeUtil.formatTime(endTime - startTime));
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int colCount = resultSetMetaData.getColumnCount();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            Object[] rsStr = new Object[colCount];
            for (int i = 0; i < colCount; i++) {
                // 获得指定列的列名
                String columnName = resultSetMetaData.getColumnName(i + 1);
                if (columnName.lastIndexOf(".") > -1) {
                    columnName = columnName.substring(columnName.lastIndexOf(".") + 1, columnName.length());
                }
                rsStr[i] = resultSet.getObject(i + 1);
                map.put(columnName, rsStr[i]);
            }
            list.add(map);
        }
        long addListTime = System.currentTimeMillis();
        //debug(randomNo + "addList use time: " + (addListTime - endTime)+ "ms");
        debug(randomNo + "加载数据 use time: " + TimeUtil.formatTime(addListTime - endTime));
        debug(randomNo + "search row number: " + list.size());
        // System.out.println("list:"+list.size());
        // System.out.println("cols:"+cols);
        resultSet.close();
        preparedStatement.close();
        DBConnectionManager.getInstance().freeConnection(dbName, connection);
        return list;
    }

    /**
     * 根据connection和查询SQL把结果集返回到List<Map < String, Object>>
     *
     * @param connection
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List<Map<String, Object>> searchSQL(Connection connection, String sql) throws SQLException {
        List<Map<String, Object>> searchSQL = searchSQL(connection, sql, null);
		/*for (int i = 0; i < searchSQL.size(); i++) {
			System.out.println("==="+searchSQL.get(i).get("colName"));
		}*/
        return searchSQL;
    }

    /**
     * 根据connection和查询SQL把结果集返回到List<Map < String, Object>>
     *
     * @param connection
     * @param searchSQL
     * @param param
     * @return
     * @throws SQLException
     */
    protected static List<Map<String, Object>> searchSQL(Connection connection, String searchSQL, Object... param) throws SQLException {
        isDebug = true;
        String randomNo = getRandomNo();
        long startTime = System.currentTimeMillis();
        PreparedStatement preparedStatement = connection.prepareStatement(searchSQL);

        connection = connection;
        preparedStatement = preparedStatement;

        //给参数赋值
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                preparedStatement.setObject(i, param[i]);
            }
        }
        debug(randomNo + "Running:" + searchSQL);
        //ResultSet resultSet = preparedStatement.executeQuery(searchSQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        long endTime = System.currentTimeMillis();
        //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
        debug(randomNo + "查询数据 use time: " + TimeUtil.formatTime(endTime - startTime));
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int colCount = resultSetMetaData.getColumnCount();

        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            Object[] rsStr = new Object[colCount];
            for (int i = 0; i < colCount; i++) {
                // 获得指定列的列名
                String columnName = resultSetMetaData.getColumnName(i + 1);
                if (columnName.lastIndexOf(".") > -1) {
                    columnName = columnName.substring(columnName.lastIndexOf(".") + 1, columnName.length());
                }
                rsStr[i] = resultSet.getObject(i + 1);
                map.put(columnName, rsStr[i]);
            }
            list.add(map);
        }
        long addListTime = System.currentTimeMillis();
        //debug(randomNo + "addList use time: " + (addListTime - endTime)+ "ms");
        debug(randomNo + "加载数据 use time: " + TimeUtil.formatTime(addListTime - endTime));
        debug(randomNo + "search row number: " + list.size());
        // System.out.println("list:"+list.size());
        // System.out.println("cols:"+cols);
        resultSet.close();
        preparedStatement.close();
        return list;
    }

    /**
     * @param sql
     * @return Table
     * @throws SQLException
     * @preserve
     * @author Chen Pei
     * @Date: 2016-05-18
     */
    public static Table searchTable(String dbName, String sql) throws SQLException {
        isDebug = true;
        Connection connection = DBConnectionManager.getInstance().getConnection(dbName);
        String randomNo = getRandomNo();
        long startTime = System.currentTimeMillis();
        PreparedStatement preparedStatement = connection.prepareStatement(sql);
        // ResultSet resultSet = preparedStatement.executeQuery(searchSQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        long endTime = System.currentTimeMillis();
        //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
        debug(randomNo + "查询数据 use time: " + TimeUtil.formatTime(endTime - startTime));
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int colCount = resultSetMetaData.getColumnCount();

        //List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Vector vector = new Vector();
        while (resultSet.next()) {
            //Map<String, Object> map = new HashMap<String, Object>();
            Vector vector1 = new Vector();
            Object[] rsStr = new Object[colCount];
            for (int i = 0; i < colCount; i++) {
                // 获得指定列的列名
                String columnName = resultSetMetaData.getColumnName(i + 1);
                rsStr[i] = resultSet.getObject(i + 1);
                //map.put(columnName, rsStr[i]);
                vector1.addElement(rsStr[i]);
            }
            vector.add(vector1);
            //list.add(map);
        }
        long addListTime = System.currentTimeMillis();
        //debug(randomNo + "addList use time: " + (addListTime - endTime)+ "ms");
        debug(randomNo + "加载数据 use time: " + TimeUtil.formatTime(addListTime - endTime));
        debug(randomNo + "search row number: " + vector.size());
        // System.out.println("list:"+list.size());
        // System.out.println("cols:"+cols);
        resultSet.close();
        preparedStatement.close();
        DBConnectionManager.getInstance().freeConnection(dbName, connection);
        Table table = new Table(vector, resultSetMetaData);
        return table;
    }

    /**
     * 把要查询的SQL结果集返回到List<Object [ ]>
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List<Object[]> search(String dbName, String sql) throws SQLException {
        List<Object[]> list = search(dbName, sql, null);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i)[0].toString());
        }

        return list;
    }

    /**
     * 把要查询的SQL结果集返回到List<Object [ ]>
     *
     * @param searchSQL
     * @param dbName
     * @param param
     * @return
     * @throws SQLException
     */
    protected static List<Object[]> search(String dbName, String searchSQL, Object... param) throws SQLException {
        isDebug = true;
        Connection connection = DBConnectionManager.getInstance().getConnection(dbName);
        String randomNo = getRandomNo();
        long startTime = System.currentTimeMillis();
        PreparedStatement preparedStatement = connection.prepareStatement(searchSQL);

        connection = connection;
        preparedStatement = preparedStatement;

        //给参数赋值
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                preparedStatement.setObject(i, param[i]);
            }
        }
        debug(randomNo + "Running:" + searchSQL);
        //ResultSet resultSet = preparedStatement.executeQuery(searchSQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        long endTime = System.currentTimeMillis();
        //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
        debug(randomNo + "查询数据 use time: " + TimeUtil.formatTime(endTime - startTime));
        ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
        int colCount = resultSetMetaData.getColumnCount();

        List<Object[]> list = new ArrayList<Object[]>();
        while (resultSet.next()) {
            Object[] rsStr = new Object[colCount];
            for (int i = 0; i < colCount; i++) {
                rsStr[i] = resultSet.getObject(i + 1);
            }
            list.add(rsStr);
        }
        long addListTime = System.currentTimeMillis();
        //debug(randomNo + "addList use time: " + (addListTime - endTime)+ "ms");
        debug(randomNo + "加载数据 use time: " + TimeUtil.formatTime(addListTime - endTime));
        debug(randomNo + "search row number: " + list.size());
        // System.out.println("list:"+list.size());
        // System.out.println("cols:"+cols);
        resultSet.close();
        preparedStatement.close();
        DBConnectionManager.getInstance().freeConnection(dbName, connection);
        return list;
    }

    /**
     * 根据connection和查询SQL把结果集返回到List<Object [ ]>
     *
     * @param connection
     * @param sql
     * @return
     */
    public static List<Object[]> search(Connection connection, String sql) {
        List<Object[]> search = search(connection, sql, null);
		/*for (int i = 0; i < search.size(); i++) {
			System.out.println( search.get(i)[0].toString());
		}*/

        return search;
    }

    /**
     * 根据connection和查询SQL把结果集返回到List<Object [ ]>
     *
     * @param connection
     * @param searchSQL
     * @param param
     * @return
     */
    protected static List<Object[]> search(Connection connection, String searchSQL, Object... param) {
        isDebug = true;
        String randomNo = getRandomNo();
        long startTime = System.currentTimeMillis();
        List<Object[]> list = new ArrayList<Object[]>();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(searchSQL);

            connection = connection;
            preparedStatement = preparedStatement;

            //给参数赋值
            if (param != null && param.length > 0) {
                for (int i = 0; i < param.length; i++) {
                    preparedStatement.setObject(i, param[i]);
                }
            }
            debug(randomNo + "Running:" + searchSQL);
            //ResultSet resultSet = preparedStatement.executeQuery(searchSQL);
            ResultSet resultSet = preparedStatement.executeQuery();
            long endTime = System.currentTimeMillis();
            //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
            debug(randomNo + "查询数据 use time: " + TimeUtil.formatTime(endTime - startTime));
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int cols = resultSetMetaData.getColumnCount();
            while (resultSet.next()) {
                Object[] rsStr = new Object[cols];
                for (int i = 0; i < cols; i++) {
                    rsStr[i] = resultSet.getObject(i + 1);
                }
                list.add(rsStr);
            }
            long addListTime = System.currentTimeMillis();
            //debug(randomNo + "addList use time: " + (addListTime - endTime)+ "ms");
            debug(randomNo + "加载数据 use time: " + TimeUtil.formatTime(addListTime - endTime));
            debug(randomNo + "search row number: " + list.size());
            //System.out.println("list:"+list.size());
            //System.out.println("cols:"+cols);
            resultSet.close();
            preparedStatement.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return list;
    }

    /**
     * 根据要查询的SQL返回一个ResultSet结果集
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static ResultSet resultSet(String dbName, String sql) throws SQLException {
        ResultSet resultSet = resultSet(dbName, sql, null);
        return resultSet;
    }

    /**
     * 根据要查询的SQL返回一个ResultSet结果集
     *
     * @param searchSQL
     * @param dbName
     * @param param
     * @return
     * @throws SQLException
     */
    protected static ResultSet resultSet(String dbName, String searchSQL, Object... param) throws SQLException {
        isDebug = true;
        Connection connection = DBConnectionManager.getInstance().getConnection(dbName);
        String randomNo = getRandomNo();
        long startTime = System.currentTimeMillis();
        PreparedStatement preparedStatement = connection.prepareStatement(searchSQL);

        connection = connection;
        preparedStatement = preparedStatement;

        //给参数赋值
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                preparedStatement.setObject(i, param[i]);
            }
        }
        debug(randomNo + "Running:" + searchSQL);
        //ResultSet resultSet = preparedStatement.executeQuery(searchSQL);
        ResultSet resultSet = preparedStatement.executeQuery();
        long endTime = System.currentTimeMillis();
        //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
        debug(randomNo + "查询数据 use time: " + TimeUtil.formatTime(endTime - startTime));

        return resultSet;
    }

    /**
     * 根据connection和查询SQL返回一个ResultSet结果集
     *
     * @param connection
     * @param sql
     * @return
     */
    public static ResultSet resultSet(Connection connection, String sql) {
        ResultSet resultSet = resultSet(connection, sql, null);
        return resultSet;
    }

    /**
     * 根据connection和查询SQL返回一个ResultSet结果集
     *
     * @param connection
     * @param searchSQL
     * @param param
     * @return
     */
    protected static ResultSet resultSet(Connection connection, String searchSQL, Object... param) {
        isDebug = true;
        ResultSet resultSet = null;
        String randomNo = getRandomNo();
        long startTime = System.currentTimeMillis();
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(searchSQL);

            connection = connection;
            preparedStatement = preparedStatement;

            //给参数赋值
            if (param != null && param.length > 0) {
                for (int i = 0; i < param.length; i++) {
                    preparedStatement.setObject(i, param[i]);
                }
            }
            debug(randomNo + "Running:" + searchSQL);
            //resultSet = preparedStatement.executeQuery(searchSQL);
            resultSet = preparedStatement.executeQuery();
            long endTime = System.currentTimeMillis();
            //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
            debug(randomNo + "查询数据 use time: " + TimeUtil.formatTime(endTime - startTime));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return resultSet;
    }

    /*******************************************************华丽的分割线_start*******************************************************/

    /**
     * 获取结果集,并将结果放在List中
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List<Object> excuteQuery(String dbName, String sql) throws SQLException {
        List<Object> excuteQuery = excuteQuery(dbName, sql, null);
		/*for (int i = 0; i < excuteQuery.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) excuteQuery.get(i);
			System.out.println(map.get("colName"));
		}*/

        return excuteQuery;
    }

    /**
     * 获取结果集,并将结果放在List中
     *
     * @param sql
     * @param dbName
     * @param params
     * @return
     * @throws SQLException
     */
    protected static List<Object> excuteQuery(String dbName, String sql, Object[] params) throws SQLException {
        isDebug = true;
        // 获得连接
        Connection connection = DBConnectionManager.getInstance().getConnection(dbName);
        String randomNo = getRandomNo();
        long startTime = System.currentTimeMillis();

        // 调用SQL
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        connection = connection;
        preparedStatement = preparedStatement;

        // 参数赋值
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }
        debug(randomNo + "Running:" + sql);
        // 执行SQL获得结果集
        ResultSet resultSet = preparedStatement.executeQuery();

        // 创建ResultSetMetaData对象
        ResultSetMetaData resultSetMetaData = null;

        // 结果集列数
        int columnCount = 0;
        try {
            resultSetMetaData = resultSet.getMetaData();
            // 获得结果集列数
            columnCount = resultSetMetaData.getColumnCount();
        } catch (SQLException e1) {
            System.out.println(e1.getMessage());
        }

        // 创建List
        List<Object> list = new ArrayList<Object>();

        // 将ResultSet的结果保存到List中
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                map.put(resultSetMetaData.getColumnLabel(i), resultSet.getObject(i));
            }
            list.add(map);
        }
        long endTime = System.currentTimeMillis();
        //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
        debug(randomNo + "use time: " + TimeUtil.formatTime(endTime - startTime));
        resultSet.close();
        DBConnectionManager.getInstance().freeConnection(dbName, connection);

        return list;
    }

    /**
     * 根据connection和查询SQL获取结果集, 并将结果放在List中
     *
     * @param connection
     * @param sql
     * @return
     * @throws SQLException
     */
    public static List<Object> excuteQuery(Connection connection, String sql) throws SQLException {
        List<Object> excuteQuery = excuteQuery(connection, sql, null);
		/*for (int i = 0; i < excuteQuery.size(); i++) {
			Map<String, Object> map = (Map<String, Object>) excuteQuery.get(i);
			System.out.println(map.get("colName"));
		}*/
        return excuteQuery;
    }

    /**
     * 根据connection和查询SQL获取结果集, 并将结果放在List中
     *
     * @param connection
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    protected static List<Object> excuteQuery(Connection connection, String sql, Object[] params) throws SQLException {
        isDebug = true;
        String randomNo = getRandomNo();
        long startTime = System.currentTimeMillis();

        // 调用SQL
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        connection = connection;
        preparedStatement = preparedStatement;

        // 参数赋值
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }
        debug(randomNo + "Running:" + sql);
        // 执行SQL获得结果集
        ResultSet resultSet = preparedStatement.executeQuery();

        // 创建ResultSetMetaData对象
        ResultSetMetaData resultSetMetaData = null;

        // 结果集列数
        int columnCount = 0;
        try {
            resultSetMetaData = resultSet.getMetaData();
            // 获得结果集列数
            columnCount = resultSetMetaData.getColumnCount();
        } catch (SQLException e1) {
            System.out.println(e1.getMessage());
        }

        // 创建List
        List<Object> list = new ArrayList<Object>();

        // 将ResultSet的结果保存到List中
        while (resultSet.next()) {
            Map<String, Object> map = new HashMap<String, Object>();
            for (int i = 1; i <= columnCount; i++) {
                map.put(resultSetMetaData.getColumnLabel(i), resultSet.getObject(i));
            }
            list.add(map);
        }
        long endTime = System.currentTimeMillis();
        //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
        debug(randomNo + "use time: " + TimeUtil.formatTime(endTime - startTime));
        resultSet.close();

        return list;
    }

    /*******************************************************华丽的分割线_end*********************************************************/

    /**
     * 执行插入语句操作
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int insert(String dbName, String sql) throws SQLException {
        return executeUpdate(dbName, sql, null);
    }

    /**
     * 执行删除操作
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int delete(String dbName, String sql) throws SQLException {
        return executeUpdate(dbName, sql, null);
    }

    /**
     * 执行更新造作
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int update(String dbName, String sql) throws SQLException {
        return executeUpdate(dbName, sql, null);
    }

    /**
     * @param sql
     * @return
     * @throws SQLException
     * @preserve
     * @author Chen Pei
     * @Date: 2016-05-18
     */
    public static int drop(String dbName, String sql) throws SQLException {
        return executeUpdate(dbName, sql, null);
    }

    /**
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int create(String dbName, String sql) throws SQLException {
        return executeUpdate(dbName, sql, null);
    }

    /**
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int set(String dbName, String sql) throws SQLException {
        return executeUpdate(dbName, sql, null);
    }

    /**
     * insert update delete SQL语句的执行的统一方法
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int executeUpdate(String dbName, String sql) throws SQLException {
        return executeUpdate(dbName, sql, null);
    }

    /**
     * insert update delete SQL语句的执行的统一方法
     *
     * @param sql
     * @param dbName
     * @param params
     * @return
     * @throws SQLException
     */
    protected static int executeUpdate(String dbName, String sql, Object[] params) throws SQLException {
        isDebug = true;
        // 获得连接
        Connection connection = DBConnectionManager.getInstance().getConnection(dbName);
        String randomNo = getRandomNo();
        long startTime = System.currentTimeMillis();

        // 受影响的行数
        int affectedLine = 0;
        // 调用SQL
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        connection = connection;
        preparedStatement = preparedStatement;

        // 参数赋值
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }
        debug(randomNo + "Running:" + sql);
        // 执行
        affectedLine = preparedStatement.executeUpdate();

        long endTime = System.currentTimeMillis();
        //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
        debug(randomNo + "use time: " + TimeUtil.formatTime(endTime - startTime));
        preparedStatement.close();
        DBConnectionManager.getInstance().freeConnection(dbName, connection);

        return affectedLine;
    }

    /**
     * 执行插入语句操作
     *
     * @param connection
     * @param sql        String 插入语句
     * @return int 操做返回插入成功的数量
     * @throws SQLException
     * @preserve
     * @author Chen Pei
     * @Date: 2016-05-18
     */
    public static int insert(Connection connection, String sql) throws SQLException {
        return executeUpdate(connection, sql, null);
    }

    /**
     * 执行删除操作
     *
     * @param connection
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int delete(Connection connection, String sql) throws SQLException {
        return executeUpdate(connection, sql, null);
    }

    /**
     * 执行更新造作
     *
     * @param connection
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int update(Connection connection, String sql) throws SQLException {
        return executeUpdate(connection, sql, null);
    }

    /**
     * @param connection
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int drop(Connection connection, String sql) throws SQLException {
        return executeUpdate(connection, sql, null);
    }

    /**
     * @param connection
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int create(Connection connection, String sql) throws SQLException {
        return executeUpdate(connection, sql, null);
    }

    /**
     * @param connection
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int set(Connection connection, String sql) throws SQLException {
        return executeUpdate(connection, sql, null);
    }

    /**
     * insert update delete SQL语句的执行的统一方法
     *
     * @param connection
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int executeUpdate(Connection connection, String sql) throws SQLException {
        return executeUpdate(connection, sql, null);
    }

    /**
     * insert update delete SQL语句的执行的统一方法
     *
     * @param connection
     * @param sql
     * @param params
     * @return
     * @throws SQLException
     */
    protected static int executeUpdate(Connection connection, String sql, Object[] params) throws SQLException {
        isDebug = true;
        // 获得连接
        String randomNo = getRandomNo();
        long startTime = System.currentTimeMillis();

        // 受影响的行数
        int affectedLine = 0;
        // 调用SQL
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        connection = connection;
        preparedStatement = preparedStatement;

        // 参数赋值
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                preparedStatement.setObject(i + 1, params[i]);
            }
        }
        debug(randomNo + "Running:" + sql);
        // 执行
        affectedLine = preparedStatement.executeUpdate();

        long endTime = System.currentTimeMillis();
        //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
        debug(randomNo + "use time: " + TimeUtil.formatTime(endTime - startTime));
        preparedStatement.close();

        return affectedLine;
    }

    /**
     * 存储过程带有一个输出参数的方法
     *
     * @param sql
     * @param outParamPos
     * @param sqlType
     * @return
     * @throws SQLException
     */
    public Object excuteQuery(String dbName, String sql, int outParamPos, int sqlType) throws SQLException {
        return excuteQuery(dbName, sql, outParamPos, sqlType, null);
    }

    /**
     * 存储过程带有一个输出参数的方法
     *
     * @param sql
     * @param dbName
     * @param outParamPos
     * @param sqlType
     * @param params
     * @return
     * @throws SQLException
     */
    protected Object excuteQuery(String dbName, String sql, int outParamPos, int sqlType, Object[] params) throws SQLException {
        isDebug = true;
        // 获得连接
        Connection connection = DBConnectionManager.getInstance().getConnection(dbName);
        String randomNo = getRandomNo();
        long startTime = System.currentTimeMillis();

        Object object = null;

        // 调用存储过程
        CallableStatement callableStatement = connection.prepareCall(sql);

        // 给参数赋值
        if (params != null) {
            for (int i = 0; i < params.length; i++) {
                callableStatement.setObject(i + 1, params[i]);
            }
        }
        debug(randomNo + "Running:" + sql);
        // 注册输出参数
        callableStatement.registerOutParameter(outParamPos, sqlType);

        // 执行
        callableStatement.execute();

        // 得到输出参数
        object = callableStatement.getObject(outParamPos);

        long endTime = System.currentTimeMillis();
        //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
        debug(randomNo + "use time: " + TimeUtil.formatTime(endTime - startTime));
        callableStatement.close();
        DBConnectionManager.getInstance().freeConnection(dbName, connection);

        return object;
    }

    /**
     * 添加批处理语句
     *
     * @param connection
     * @param sql
     * @throws SQLException
     * @throws NullConnectionException
     */
    public void addBatch(Connection connection, String sql) throws SQLException, NullConnectionException {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            connection = connection;
            preparedStatement = preparedStatement;

            preparedStatement.addBatch(sql);
            debug("addBatch:" + sql);
        } catch (SQLException se) {
            debug(se);
            //此处先屏蔽
            //release();
            throw se;
        }
    }

    /**
     * PreparedStatement方式执行SQL
     *
     * @param filename
     * @param tableName
     * @return
     * @throws SQLException
     */
    public static int loadFile(String dbName, String filename, String tableName) throws SQLException {
        return loadFile(dbName, filename, tableName, null);
    }

    /**
     * PreparedStatement方式执行SQL
     *
     * @param filename
     * @param tableName
     * @param dbName
     * @param param
     * @return
     */
    protected static int loadFile(String dbName, String filename, String tableName, Object... param) {
        int rs = 0;
        try {
            isDebug = true;
            Connection connection = DBConnectionManager.getInstance().getConnection(dbName);
            String randomNo = getRandomNo();
            long startTime = System.currentTimeMillis();
            String sql = "load data local inpath '" + filename + "' into table " + tableName;

            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            connection = connection;
            preparedStatement = preparedStatement;

            //给参数赋值
            if (param != null && param.length > 0) {
                for (int i = 0; i < param.length; i++) {
                    preparedStatement.setObject(i, param[i]);
                }
            }
            debug(randomNo + "Running:" + sql);
            //ResultSet resultSet = preparedStatement.executeQuery(sql);
            //ResultSet resultSet = preparedStatement.executeQuery();
            //preparedStatement.execute(sql);
            rs = preparedStatement.executeUpdate(sql);
            //System.out.println("====="+rs);
            long endTime = System.currentTimeMillis();
            //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
            debug(randomNo + "load data use time: " + TimeUtil.formatTime(endTime - startTime));
            preparedStatement.close();
            DBConnectionManager.getInstance().freeConnection(dbName, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * PreparedStatement方式执行SQL
     *
     * @param filename
     * @param tableName
     * @param connection
     * @throws SQLException
     */
    public static void loadFile(String filename, String tableName, Connection connection) throws SQLException {
        loadFile(filename, tableName, connection, null);
    }

    /**
     * PreparedStatement方式执行SQL
     *
     * @param filename
     * @param tableName
     * @param connection
     * @param param
     */
    protected static void loadFile(String filename, String tableName, Connection connection, Object... param) {
        try {
            isDebug = true;
            String randomNo = getRandomNo();
            long startTime = System.currentTimeMillis();
            String sql = "load data local inpath '" + filename + "' into table " + tableName;
            PreparedStatement preparedStatement = connection.prepareStatement(sql);

            connection = connection;
            preparedStatement = preparedStatement;

            //给参数赋值
            if (param != null && param.length > 0) {
                for (int i = 0; i < param.length; i++) {
                    preparedStatement.setObject(i, param[i]);
                }
            }
            debug(randomNo + "Running:" + sql);
            //ResultSet resultSet = preparedStatement.executeQuery(sql);
            //ResultSet resultSet = preparedStatement.executeQuery();
            //preparedStatement.execute(sql);
            int rs = preparedStatement.executeUpdate(sql);
            //System.out.println("====="+rs);
            long endTime = System.currentTimeMillis();
            //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
            debug(randomNo + "load data use time: " + TimeUtil.formatTime(endTime - startTime));
            preparedStatement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Statement方式执行SQL
     *
     * @param filename
     * @param tableName
     * @return
     * @throws SQLException
     */
    public static int loadDataFile(String dbName, String filename, String tableName) throws SQLException {
        return loadDataFile(dbName, filename, tableName, null);
    }

    /**
     * Statement方式执行SQL
     *
     * @param filename
     * @param tableName
     * @param dbName
     * @param param
     * @return
     */
    protected static int loadDataFile(String dbName, String filename, String tableName, Object... param) {
        int rs = 0;
        try {
            isDebug = true;
            Connection connection = DBConnectionManager.getInstance().getConnection(dbName);
            String randomNo = getRandomNo();
            long startTime = System.currentTimeMillis();
            String sql = "load data local inpath '" + filename + "' overwrite into table " + tableName;
            Statement statement = connection.createStatement();
            //statement.executeQuery(sql);
            //statement.execute(sql);
            debug(randomNo + "Running:" + sql);
            rs = statement.executeUpdate(sql);
            //System.out.println("====="+rs);
            long endTime = System.currentTimeMillis();
            //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
            debug(randomNo + "load data use time: " + TimeUtil.formatTime(endTime - startTime));
            statement.close();
            DBConnectionManager.getInstance().freeConnection(dbName, connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rs;
    }

    /**
     * Statement方式执行SQL
     *
     * @param filename
     * @param tableName
     * @param connection
     * @throws SQLException
     */
    public static void loadDataFile(String filename, String tableName, Connection connection) throws SQLException {
        loadDataFile(filename, tableName, connection, null);
    }

    /**
     * Statement方式执行SQL
     *
     * @param filename
     * @param tableName
     * @param connection
     * @param param
     */
    protected static void loadDataFile(String filename, String tableName, Connection connection, Object... param) {
        try {
            isDebug = true;
            String randomNo = getRandomNo();
            long startTime = System.currentTimeMillis();
            String sql = "load data local inpath '" + filename + "' overwrite into table " + tableName;
            debug(randomNo + "Running:" + sql);
            Statement statement = connection.createStatement();
            //statement.executeQuery(sql);
            //statement.execute(sql);
            int rs = statement.executeUpdate(sql);
            //System.out.println("====="+rs);
            long endTime = System.currentTimeMillis();
            //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
            debug(randomNo + "load data use time: " + TimeUtil.formatTime(endTime - startTime));
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 两种不同连接执行SQL获取结果集对比
     *
     * @param connection
     */
    private static void runAll(Connection connection) {
        //查询一
        try {
            String sql = "select * from table_name ";
            System.out.println("Running:" + sql);
            Statement statement = connection.createStatement();
			/*boolean resHivePropertyTest = statement.execute("set hive.execution.engine=tez");
		    System.out.println(resHivePropertyTest);*/
            ResultSet resultSet = statement.executeQuery(sql);
            ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
            int colCount = resultSetMetaData.getColumnCount();
            System.out.println("=====" + colCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        //查询二
        try {
            String str = "select * from table_name ";
            System.out.println("Running:" + str);
            PreparedStatement preparedStatement = connection.prepareStatement(str);
            ResultSet res = preparedStatement.executeQuery(str);
            ResultSetMetaData resultSetMetaData = res.getMetaData();
            int columnCount = resultSetMetaData.getColumnCount();
            System.out.println("=====" + columnCount);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * 通过该方法获取结果集的行数
     *
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int getRowCount(String dbName, String sql) throws SQLException {
        int rowCount = getRowCount(dbName, sql, null, null);
        return rowCount;
    }

    /**
     * 通过该方法获取结果集的行数
     *
     * @param sql
     * @param dbName
     * @param randomNo
     * @param param
     * @return
     * @throws SQLException
     */
    protected static int getRowCount(String dbName, String sql, String randomNo, Object... param) throws SQLException {
        isDebug = true;
        Connection connection = DBConnectionManager.getInstance().getConnection(dbName);
        if (randomNo == null || randomNo.trim().equals("")) {
            randomNo = getRandomNo();
        }
        long startTime = System.currentTimeMillis();
        int rowCount = 0;
        sql = "select count(*) record_ from ( " + sql + " )t";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        connection = connection;
        preparedStatement = preparedStatement;

        //给参数赋值
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                preparedStatement.setObject(i, param[i]);
            }
        }
        debug(randomNo + "Running:" + sql);
        ResultSet resultSet = preparedStatement.executeQuery(sql);
        if (resultSet.next()) {
            rowCount = resultSet.getInt("record_");
        }
        long endTime = System.currentTimeMillis();
        //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
        debug(randomNo + "getRowCount use time: " + TimeUtil.formatTime(endTime - startTime));
        resultSet.close();
        preparedStatement.close();
        DBConnectionManager.getInstance().freeConnection(dbName, connection);
        return rowCount;
    }

    /**
     * 通过该方法获取结果集的行数
     *
     * @param connection
     * @param sql
     * @return
     * @throws SQLException
     */
    public static int getRowCount(Connection connection, String sql) throws SQLException {
        int rowCount = getRowCount(connection, sql, null, null);
        return rowCount;
    }

    /**
     * 通过该方法获取结果集的行数
     *
     * @param connection
     * @param sql
     * @param randomNo
     * @param param
     * @return
     * @throws SQLException
     */
    protected static int getRowCount(Connection connection, String sql, String randomNo, Object... param) throws SQLException {
        isDebug = true;
        if (randomNo == null || randomNo.trim().equals("")) {
            randomNo = getRandomNo();
        }
        long startTime = System.currentTimeMillis();
        int rowCount = 0;
        sql = "select count(*) record_ from ( " + sql + " )t";

        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        connection = connection;
        preparedStatement = preparedStatement;

        //给参数赋值
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                preparedStatement.setObject(i, param[i]);
            }
        }
        debug(randomNo + "Running:" + sql);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            rowCount = resultSet.getInt("record_");
        }
        long endTime = System.currentTimeMillis();
        //debug(randomNo + "use time: " + (endTime - startTime)+ "ms");
        debug(randomNo + "getRowCount use time: " + TimeUtil.formatTime(endTime - startTime));
        resultSet.close();
        preparedStatement.close();
        return rowCount;
    }

    /**
     * 通过该方法获取结果集的行数
     *
     * @param resultSet
     * @return
     * @throws Exception
     */
    protected static int getRowCount(ResultSet resultSet) throws Exception {
        int result = 0;
        //移到最后一行
        if (resultSet.last()) {
            //得到当前行号,也就是记录数
            result = resultSet.getRow();
            //光标回滚
            resultSet.beforeFirst();
        }
        return result;
    }

    /**
     * 通过该方法获取结果集的行数
     *
     * @param resultSet
     * @return
     * @throws SQLException
     */
    protected static int getRowNum(ResultSet resultSet) throws SQLException {
        int rowCount = 0;
        while (resultSet.next()) {
            rowCount++;
        }
        return rowCount;
    }

    protected static String getRandomNo() {
        String str = "SQL " + Math.abs(random.nextInt()) + ": ";
        return str;
    }

    protected static String getRandomNum() {
        return "SQL " + String.valueOf((int) (100000 * Math.random())) + ": ";
    }

    /**
     * @param object
     */
    protected static void debug(Object object) {
        if (isDebug) {
            if ((object instanceof Throwable))
                ((Throwable) object).printStackTrace();
            else {
                System.out.println(object);
            }
        }
        log(object);
    }

    /**
     * @param object
     */
    protected static void log(Object object) {
        if ((isLog) && (logPrint != null)) {
            logPrint.println(SimpleDateFormat.getDateTimeInstance().format(new Date()) + ": " + object);
            if ((object instanceof Throwable)) {
                ((Throwable) object).printStackTrace(logPrint);
            }
            logPrint.flush();
        }
    }

    /**
     * 判断数据库中是否存在某张表
     *
     * @param tableName
     * @return
     * @throws SQLException
     */
    public static boolean isTableExist(String dbName, String tableName) throws SQLException {
        boolean flag = false;
        return isTableExist(dbName, tableName, null);
    }

    /**
     * 判断数据库中是否存在某张表
     *
     * @param tableName
     * @param dbName
     * @param param
     * @return
     */
    protected static boolean isTableExist(String dbName, String tableName, Object... param) {
        Connection connection = DBConnectionManager.getInstance().getConnection(dbName);
        boolean flag = false;
        try {
            DatabaseMetaData meta = connection.getMetaData();
            String[] type = {"TABLE"};
            ResultSet resultSet = meta.getTables(null, null, tableName, type);
            flag = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 判断数据库中是否存在某张表
     *
     * @param connection
     * @param tableName
     * @return
     * @throws SQLException
     */
    public static boolean isTableExist(Connection connection, String tableName) throws SQLException {
        boolean flag = false;
        return isTableExist(connection, tableName, null);
    }

    /**
     * 判断数据库中是否存在某张
     *
     * @param connection
     * @param tableName
     * @param param
     * @return
     */
    protected static boolean isTableExist(Connection connection, String tableName, Object... param) {
        boolean flag = false;
        try {
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String[] type = {"TABLE"};
            ResultSet resultSet = databaseMetaData.getTables(null, null, tableName, type);
            flag = resultSet.next();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return flag;
    }
}