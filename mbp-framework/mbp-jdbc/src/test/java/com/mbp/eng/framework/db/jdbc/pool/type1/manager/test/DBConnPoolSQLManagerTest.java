package com.mbp.eng.framework.db.jdbc.pool.type1.manager.test;

import com.mbp.eng.framework.db.jdbc.pool.type1.manager.DBConnPoolSQLManager;
import com.mbp.eng.framework.test.model.DemoInfo;
import com.mbp.eng.framework.common.util.json.JsonUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class DBConnPoolSQLManagerTest {

    private static Logger logger = LoggerFactory.getLogger(DBConnPoolSQLManagerTest.class);

    private static DBConnPoolSQLManager dbConnPoolSQLManager = new DBConnPoolSQLManager();

    public static void main(String[] args) throws IllegalAccessException, SQLException, InstantiationException {
        query();
        searchMySQL();
        search();
        //searchClickhouse();
    }

    public static void query() throws SQLException {
        String dbName = "MySQL";
        String querySQL = "select * from scheduler_task_info limit 2 ";
        List list = dbConnPoolSQLManager.query(dbName, querySQL);
        //System.out.println(String.format("DBConnPoolSQLManagerTest.query list:%s", JsonUtil.toJSON(list)));
        for (int i = 0; i < list.size(); i++) {
            System.out.println(String.format("DBConnPoolSQLManagerTest.query {%s}", list.get(i).toString()));
            //logger.info("DBConnPoolSQLManagerTest.query {}", JsonUtil.toJSON(list.get(i).toString()));
        }
    }

    public static List<Map<String, Object>> searchMySQL() throws SQLException {
        String dbName = "MySQL";
        String sqlStr = "select * from scheduler_task_info limit 2 ";
        List<Map<String, Object>> listMySQL = dbConnPoolSQLManager.searchSQL(dbName, sqlStr);
        for (int i = 0; i < listMySQL.size(); i++) {
            String audienceId = listMySQL.get(i).get("audienceId").toString();
            System.out.println(String.format("DBConnPoolSQLManagerTest.searchMySQL audienceId:{%s}", audienceId));
            //logger.info("DBConnPoolSQLManagerTest.searchSQLMySQL audienceId:{}", JsonUtil.toJSON(audienceId));
        }
        return listMySQL;
    }

    public static void search() throws SQLException, InstantiationException, IllegalAccessException {
        String dbName = "MySQL";
        String querySQL = "select * from scheduler_task_info limit 2 ";
        List<DemoInfo> list = dbConnPoolSQLManager.search(dbName, querySQL, DemoInfo.class);
        //System.out.println(String.format("DBConnPoolSQLManagerTest.search list:%s", JsonUtil.toJSON(list)));
        for (DemoInfo demoInfo : list) {
            System.out.println(String.format("DBConnPoolSQLManagerTest.search {%s}", JsonUtil.toJSON(demoInfo)));
            //logger.info("DBConnPoolSQLManagerTest.search {}", JsonUtil.toJSON(demoInfo));
        }
    }

    public static List<Map<String, Object>> searchClickhouse() throws SQLException {
        String dbName = "Clickhouse";
        String sqlStr = "SELECT tuid from dl_biz.dmp_user_collect limit 1 ";
        List<Map<String, Object>> listClickhouse = dbConnPoolSQLManager.searchSQL(dbName, sqlStr);
        for (int i = 0; i < listClickhouse.size(); i++) {
            String tuid = listClickhouse.get(i).get("tuid").toString();
            System.out.println(String.format("DBConnPoolSQLManagerTest.searchClickhouse tuid:{%s}", tuid));
            //logger.info("DBConnPoolSQLManagerTest.searchSQLClickhouse tuid:{}", JsonUtil.toJSON(tuid));
        }
        return listClickhouse;
    }

}
