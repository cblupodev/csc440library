import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.util.*;

class EnterHere {
	
	private Connection con = null;
	private DBInteraction db = null;
	Scanner console = new Scanner(System.in);
	private Integer currentID;
	
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
    
    private void displayLogin() {
    	// print beginning header
    	sop("How to interact --> enter the adjacent numbers to go to the corresponding pages");
    	sop("                    hit ctrl+c to exit");
    	sop("");
    	sop("Login as (1) Student OR (2) Faculty");
    	int res = console.nextInt();
    	if (res == 1 || res == 2) {
    		// enter username and password
    		sop("enter username   ");
    		String username = console.next();
    		sop("enter password");
    		String password = console.next();
    		int log = db.isUser(username, password);
    		if (log != -1) {
    			// login succesfull
    			currentID = log;
    			displayHomepage();	
    		} else {
    			sop("login failed");
    			displayLogin();
    		}
    	} else if (res == 0) {
    		sop("Exited");
    		System.exit(0);
    	} else {
    		sop("login failed, probably didn't enter 1 or 2");
    		displayLogin();
    	}
    }
    
    private void displayHomepage() {
    	sop("Go to:\n");
    	sop(
    		"(1) Profile\n"+
    		"(2) Resources\n"+
    		"(3) Checked-out Resources\n"+
    		"(4) Resource Requests\n"+
    		"(5) Notifications\n"+
    		"(6) Due-Balance\n"
    	);
    	switch(console.nextInt()) {
    		case 1:
    			displayProfile();
    			break;
    		case 2:
    			displayResources();
    			break;
			case 3:
				displayCheckedOutResources();
				break;
			case 4:
				displayResourceRequests();
				break;
			case 5:
				displayNotifications();
				break;
			case 6:
				displayDueBalance();
				break;
			default:
				sop("failed. enter number 1 - 6");
				displayHomepage();
    	}
    }
    
    private void displayProfile() {
    	db.printProfile();
    	sop("(1) Go back (2) update profile");
    	int resu = console.nextInt();
    	sop("resu   "+resu);
    	if (resu == 1) {
    		displayHomepage();
    	} else if (resu == 2) {
    		sop("Enter attribute to update");
    		String attribute = console.next();
    		sop("Enter value");
    		String val = console.next();
    		db.updateProfile(currentID, attribute, val);
    	} else {
    		sop("failed");
    	}
		displayProfile();
    }
    
    private void displayResources() {
    	
    }
    
    private void displayCheckedOutResources() {
    	
    }
    
    private void displayResourceRequests() {
    	
    }
    
    private void displayNotifications() {
    	
    }
    
    private void displayDueBalance() {
    	
    }
    
    public void run() {
    	// connect to the databse
    	connectDatabase("pscarlso", "001063754");
    	
    	// cleaer the tables and fill in data
    	// DBBuilder builder = new DBBuilder(con);
    	// builder.deleteTables();
    	// builder.createTables();
    	// builder.fillTables();
    	
    	db = new DBInteraction(con);
    	
    	// TODO start user interface interaction
    	displayLogin();
    }
    
    // Have to make this dumb thing because System.out.println takes too long to type
    private void sop(Object obj) {
    	System.out.println(obj);
    }
}