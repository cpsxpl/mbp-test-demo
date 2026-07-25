package com.mbp.eng.framework.db.jdbc.pool.type1.test;

import com.mbp.eng.framework.db.jdbc.pool.type1.DBConnectionManager;
import org.json.JSONArray;
import org.json.JSONException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBConnectionManagerTest {

	public static void main(String[] args) throws SQLException, JSONException {
		// 3. 连接池的使用
		// 1。Connection的获得和释放
		// 得到唯一实例
		DBConnectionManager dbConnectionManager = DBConnectionManager.getInstance();
		// 得到连接
		// 从上下文得到你要访问的数据库的名字
		String name = "MySQL";
		Connection connection = dbConnectionManager.getConnection(name);
		// 使用
		String sql = "select * from  audit_detail";
				//+ "and cont_guid = '056ba3f3e7584b67a53eb88eeb41f4ba'";
		PreparedStatement preparedStatement = connection.prepareStatement(sql);
		ResultSet resultSet = preparedStatement.executeQuery(sql);
	
		while (resultSet.next()) {
			String speaker ="";
			String contGuid = resultSet.getString(1);
			//人
			String quotationData = resultSet.getString(2);
			String title = resultSet.getString(3);
			//System.out.println("cont_guid = '"+contGuid+"'");
			
			String contGuidSpeaker = "";
			String titleSpeaker = "";
			String valueSpeaker ="";
			int resultSetSpeakerSize = 0;
			
			//包含speaker
			if (quotationData.indexOf("speaker")>-1) {
				JSONArray jsonArrayQuotationData = new JSONArray(quotationData);
				org.json.JSONObject jsonObj = jsonArrayQuotationData.getJSONObject(0);
				speaker = (String) jsonObj.get("speaker");
				
				String sqlSpeaker = "select distinct cast(cont_guid as string)cont_guid,cast(title as string)title "
						+ "from dim_cont_info_gc "
						+ "where quotation like '%"+speaker+"%' "
						+ "limit 30";
				//System.out.println("RunSQL: "+sqlSpeaker);
				PreparedStatement preparedStatementSpeaker = connection.prepareStatement(sqlSpeaker);
				ResultSet resultSetSpeaker = preparedStatementSpeaker.executeQuery(sqlSpeaker);
				
				while (resultSetSpeaker.next()&& resultSetSpeakerSize<=30) {
					contGuidSpeaker = resultSetSpeaker.getString(1);
					titleSpeaker = resultSetSpeaker.getString(2);
					valueSpeaker += contGuid+"\t" + title+ "\t" + contGuidSpeaker +"\t" + titleSpeaker+ "\r\n";
					resultSetSpeakerSize++;
				}
				System.out.println(valueSpeaker);
			}
		}
		// 使用完毕
		// 释放,但并未断开连接
		dbConnectionManager.freeConnection(name, connection);
		// 2。数据库连接的动态增加和连接池的动态增加
		// 1。调用xml操作增加类

		// 2。重新实例化连接池管理池类
	}

}
