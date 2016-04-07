
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.util.*;

class EnterHere {
	
	private Connection con = null;
	private DBInteraction db = null;
	private Scanner console = new Scanner(System.in);
	private String currentID;
	
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
			System.out.println("Where isz your Oracle JDBC Driver?");
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
    		String log = db.isUser(username, password);
    		if (log != "") {
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
    	sop(db.printProfile(currentID)); // print profile info
    	
    	sop("(1) Go back (2) update profile");
    	int res = console.nextInt();
    	if (res == 1) {
    		displayHomepage();
    	} else if (res == 2) {
    		// update an attribute
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
    	sop(
    		"(1) Go back"
    		"(2) Publications\n"+
    		"(3) Conference/Study/Media-production room\n"+
    		"(4) Technology Consultation\n"+
    		"(5) Cameras"
		);
    	switch (console.nextInt()) {
    		case 1:
    			displayHomepage();
    		case 2:
    			displayResourcesPublications();
    			break;
    		case 3:
    			displayResourcesConferenceStudyMediaRooms();
    			break;
    		case 4:
    			displayResourcesTechnologyConsultation();
    			break;
    		case 5:
    			displayResourcesCameras();
    			break;
    	}
    }
    
    private void displayCheckedOutResources() {
    	ArrayList requests = new ArrayList();
    	requests = db.getCheckedOutResources(currentID);
    	

		for(int x = 0; x < requests.size(); x++){
    		sop("(" + x + ") " + requests[x].getName());
    	}
    	sop("(" + requests.size() + ") Go back");
    	
    	int res = console.nextInt();
    	if(res == 0){
    		displayHomepage();
    	} else {
    		resource = db.printResourceDetails(res);
    		sop(resource);
    		if (resource.equals("Inalid ID")) {
    			displayHomepage();
    		} else {
	    		sop("Would you like to renew this resource?\n"+
	    			"(0) No\n"
	    			"(1) Yes"+
	    		);
	    		if(console.nextInt() == 1){
	    			db.renewCheckedOutResource(currentID, requests[res].id);
	    			displayHomepage();
	    		} else {
	    			displayHomepage();
	    		}
    		}
    	}
    }
    
    // The option 'Resource Request' will display the list of resources requested by the user. 
    private void displayResourceRequests() {
    	sop(db.printResourceRequests(currentID));
    	displayHomepage();
    }
    
    
    private void displayNotifications() {
    	sop(db.printNotifications(currentID));
    	displayHomepage();
    }
    
    private void displayDueBalance() {
    	sop(db.printDueBalance(currentID));
    	sop("(1) Go back (2) clear the due balance");
    	int res = console.nextInt();
    	if (res == 2) {
    		db.clearDueBalance(currentID);
    	}
    	displayHomepage();
    }
    
    // On selecting the ‘Publications’ option, the list of all publications
    // (including books, ebooks, journals and conference proceedings) will be displayed. 
    // On further selecting an item from the list displayed, its relevant details will be shown, 
    // along with the option to request/renew the book (if applicable). Any requests which violate 
    // any of the constraints mentioned in the description should not be entertained. 
    // If the student requests for a publication that is not currently available, 
    // the request should be added to a waitlist and an appropriate message should be displayed. 
    // If the requested publication is available, details like checkout date/time, 
    // return date/time should be taken as input (refer to the description).
    // If the request to issue a book is granted (refer to the constraints on various publications), 
    // then the status of the book (issued, waitlisted) should be updated.
    private void displayResourcesPublications() {
		sop(db.listPublications); // print publications list info
		sop("(1) Go back (2) select publication");
		int res = console.nextInt();
		if (res == 1) {
			displayResources();
		} else if (res == 2) {
			// select specific publication
			sop("enter publication id");
			String idres = console.next();
			if (db.hasPublication(idres)) {
				sop(db.printPublicationInfo(idres)); // print publication info
				
				// make the checkout/renew distinction on the database side
				// I'de just be duplicating the same interaction on the ui side
				
				// determine if the publication is checked out by the user
				// if (!db.isPublicationCheckedOutBy(currentID, idres)) {
					// ask to request it
					sop("do you want to request/renew this? 1 OR 0");
					res = console.nextInt();
					if (res = 1) {
						// If the requested publication is available, details like checkout date/time, return date/time should be taken as input 
						sop(
							"enter checkout date, must be in this format YYYY-MON-DD HH24:MI"+
							"example '2003/05/03 21:02:44'"
						);
						String checkoutDate  = console.next();
						sop(
							"enter return date"
						);
						String returnDate  = console.next();
						// checkout the publication
						db.checkoutPublication(currentID, idres, checkoutDate, returnDate);
						// go back to publication dispaly
						displayResourcesPublications();
					} else {
						displayResources();
					}
				// }
				// else {
				// 	// todo maybe distingish renew or a new checkout on the datbase side
				// 	sop("do you want to renew this? 1 OR 0");
				// 	res = console.nextInt();
				// 	if (res = 1) {
				// 		db.checkoutPublication(currentID, idres);
				// 	} else {
				// 		displayResources();
				// 	}
				// }
			} else {
				sop("no publication");
				displayResources();
			}
		} else {
			
		}
    }
	
	/* On selecting the ‘Conference/Study/Media-production room’ option, 
	 the number of occupants and library should be taken as input. 
	 Based on the input the list of relevant study rooms and its details should be displayed. 
	 On further selecting a particular study room, the user should be allowed to book the room 
	 for a particular duration on a particular date. 
	 Follow the rules mentioned in the project description for a valid request. 
	 On trying to book the media production room, the constraints for booking should be followed. 
	 There will be an option to choose the type of instruments required and depending on
	 that a list of available rooms should be displayed and the user should be allow to book the room. 
	 Kindly refer to the specification given in the project description for the booking rules, instruments, etc. */
	private void displayResourcesConferenceStudyMediaRooms() {
		sop("Enter the libray to see rooms for: Hunt OR Hill");
		String library = console.next();
		sop("Enter occupants as an integer");
		int occupants = console.nextInt();
		sop(db.printRooms(library, occupants));
		sop("(1) go back (2) book a room");
		int res = console.nextInt();
		if (res == 1) {
			displayResources();
		} else if (res == 2) {
			sop("Enter room id");
			// don't forget to convert this id into the appropriate int if need be, difference between conference and study/media rooms ids
			String id = console.next();
			String avail = db.roomAvailability(id, isStudent);
			if (isStudent && (avail != "" || avail != "Conference")) {
				// student is allowed to book this room
				sop("enter reservation duration"); // todo figure out what format this is
				String duration = console.next();
				sop("enter reservation date, must be in this format YYYY-MON-DD HH24:MI"+
					"example '2003/05/03 21:02:44'");
				String date = console.next();
				if (avail == "Media") {
					sop("enter instrument: 	(1) 'Mini Keyboard', (2) 'Microphones', (3) 'Cassette deck', (4) 'Guitar', (5) '88-key MIDI Keyboard', (6) 'Drum'");
					int instrNum = console.nextInt();
					String[] instrumentsList = {"Mini Keyboard", "Microphones", "Cassette deck", "Guitar", "88-key MIDI Keyboard", "Drum"};
					String instrString = instrumentsList[instrNum - 1];
					sop("enter number of chairs wanted 2 OR 4 OR 6");
					String chairNum = console.nextInt();
					db.bookMediaRoom(currentID, id, instrString, chairNum);
					displayResources();
				}
				db.reserveRoom(currentID, date, duration);
				displayResources();
			} else if (!isStudent && (avail != "")) {
				// student is allowed to book this room
				sop("enter reservation duration"); // todo figure out what format this is
				String duration = console.next();
				sop("enter reservation date, must be in this format YYYY-MON-DD HH24:MI"+
					"example '2003/05/03 21:02:44'");
				String date = console.next();
				if (avail == "Media") {
					sop("enter instrument: 	(1) 'Mini Keyboard', (2) 'Microphones', (3) 'Cassette deck', (4) 'Guitar', (5) '88-key MIDI Keyboard', (6) 'Drum'");
					int instrNum = console.nextInt();
					String[] instrumentsList = {"Mini Keyboard", "Microphones", "Cassette deck", "Guitar", "88-key MIDI Keyboard", "Drum"};
					String instrString = instrumentsList[instrNum - 1];
					sop("enter number of chairs wanted 2 OR 4 OR 6");
					String chairNum = console.nextInt();
					db.bookMediaRoom(currentID, id, instrString, chairNum);
					displayResources();
				}
				db.reserveRoom(currentID, date, duration);
				displayResources();
			} else {
				sop("failed");
				displayResources();
			}
			sop("");
		} else {
			displayResources();
		}
		String roomid = console.next();
		
	}
	
	/*On selecting Technology consultation option, the patron should be given the option to schedule a new a
	ppointment or view all their past appointments. The option to schedule a new appointment should take as input the location, 
	3 date and time slots, and the topic that they need help with, as mentioned in the description. 
	The option to view the log of past appointments should display all the details of that meeting along with the 
	option to give feedback for that meeting. */
	private void displayResourcesTechnologyConsultation() {
		
	}
	
	private void displayResourcesCameras() {
		
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
    	
    	// start user interface interaction
    	displayLogin();
    }
    
    // Have to make this dumb thing because System.out.println takes too long to type
    private void sop(Object obj) {
    	System.out.println(obj);
    }
}