package com.github.lantoine.lamsadetools.conferences.database;

import java.sql.Connection;
import java.sql.SQLException;

import org.h2.jdbcx.JdbcConnectionPool;

/**
 * ConnectionDataBase classe avoid to repeat code for connected in the data base
 * in each function which need to make a query Moreover, thats allow to keep the
 * Conferences classe maintainable
 *
 * @author Kevin
 *
 */
public class ConnectionDataBase {

	private static Connection conn;
	private static JdbcConnectionPool cp;
	private static String password = "sa";
	private static String url = "jdbc:h2:~/conferences_database";
	private static String username = "sa";

	public static Connection getConn() {
		return conn;
	}

	public static JdbcConnectionPool getCp() {
		return cp;
	}

	public static String getPassword() {
		return password;
	}

	public static String getUrl() {
		return url;
	}

	public static String getUsername() {
		return username;
	}

	public static void setConn(Connection conn) {
		ConnectionDataBase.conn = conn;
	}

	public static void setCp(JdbcConnectionPool cp) {
		ConnectionDataBase.cp = cp;
	}

	public static void setPassword(String password) {
		ConnectionDataBase.password = password;
	}

	public static void setUrl(String url) {
		ConnectionDataBase.url = url;
	}

	public static void setUsername(String username) {
		ConnectionDataBase.username = username;
	}

	/**
	 * closeAndDisposeConnection function close and dispose connection properly
	 *
	 * @throws SQLException
	 */
	public void closeAndDisposeConnection() throws SQLException {

		ConnectionDataBase.getConn().close();
		ConnectionDataBase.getCp().dispose();

	}

	/**
	 * getConnection function set connection with url, username and password and
	 * return Connection object This function is used to connect in the data
	 * base
	 *
	 * @return
	 * @throws SQLException
	 */
	public Connection getConnection() throws SQLException {

		ConnectionDataBase.setCp(JdbcConnectionPool.create(ConnectionDataBase.getUrl(),
				ConnectionDataBase.getUsername(), ConnectionDataBase.getPassword()));

		ConnectionDataBase.setConn(ConnectionDataBase.getCp().getConnection());

		return ConnectionDataBase.getConn();
	}

	/**
	 * sqlQuery function create and execute sql query and take in input a string
	 * which contain sql query
	 *
	 * @param query
	 * @throws SQLException
	 */

	public void sqlQuery(String query) throws SQLException {

		ConnectionDataBase.getConn().createStatement().execute(query);
	}

}
