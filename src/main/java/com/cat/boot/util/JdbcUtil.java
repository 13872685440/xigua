package com.cat.boot.util;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

public class JdbcUtil {

	private static String className;
	private static String url;
	private static String user;
	private static String password;

	static {

		try {
			InputStream in = JdbcUtil.class.getClassLoader().getResourceAsStream("sqlserver.properties");

			Properties props = new Properties();
			props.load(in);

			className = props.getProperty("className");

			url = props.getProperty("url");

			user = props.getProperty("username");

			password = props.getProperty("password");

			// 注册驱动
			Class.forName(className);

		} catch (Exception e) {

			e.printStackTrace();
		}

	}

	public static Connection getConnection() throws Exception {
		return DriverManager.getConnection(url, user, password);

	}

	public static void close(ResultSet rs, Statement stmt, Connection con) {

		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			rs = null;
		}

		if (stmt != null) {
			try {
				stmt.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			stmt = null;
		}

		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			con = null;
		}
	}

}