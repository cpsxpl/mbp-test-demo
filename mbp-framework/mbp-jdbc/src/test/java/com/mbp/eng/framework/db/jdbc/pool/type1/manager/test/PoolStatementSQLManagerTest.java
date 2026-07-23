package com.mbp.eng.framework.db.jdbc.pool.type1.manager.test;

import com.mbp.eng.framework.db.jdbc.pool.type1.manager.PoolStatementSQLManager;
import com.mbp.eng.framework.jdbc.sql.table.Table;

public class PoolStatementSQLManagerTest {
    public static void main(String[] args) throws Exception {
        String dbName = "MySQL";
        String sql1 = "select * from scheduler_task_info";
        Table table = PoolStatementSQLManager.searchTable(dbName, sql1);
        int columnCount = table.getColumnCount();
        System.out.println(String.format("columnCount=%s", columnCount));
        int rowCount = table.getRowCount();
        System.out.println(String.format("rowCount=%s", rowCount));
        for (int i = 0; i < rowCount; i++) {
            System.out.println(String.format("id=%s", table.getCellValue(i, "id", true)));
        }

        /*String sql2 = "select * from scheduler_task_info";
        List<Object[]> locationList = PoolStatementSQLManager.search(dbName, sql2);
        for (int i = 0; i < locationList.size(); i++) {
            String audienceId = locationList.get(i)[0].toString();
            System.out.println("audienceId = '" + audienceId + "'");
        }

        List<Map<String, Object>> locationListMap = PoolStatementSQLManager.searchSQL(dbName, sql2);
        for (int i = 0; i < locationListMap.size(); i++) {
            String audienceId = locationListMap.get(i).get("audienceId").toString();
            System.out.println("audienceId = '" + audienceId + "'");
        }*/
    }
}