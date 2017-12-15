package com.techD;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DBUser {
	
 public String JDBC_DRIVER;
 public String DB_URL;
 public String SchemaName;
 public String SchemaPassword;
 public  Connection conn;
 public Statement stmt;
 
	
public 	DBUser(String jDriver,String dbUrl,String user,String pass){
		this.JDBC_DRIVER=jDriver;
		this.DB_URL=dbUrl;
		this.SchemaName=user;
		this.SchemaPassword=pass;
		
			try {
				Class.forName(JDBC_DRIVER);
				System.out.println("Connection to database");
				try {
					conn = DriverManager.getConnection(DB_URL,SchemaName,SchemaPassword);
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		
	}
	
	
	public List<DBUserInfo> getAllUsers(String tableName) throws SQLException {
		
		stmt = conn.createStatement();
		 ResultSet rs = stmt.executeQuery("select * from "+tableName);
		 List<DBUserInfo> userList= new ArrayList<DBUserInfo>();
		 
		 
		 while(rs.next()){
			 DBUserInfo userInfo= new DBUserInfo();
			 
			 userInfo.setFirstName(rs.getString("FIRSTNAME"));
			userInfo.setLastName(rs.getString("LASTNAME"));
	         userInfo.setMiddleName(rs.getString("MIDDLENAME"));
	        userInfo.setLoginid(rs.getString("LOGINID"));
	       userInfo.setPassword(rs.getString("PASSWORD"));
	        userInfo.setMail(rs.getString("MAIL"));
	         userInfo.setPhone(rs.getString("PHONE"));
	         userList.add(userInfo);
	     
	         
		 }
		 System.out.println(userList);
		
		
		return userList;
	}

	
	
	public static void main(String[] args) throws SQLException {
		
		DBUser dbUser= new DBUser("oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@localhost:1521:XE","sys as sysdba","Passw0rd");
	    dbUser.getAllUsers("TRUSTEDDBUSER");
	}
}
