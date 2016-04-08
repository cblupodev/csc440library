import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.BatchUpdateException;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.Statement;


// use to construct the database
class DBBuilder {
    
    private Connection con = null;
    
    public DBBuilder(Connection con) {
        this.con = con;
    }
    
    // create tables from https://drive.google.com/open?id=1BoMzhg6PL0osWdHVDvKXC_lRCbDRALIDwIVQD68d3pY
    public void createTables() {
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Books ("+
                "   edition varchar(16) NOT NULL,"+
                "   ISBN character(13) NOT NULL,"+
                "   pub_Date date NOT NULL,"+
                "	publisher varchar(32) NOT NULL,"+
                "	PRIMARY KEY (ISBN)"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Camera_Check_Out ("+
                "	patron_ID character(16) NOT NULL,"+
                "	camera_ID character(16) NOT NULL,"+
                "	check_out_date date NOT NULL,"+
                "	late_fee currency NOT NULL,"+
                "	PRIMARY KEY (patron_ID, camera_ID)"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (camera_ID) REFERENCES Cameras (camera_ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Camera_Wait_Queue ("+
                "	patron_ID character(16) NOT NULL,"+
                "	camera_ID character(16) NOT NULL,"+
                "	position int NOT NULL,"+
                "	PRIMARY KEY (patron_ID, camera_ID)"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (camera_ID) REFERENCES Cameras (camera_ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Cameras ("+
                "	camera_ID character(16) NOT NULL,"+
                "	library varchar(16) NOT NULL,"+
                "	make varchar(32) NOT NULL,"+
                "	model varchar(32) NOT NULL,"+
                "	lens_configuration varchar(32) NOT NULL,"+
                "	memory int NOT NULL,"+
                "	PRIMARY KEY (camera_ID)"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Conf_Booked ("+
                "	number int NOT NULL,"+
                "	library varchar(16) NOT NULL,"+
                "   patron_ID character(16) NOT NULL,"+
                "	duration time NOT NULL,"+
                "	date_begin_res datetime NOT NULL,"+
                "	checked_in datetime,"+
                "	PRIMARY KEY (number, library, date_begin_res),"+
                "	FOREIGN KEY (number) REFERENCES Conference_Rooms (number)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Conf_Proceedings ("+
                "	ID character(16) NOT NULL,"+
                "	conf_number int NOT NULL,"+
                "	conf_year smallint NOT NULL,"+
                "	copy_number int NOT NULL,"+
                "	PRIMARY KEY (ID),"+
                "	FOREIGN KEY (ID) REFERENCES Publications (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Conference_Rooms ("+
                "	number int NOT NULL,"+
                "	floor int NOT NULL,"+
                "	capacity int NOT NULL,"+
                "	PRIMARY KEY (number)"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Courses ("+
                "	patron_ID character(16) NOT NULL,"+
                "	course_ID varchar(16) NOT NULL,"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Faculty( IS A: Patron ) Has Courses ("+
                "	ID character(16) NOT NULL,"+
                "	category varchar(32) NOT NULL,"+
                "	PRIMARY KEY (ID),"+
                "	FOREIGN KEY (ID) REFERENCES Patrons (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Fees ("+
                "type varchar(16) NOT NULL,"+
                "	value currency NOT NULL,"+
                "	fee_ID varchar(16) NOT NULL,"+
                "	PRIMARY KEY (fee_ID),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Hardcopy ("+
                "	copy_num character(16) NOT NULL,"+
                "	ISBN character(16) NOT NULL,"+
                "	library varchar(16),"+
                "	PRIMARY KEY (copy_num, ISBN),"+
                "	FOREIGN KEY (ISBN) REFERENCES Books (ISBN)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Journal("+
                "	issn character(16) NOT NULL,"+
                "	copy_number int NOT NULL,"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Media_Production_Rooms ("+
                "	library varchar(16) NOT NULL,"+
                "	room_number int NOT NULL,"+
                "	item1 varchar(16),"+
                "	item2 varchar(16),"+
                "	item3 varchar(16),"+
                "	chairs int NOT NULL,"+
                "	PRIMARY KEY(library, roomID)"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Media_Room_Reservation ("+
                "	timeSlot datetime NOT NULL,"+
                "	checked_in datetime,"+
                "	checked_out datetime,"+
                "	room_number int NOT NULL,"+
                "	patronID varchar(16) NOT NULL,"+
                "	FOREIGN KEY (patronID) REFERENCES Patrons (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (roomID) REFERENCES MediaProductionRoom (roomID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Messages ("+
                "	msg_ID char(16) NOT NULL,"+
                "	msg varchar(512) NOT NULL,"+
                "	patron_ID char(16) NOT NULL,"+
                "	PRIMARY KEY (msg_ID),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE"+
                "		ON UPDATE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try {
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Patrons ("+
                "   first_name varchar(64) NOT NULL,"+
                "	last_name varchar(64) NOT NULL,"+
                "	ID character(16),"+
                "	dept character(3),"+
                "	nationality varchar(64),"+
                "	delinquent boolean NOT NULL,"+
                "	PRIMARY KEY (ID)"+
                ")"
            );
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }

        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Pub_Check_Out ("+
                "	patron_ID character(16),"+
                "	pub_ID character(16),"+
                "	check_out_date datetime NOT NULL,"+
                "	due_date datetime NOT NULL,"+
                "	late_fee currency,"+
                "	PRIMARY KEY (patron_ID, pub_ID),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (pub_ID) REFERENCES Publication (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Publication ("+
                "	title varchar(16) NOT NULL,"+
                "	ID character(16),"+
                "	library varchar(16) NOT NULL,"+
                "	has_electronic boolean NOT NULL,"+
                "	authors varchar(64),"+
                "	pub_year int,"+
                "	PRIMARY KEY (ID)"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Publication_Wait_Queue_Fac ("+
                "	patron_ID character(16),"+
                "	pub_ID character(16),"+
                "	library varchar(16),"+
                "	request_position int NOT NULL,"+
                "	PRIMARY KEY (patron_ID, pub_ID, library),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE"+
                "		ON UPDATE CASCADE,"+
                "	FOREIGN KEY (pub_ID) REFERENCES Publications (ID)"+
                "		ON DELETE CASCADE"+
                "		ON UPDATE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Publication_Wait_Queue_Stud ("+
                "	patron_ID character(16),"+
                "	pub_ID character(16),"+
                "	library varchar(16),"+
                "	request_position int NOT NULL,"+
                "	PRIMARY KEY (patron_ID, pub_ID, library),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE"+
                "		ON UPDATE CASCADE,"+
                "	FOREIGN KEY (pub_ID) REFERENCES Publications (ID)"+
                "		ON DELETE CASCADE"+
                "		ON UPDATE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
               "CREATE TABLE Resource_Request_History ("+
                "	patron_ID char(16),"+
                "	resource_ID char(16),"+
                "	check_in_time DATETIME NOT NULL,"+
                "	check_out_time DATETIME NOT NULL,"+
                "	title varchar(64),"+
                "	PRIMARY KEY (patron_ID, resource_ID, check_out_time),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons(id)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (resource_ID) REFERENCES Resources(id)"+
                "		ON UPDATE CASCADE"+
                "	    ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Reserved ("+
                "	PRIMARY KEY (copy_num),"+
                "	FOREIGN KEY (copy_num) REFERENCES Hardcopy (copy_num)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Student_Hold_List ("+
                "	ID character(16),"+
                "   PRIMARY KEY (ID),"+
                "   FOREIGN KEY (ID) REFERENCES Patrons (ID)"+
                "		ON UPDATE CASCADE"+
                "	    ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Students ("+
                "	ID character(16),"+
                "	phone character(14) NOT NULL,"+
                "	alt_phone character(14),"+
                "	street varchar(64) NOT NULL,"+
                "	city varchar(64) NOT NULL,"+
                "	postcode character(5) NOT NULL,"+
                "	dob datetime NOT NULL,"+
                "	sex varchar(32),"+
                "	classification varchar(16) NOT NULL,"+
                "	PRIMARY KEY (ID),"+
                "	FOREIGN KEY (ID) REFERENCES Patrons (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Study_Booked ("+
                "	patron_ID character(16) NOT NULL,"+
                "	room_number int NOT NULL,"+
                "	library varchar(16) NOT NULL,"+
                "	date_begin_reservation datetime NOT NULL,"+
                "	duration time NOT NULL,"+
                "	checked_in datetime,"+
                "	PRIMARY KEY (room_number, library, date_begin_reservation),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (room_number, library) REFERENCES Study_Rooms (room_number, library)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Study_Rooms ("+
                "	room_number int,"+
                "	library varchar(16),"+
                "	floor int NOT NULL,"+
                "	capacity int NOT NULL,"+
                " 	PRIMARY KEY (room_number, library)"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Tech_Consult ("+
                "	patronID varchar(16) NOT NULL,"+
                "	timeSlot1 datetime NOT NULL,"+
                "	timeSlot2 datetime NOT NULL,"+
                "	timeSlot3 datetime NOT NULL,"+
                "	library varchar(16) NOT NULL,"+
                "	help_category varchar(16) NOT NULL,"+
                "	PRIMARY KEY (tcID)"+
                "	FOREIGN KEY (patronID) REFERENCES Patrons (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Tech_Consultation_Log ("+
                "	patronID varchar(16) NOT NULL,"+
                "   scheduled boolean NOT NULL,"+
                "   library varchar(16) NOT NULL,"+
                "   feedback varchar(256),"+
                "   time_slot datetime NOT NULL,"+
                "   tcID varchar(16) NOT NULL,"+
                "   helpCategory varchar(16) NOT NULL,"+
                "   PRIMARY KEY(time_slot)"+
                "   FOREIGN KEY(patronID) REFERENCES Patrons(ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE,"+
                "   FOREIGN KEY (tcID) REFERENCES TechConsult(tcID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Tech_Reservation ("+
                "	timeSlot datetime NOT NULL,"+
                "	tcID varchar(16) NOT NULL,"+
                "	patronID varchar(16) NOT NULL,"+
                "	FOREIGN KEY (tcID) REFERENCES TechConsult (tcID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (patronID) REFERENCES Patrons (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
    }
    
    public void fillTables() {
        // import from https://drive.google.com/open?id=14-YvxM3s_P8XuszpTIbGdTIREDulbbQmm8u59GlqyYw
        try {
            PreparedStatement ps = con.prepareStatement(
                "INSERT INTO Patrons ("+
                "Jesse,"+
                "Pinkman,"+
                "S1,"+
                "Chemistry"+
                "American,"+
                "false,"+
                ""+
                ")"
            );
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    // http://www.tutorialspoint.com/jdbc/jdbc-batch-processing.htm
    public void deleteTables() {
        try {
            Statement stmnt = con.createStatement();
            // Set auto-commit to false
            con.setAutoCommit(false);
            
            // http://stackoverflow.com/questions/1799128/oracle-if-table-exists
            stmnt.addBatch("DROP TABLE Books");
            stmnt.addBatch("DROP TABLE Camera_Check_Out");
            stmnt.addBatch("DROP TABLE Camera_Wait_Queue");
            stmnt.addBatch("DROP TABLE Cameras");
            stmnt.addBatch("DROP TABLE Conf_Booked");
            stmnt.addBatch("DROP TABLE Conf_Proceedings");
            stmnt.addBatch("DROP TABLE Conference_Rooms");
            stmnt.addBatch("DROP TABLE Courses");
            stmnt.addBatch("DROP TABLE Faculty");
            stmnt.addBatch("DROP TABLE Fees");
            stmnt.addBatch("DROP TABLE Hardcopy");
            stmnt.addBatch("DROP TABLE Journal");
            stmnt.addBatch("DROP TABLE Media_Production_Rooms");
            stmnt.addBatch("DROP TABLE Media_Room_Reservation");
            stmnt.addBatch("DROP TABLE Messages");
            stmnt.addBatch("DROP TABLE Patrons");
            stmnt.addBatch("DROP TABLE Pub_Check_Out");
            stmnt.addBatch("DROP TABLE Publication");
            stmnt.addBatch("DROP TABLE Publication_Wait_Queue_Fac");
            stmnt.addBatch("DROP TABLE Publication_Wait_Queue_Stud");
            stmnt.addBatch("DROP TABLE Reserved");
            stmnt.addBatch("DROP TABLE Resource_Request_History");
            stmnt.addBatch("DROP TABLE Student_Hold_List");
            stmnt.addBatch("DROP TABLE Students");
            stmnt.addBatch("DROP TABLE Study_Booked");
            stmnt.addBatch("DROP TABLE Study_Rooms");
            stmnt.addBatch("DROP TABLE Tech_Consult:");
            stmnt.addBatch("DROP TABLE Tech_Consultation_Log");
            stmnt.addBatch("DROP TABLE Tech_Reservation");
            stmnt.executeBatch();
            
            //Explicitly commit statements to apply changes
            con.commit();
        } catch (BatchUpdateException e) {
            System.out.println("BatchUpdateException was thrown but that's probably because you tried to delete tables that don't exist"); 
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }
}