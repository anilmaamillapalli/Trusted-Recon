
import java.sql.*;

public class CreateUser {
	
	
		//JDBC dirver name and database URL
		static String JDBC_DRIVER;
		static String DB_URL;
		static String USER;
		static String PASS;
		static Connection conn;

		CreateUser(){};
		public CreateUser(String jDriver,String dbUrl,String user,String pass){

				this.JDBC_DRIVER=jDriver;
				this.DB_URL=dbUrl;
				this.USER=user;
				this.PASS=pass;
				try{
					Class.forName(JDBC_DRIVER);
					System.out.println("Connection to database");
					conn = DriverManager.getConnection(DB_URL,USER,PASS);
				}catch(Exception e){e.printStackTrace();}
		}

		public String createUser(String fn,String ln,String lid,String pass,String pno,String mail){
		
			try{
				String sql="INSERT INTO userlogintable VALUES(?,?,?,?,?,?,?)";
				PreparedStatement ps=conn.prepareStatement(sql);
				ps.setString(1,fn);
				ps.setString(2,ln);
				ps.setString(3,lid);
				ps.setString(4,pass);
				ps.setString(5,pno);
				ps.setString(6,mail);
				ps.setString(7,"enable");
				ps.executeUpdate();
				conn.commit();
				conn.close();

			}catch(Exception e){e.printStackTrace();}

			System.out.println("Inserted records into the table. . . ");
			
			return "SUCCESS";
		}
	/*	public static void main(String[] args){

		CreateUser cusr=new CreateUser("oracle.jdbc.driver.OracleDriver","jdbc:oracle:thin:@localhost:1521:orcl","sys as sysdba","Passw0rd");
			
		String s=cusr.createUser("user1","kumar","user1","Passw0rd","1234","user1@gmail.com");
		System.out.println(s);
		}
*/
	};


