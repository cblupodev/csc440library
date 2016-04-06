import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;

class EnterHere {
	
	private Connection con = null;
	
    public static void main(String[] args) {
        EnterHere enter = new EnterHere();
        enter.run();
    }
    
    
    // connect to the database
    // username doesn't include orcl
    // pscarlso
    // 001063754
    // source https://moodle1516-courses.wolfware.ncsu.edu/mod/forum/discuss.php?d=229176
    private void connectDatabase(String username, String password) {
		System.out.println("-------- Oracle JDBC Connection Testing ------");

		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			System.out.println("Where is your Oracle JDBC Driver?");
			e.printStackTrace();
			return;
		}

		System.out.println("Oracle JDBC Driver Registered!");

		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@orca.csc.ncsu.edu:1521:orcl01", username, password);
		} catch (SQLException e) {
			System.out.println("Connection Failed! Check output console");
			e.printStackTrace();
			return;
		}

		if (con != null) {
			System.out.println("You made it, take control your database now!");
		} else {
			System.out.println("Failed to make connection!");
		}    
    	
    }
    
    // print all the table names
    private void printTableNames() {
    	try {
	    	DatabaseMetaData md = con.getMetaData();
			ResultSet rs1 = md.getTables(null, null, null, null);
			while (rs1.next()) {
	  			String tableName = rs1.getString("TABLE_NAME");
	  			System.out.println(tableName);
			}
    	} catch (SQLException e) { e.printStackTrace(); }
    }
    
    private void login() {
    	
    }
    
    public void run() {
    	connectDatabase("pscarlso", "001063754");
    	
    }
}