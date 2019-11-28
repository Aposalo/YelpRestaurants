package jdbc;

import java.sql.*;

public class Driver {
	
	private Connection myConn = null;
	private Statement myStmt = null;
	private ResultSet myRs = null;
	
	public ResultSet run(String command) throws SQLException {
		
		try {
			// 1. Get a connection to database
			myConn = DriverManager.getConnection("jdbc:mysql://localhost:3306/yelp_db?useSSL=false","root","Galactus_234");
			// 2. Create a statement
			myStmt = myConn.createStatement();
			// 3. Execute SQL query
			myRs = myStmt.executeQuery(command);
		}
		catch (Exception exc) {
			exc.printStackTrace();
		}
		return myRs;
	}
	
	public void close()  throws SQLException {
		if (myRs != null) {
			myRs.close();
		}
		
		if (myStmt != null) {
			myStmt.close();
		}
		
		if (myConn != null) {
			myConn.close();
		}
	}
}