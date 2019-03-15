package com.capgemini.connection;

import java.sql.Connection;
import java.sql.DriverManager;

public class ConnectionJDBC {
	
	public static Connection getConnection() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			Connection con=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe","system","Capgemini123");  
			return con;
		}catch(Exception e) {
			System.out.println("Connection Error");
		}
		return null;
	}
}
