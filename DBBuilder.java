import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.BatchUpdateException;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.SQLSyntaxErrorException;


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
        
        try {
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Patrons ("+
                "   first_name varchar(64) NOT NULL,"+
                "	last_name varchar(64),"+
                "	ID character(16),"+
                "	dept varchar(36),"+
                "	nationality varchar(64),"+
                "	delinquent int NOT NULL,"+
                "   user_name varchar(32) NOT NULL,"+
                "   password varchar(32) NOT NULL,"+
                "	PRIMARY KEY (ID)"+
                ")"
            );
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
        
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
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Camera_Check_Out ("+
        //         "	patron_ID character(16) NOT NULL,"+
        //         "	camera_ID character(16) NOT NULL,"+
        //         "	check_out_date date NOT NULL,"+
        //         "	Due_date date NOT NULL,"+
        //         "	late_fee currency NOT NULL,"+
        //         "	PRIMARY KEY (patron_ID, camera_ID),"+
        //         "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE,"+
        //         "	FOREIGN KEY (camera_ID) REFERENCES Cameras (camera_ID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // todo foreign key references non existent table?
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Camera_Wait_Queue ("+
                "	patron_ID character(16) NOT NULL,"+
                "	camera_ID character(16) NOT NULL,"+
                "	place int NOT NULL,"+
                "	PRIMARY KEY (patron_ID, camera_ID),"+
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
        
        // todo the time is probably a bad data type
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Conf_Booked ("+
        //         "	number int NOT NULL,"+
        //         "	library varchar(16) NOT NULL,"+
        //         "   patron_ID character(16) NOT NULL,"+
        //         "	duration time NOT NULL,"+
        //         "	date_begin_res datetime NOT NULL,"+
        //         "	checked_in datetime,"+
        //         "	PRIMARY KEY (number, library, date_begin_res),"+
        //         "	FOREIGN KEY (number) REFERENCES Conference_Rooms (number)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE,"+
        //         "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // todo missing keyword
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Conf_Proceedings("+
                "	conf_number varchar(8) NOT NULL,"+
                "	conf_name varchar(16) NOT NULL,"+
                "	PRIMARY KEY (conf_number),"+
                "	FOREIGN KEY (conf_number) REFERENCES Publications (ID)"+
                "		ON UPDATE CASCADE"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        // todo 'missing keyword' error
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Conf_Proceedings("+
        //         "	conf_number varchar(8) NOT NULL,"+
        //         "	conf_name varchar(16) NOT NULL,"+
        //         "	PRIMARY KEY (conf_number),"+
        //         "	FOREIGN KEY (conf_number) REFERENCES Publications (ID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Courses ("+
        //         "	course_ID varchar(16) NOT NULL,"+
        //         "	isbn varchar(16) NOT NULL,"+
        //         "	PRIMARY KEY (course_ID),"+
        //         "	FOREIGN KEY (isbn) REFERENCES Books (ISBN)"+
        //         "ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Courses_Students ("+
        //         "	course_ID varchar(16) NOT NULL,"+
        //         "	patron_ID varchar(16) NOT NULL,"+
        //         "	PRIMARY KEY (course_ID),"+
        //         "	FOREIGN KEY (course_ID) REFERENCES Courses (course_ID)"+
        //         "       ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE,"+
        //         "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
        //         "       ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Faculty ("+
        //         "	ID character(16) NOT NULL,"+
        //         "	category varchar(32) NOT NULL,"+
        //         "	PRIMARY KEY (ID),"+
        //         "	FOREIGN KEY (ID) REFERENCES Patrons (ID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // // todo the currency values is probably messing this up
        // // try{
        // //     PreparedStatement ps = con.prepareStatement(
        // //         "CREATE TABLE Fees ("+
        // //         "type varchar(16) NOT NULL,"+
        // //         "	value currency NOT NULL,"+
        // //         "	fee_ID varchar(16) NOT NULL,"+
        // //         "	PRIMARY KEY (fee_ID),"+
        // //         "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
        // //         "		ON UPDATE CASCADE"+
        // //         "		ON DELETE CASCADE"+
        // //         ")"
        // //     );
        // //     ps.executeUpdate();
        // // }  catch (SQLException e) { e.printStackTrace(); }
        
        // // todo, error was 'missing keyword' may have something to do with books not being created before this one?
        // // try{
        // //     PreparedStatement ps = con.prepareStatement(
        // //         "CREATE TABLE Hardcopy ("+
        // //         "	copy_num character(16),"+
        // //         "	ISBN character(16),"+
        // //         "	PRIMARY KEY (copy_num, ISBN),"+
        // //         "	FOREIGN KEY (ISBN) REFERENCES Books (ISBN)"+
        // //         "		ON UPDATE CASCADE"+
        // //         "		ON DELETE CASCADE"+
        // //         ")"
        // //     );
        // //     ps.executeUpdate();
        // // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Journal("+
        //         "	issn character(16) NOT NULL,"+
        //         "	copy_number int NOT NULL,"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Media_Production_Rooms ("+
        //         "	room_number varchar(8),"+
        //         "	item1 varchar(16),"+
        //         "	item2 varchar(16),"+
        //         "	item3 varchar(16),"+
        //         "	chairs int NOT NULL,"+
        //         "	PRIMARY KEY(roomID)"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Media_Room_Reservation ("+
        //         "	timeSlot datetime NOT NULL,"+
        //         "	checked_in datetime,"+
        //         "	checked_out datetime,"+
        //         "	room_number int NOT NULL,"+
        //         "	patronID varchar(16) NOT NULL,"+
        //         "	FOREIGN KEY (patronID) REFERENCES Patrons (ID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE,"+
        //         "	FOREIGN KEY (roomID) REFERENCES MediaProductionRoom (roomID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Messages ("+
        //         "	msg_ID char(16) NOT NULL,"+
        //         "	msg varchar(512) NOT NULL,"+
        //         "	patron_ID char(16) NOT NULL,"+
        //         "	PRIMARY KEY (msg_ID),"+
        //         "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
        //         "		ON DELETE CASCADE"+
        //         "		ON UPDATE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }

        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Pub_Check_Out ("+
        //         "	patron_ID character(16),"+
        //         "	pub_ID character(16),"+
        //         "	check_out_date datetime NOT NULL,"+
        //         "	due_date datetime NOT NULL,"+
        //         "	late_fee currency,"+
        //         "	PRIMARY KEY (patron_ID, pub_ID),"+
        //         "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE,"+
        //         "	FOREIGN KEY (pub_ID) REFERENCES Publication (ID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Publication ("+
        //         "	title varchar(16) NOT NULL,"+
        //         "	ID character(16),"+
        //         "	library varchar(16) NOT NULL,"+
        //         "	has_electronic boolean NOT NULL,"+
        //         "	authors varchar(64),"+
        //         "	pub_year int,"+
        //         "	PRIMARY KEY (ID)"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Publication_Wait_Queue_Fac ("+
        //         "	patron_ID character(16),"+
        //         "	pub_ID character(16),"+
        //         "	library varchar(16),"+
        //         "	request_position int NOT NULL,"+
        //         "	PRIMARY KEY (patron_ID, pub_ID, library),"+
        //         "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
        //         "		ON DELETE CASCADE"+
        //         "		ON UPDATE CASCADE,"+
        //         "	FOREIGN KEY (pub_ID) REFERENCES Publications (ID)"+
        //         "		ON DELETE CASCADE"+
        //         "		ON UPDATE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Publication_Wait_Queue_Stud ("+
        //         "	patron_ID character(16),"+
        //         "	pub_ID character(16),"+
        //         "	library varchar(16),"+
        //         "	request_position int NOT NULL,"+
        //         "	PRIMARY KEY (patron_ID, pub_ID, library),"+
        //         "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
        //         "		ON DELETE CASCADE"+
        //         "		ON UPDATE CASCADE,"+
        //         "	FOREIGN KEY (pub_ID) REFERENCES Publications (ID)"+
        //         "		ON DELETE CASCADE"+
        //         "		ON UPDATE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //       "CREATE TABLE Resource_Request_History ("+
        //         "	patron_ID char(16),"+
        //         "	resource_ID char(16),"+
        //         "	check_out_time DATETIME NOT NULL,"+
        //         "	check_in_time DATETIME,"+
        //         "	title varchar(64),"+
        //         "	PRIMARY KEY (patron_ID, resource_ID, check_out_time),"+
        //         "	FOREIGN KEY (patron_ID) REFERENCES Patrons(id)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE,"+
        //         "	FOREIGN KEY (resource_ID) REFERENCES Resources(id)"+
        //         "		ON UPDATE CASCADE"+
        //         "	    ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Reserved ("+
        //         "	PRIMARY KEY (copy_num),"+
        //         "	FOREIGN KEY (copy_num) REFERENCES Hardcopy (copy_num)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Student_Hold_List ("+
        //         "	ID character(16),"+
        //         "   PRIMARY KEY (ID),"+
        //         "   FOREIGN KEY (ID) REFERENCES Patrons (ID)"+
        //         "		ON UPDATE CASCADE"+
        //         "	    ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Students ("+
        //         "	ID character(16),"+
        //         "	phone character(14) NOT NULL,"+
        //         "	alt_phone character(14),"+
        //         "	street varchar(64) NOT NULL,"+
        //         "	city varchar(64) NOT NULL,"+
        //         "	postcode character(5) NOT NULL,"+
        //         "   state character(2) NOT NULL,"+
        //         "	dob date NOT NULL,"+
        //         "	sex varchar(32),"+
        //         "	classification varchar(16) NOT NULL,"+
        //         "   degree_program varchar(16) NOT NULL,"+
        //         "   category varchar(16) NOT NULL,"+
        //         "	PRIMARY KEY (ID),"+
        //         "	FOREIGN KEY (ID) REFERENCES Patrons (ID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Study_Booked ("+
        //         "	patron_ID character(16) NOT NULL,"+
        //         "	room_number int NOT NULL,"+
        //         "	library varchar(16) NOT NULL,"+
        //         "	date_begin_reservation datetime NOT NULL,"+
        //         "	duration time NOT NULL,"+
        //         "	checked_in datetime,"+
        //         "	PRIMARY KEY (room_number, library, date_begin_reservation),"+
        //         "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE,"+
        //         "	FOREIGN KEY (room_number, library) REFERENCES Study_Rooms (room_number, library)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Study_Rooms ("+
        //         "	room_number int,"+
        //         "	library varchar(16),"+
        //         "	floor int NOT NULL,"+
        //         "	capacity int NOT NULL,"+
        //         " 	PRIMARY KEY (room_number, library)"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Tech_Consult ("+
        //         "	patronID varchar(16) NOT NULL,"+
        //         "	timeSlot1 datetime NOT NULL,"+
        //         "	timeSlot2 datetime NOT NULL,"+
        //         "	timeSlot3 datetime NOT NULL,"+
        //         "	library varchar(16) NOT NULL,"+
        //         "	help_category varchar(16) NOT NULL,"+
        //         "	PRIMARY KEY (tcID)"+
        //         "	FOREIGN KEY (patronID) REFERENCES Patrons (ID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Tech_Consultation_Log ("+
        //         "	patronID varchar(16) NOT NULL,"+
        //         "   scheduled boolean NOT NULL,"+
        //         "   library varchar(16) NOT NULL,"+
        //         "   feedback varchar(256),"+
        //         "   time_slot datetime NOT NULL,"+
        //         "   tcID varchar(16) NOT NULL,"+
        //         "   helpCategory varchar(16) NOT NULL,"+
        //         "   PRIMARY KEY(time_slot)"+
        //         "   FOREIGN KEY(patronID) REFERENCES Patrons(ID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE,"+
        //         "   FOREIGN KEY (tcID) REFERENCES TechConsult(tcID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
        
        // try{
        //     PreparedStatement ps = con.prepareStatement(
        //         "CREATE TABLE Tech_Reservation ("+
        //         "	timeSlot datetime NOT NULL,"+
        //         "	tcID varchar(16) NOT NULL,"+
        //         "	patronID varchar(16) NOT NULL,"+
        //         "	FOREIGN KEY (tcID) REFERENCES TechConsult (tcID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE,"+
        //         "	FOREIGN KEY (patronID) REFERENCES Patrons (ID)"+
        //         "		ON UPDATE CASCADE"+
        //         "		ON DELETE CASCADE"+
        //         ")"
        //     );
        //     ps.executeUpdate();
        // }  catch (SQLException e) { e.printStackTrace(); }
    }
    
    public void fillTables() {
        // import from https://drive.google.com/open?id=14-YvxM3s_P8XuszpTIbGdTIREDulbbQmm8u59GlqyYw
        try { //Jesse Pinkman
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Patrons ("+
                "Jesse,"+
                "Pinkman,"+
                "S1,"+
                "Chemistry"+
                "American,"+
                "false,"+
                "jpink,"+
                "jpink,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Students ("+
                "S1,"+
                "123456789,"+
                "123456787,"+
                "1511 Graduate Lane,"+
                "Raleigh,"+
                "276006,"+
                "NC,"+
                "10/03/1988,"+
                "Male,"+
                "Undergraduate,"+
                "BS,"+
                "First Year"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try { // Walt Jr.
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Patrons ("+
                "Walt,"+ //First Name
                "Jr.,"+ //Last Name
                "S2,"+ //ID
                "Chemistry,"+ //Department
                "American,"+ //Nationality
                "false,"+ //delinquent
                "wjr,"+ //username
                "wjr,"+ //password
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Students ("+
                "S2,"+ //ID
                "123456780,"+ //Phone
                "123456781,"+ //Alt-Phone
                "1512 Graduate Lane,"+ //Address
                "Raleigh,"+ //City
                "NC,"+ //Postal Code
                "27606,"+ //State
                "11/03/1988,"+ //Dob mm/dd/yyyy
                "Male,"+ //Sex
                "Undergraduate,"+ //classification
                "BS,"+ //degree program
                "Second Year,"+ //category
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        
        try { // Gale Boetticher.
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Patrons ("+
                "Gale,"+ //First Name
                "Boetticher,"+ //Last Name
                "S3,"+ //ID
                "Chemistry,"+ //Department
                "Chile,"+ //Nationality
                "false,"+ //delinquent
                "gboet,"+ //username
                "gboet,"+ //password
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Students ("+
                "S3,"+ //ID
                "123456782,"+ //Phone
                "123456783,"+ //Alt-Phone
                "1513 Graduate Lane,"+ //Address
                "Raleigh,"+ //City
                "NC,"+ //Postal Code
                "27606,"+ //State
                "12/03/1988,"+ //Dob mm/dd/yyyy
                "Male,"+ //Sex
                "Undergraduate,"+ //classification
                "BS,"+ //degree program
                "Third Year,"+ //category
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        
        try { //Saul Goodman
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Patrons ("+
                "Saul,"+ //First Name
                "Goodman,"+ //Last Name
                "S4,"+ //ID
                "Chemistry,"+ //Department
                "American,"+ //Nationality
                "false,"+ //delinquent
                "sgood,"+ //username
                "sgood,"+ //password
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Students ("+
                "S4,"+ //ID
                "123456784,"+ //Phone
                "123456785,"+ //Alt-Phone
                "1514 Graduate Lane,"+ //Address
                "Raleigh,"+ //City
                "NC,"+ //Postal Code
                "27606,"+ //State
                "01/03/1988,"+ //Dob mm/dd/yyyy
                "Male,"+ //Sex
                "Graduate,"+ //classification
                "MS,"+ //degree program
                "Second Year,"+ //category
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Patrons ("+
                "Walter,"+ //First Name
                "White,"+ //Last Name
                "F1,"+ //ID
                "Chemistry,"+ //Department
                "American,"+ //Nationality
                "false,"+ //delinquent
                "wwhite,"+ //username
                "wwhite,"+ //password
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Faculty ("+
                "F1,"+ //ID
                "Assistant Professor,"+ //category
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        
         try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Patrons ("+
                "Gustavo,"+ //First Name
                "Fring,"+ //Last Name
                "F2,"+ //ID
                "Chemistry,"+ //Department
                "American,"+ //Nationality
                "false,"+ //delinquent
                "gfring,"+ //username
                "gfring,"+ //password
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Faculty ("+
                "F2,"+ //ID
                "Assistant Professor,"+ //category
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Patrons ("+
                "Hank,"+ //First Name
                "Schrader,"+ //Last Name
                "F3,"+ //ID
                "Chemistry,"+ //Department
                "American,"+ //Nationality
                "false,"+ //delinquent
                "hschrad,"+ //username
                "hschrad,"+ //password
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Faculty ("+
                "F3,"+ //ID
                "Associate Professor,"+ //category
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Patrons ("+
                "Skyler,"+ //First Name
                "White,"+ //Last Name
                "F4,"+ //ID
                "Chemistry,"+ //Department
                "American,"+ //Nationality
                "false,"+ //delinquent
                "swhite,"+ //username
                "swhite,"+ //password
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Faculty ("+
                "F4,"+ //ID
                "Professor,"+ //category
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        //Courses
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses ("+
                "F1,"+ //ID
                "CH101,"+ //Course ID?
                "B1,"+ //Pub ID
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses_Students ("+
                "CH101,"+ //Course ID?
                "S1,"+ 
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses_Students ("+
                "CH101,"+ //Course ID?
                "S2,"+ 
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses_Students ("+
                "CH101,"+ //Course ID?
                "S3,"+ 
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses ("+
                "F2,"+ //ID
                "CH102,"+
                "B2,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses_Students ("+
                "CH102,"+ //Course ID?
                "S2,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses_Students ("+
                "CH102,"+ //Course ID?
                "S3,"+ 
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses_Students ("+
                "CH102,"+ //Course ID?
                "S4,"+ 
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses ("+
                "F3,"+ //ID
                "CH103,"+
                "B3,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses_Students ("+
                "CH103,"+ //Course ID?
                "S3,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses_Students ("+
                "CH103,"+ //Course ID?
                "S4,"+ 
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses_Students ("+
                "CH103,"+ //Course ID?
                "S1,"+ 
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        //C4
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses ("+
                "F4,"+ //ID
                "CH104,"+
                "B4,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses_Students ("+
                "CH104,"+ //Course ID?
                "S1,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses_Students ("+
                "CH104,"+ //Course ID?
                "S2,"+ 
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Courses_Students ("+
                "CH104,"+ //Course ID?
                "S4,"+ 
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        //Books
        //B1
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Publication ("+
                "Introduction to Chemistry,"+
                "B1,"+ 
                "true,"+
                "SK Goyal,"+
                "2005,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Books ("+
                "1,"+
                "B1,"+
                "2005,"+
                "Pub1,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Hardcopy ("+
                "1,"+
                "B1,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Hardcopy ("+
                "2,"+
                "B1,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Reserved ("+
                "B1,"+
                "CH101,"+
                "07/08/2016,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        //B2
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Publication ("+
                "Introduction to Organic Chemistry,"+
                "B2,"+ 
                "true,"+
                "HC Verma,"+
                "2006,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Books ("+
                "2,"+
                "B2,"+
                "2006,"+
                "Pub2,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Hardcopy ("+
                "1,"+
                "B2,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Hardcopy ("+
                "2,"+
                "B2,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        //b3
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Publication ("+
                "Introduction to Physical Chemistry,"+
                "B3,"+ 
                "false,"+
                "Resnick Halliday Walker,"+
                "2000,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Books ("+
                "3,"+
                "B3,"+
                "2000,"+
                "Pub3,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Hardcopy ("+
                "1,"+
                "B3,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Hardcopy ("+
                "2,"+
                "B3,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        try {
            Statement s = con.createStatement();
            s.executeUpdate(
                "INSERT INTO Reserved ("+
                "B3,"+
                "CH103,"+
                "07/08/2016,"+
                ""+
                ")"
            );
        } catch (SQLException e) { e.printStackTrace(); }
        
        
        
        
        
    }
    
    // http://www.tutorialspoint.com/jdbc/jdbc-batch-processing.htm
    public void deleteTables() {
        try {
            Statement stmnt;
            String[] drops = 
            {
                "DROP TABLE Books",
                "DROP TABLE Camera_Check_Out",
                "DROP TABLE Camera_Wait_Queue",
                "DROP TABLE Cameras",
                "DROP TABLE Conf_Booked",
                "DROP TABLE Conf_Proceedings",
                "DROP TABLE Conference_Rooms",
                "DROP TABLE Courses",
                "DROP TABLE Faculty",
                "DROP TABLE Fees",
                "DROP TABLE Hardcopy",
                "DROP TABLE Journal",
                "DROP TABLE Media_Production_Rooms",
                "DROP TABLE Media_Room_Reservation",
                "DROP TABLE Messages",
                "DROP TABLE Patrons",
                "DROP TABLE Pub_Check_Out",
                "DROP TABLE Publication",
                "DROP TABLE Publication_Wait_Queue_Fac",
                "DROP TABLE Publication_Wait_Queue_Stud",
                "DROP TABLE Reserved",
                "DROP TABLE Resource_Request_History",
                "DROP TABLE Student_Hold_List",
                "DROP TABLE Students",
                "DROP TABLE Study_Booked",
                "DROP TABLE Study_Rooms",
                "DROP TABLE Tech_Consult:",
                "DROP TABLE Tech_Consultation_Log",
                "DROP TABLE Tech_Reservation"
            };
            for (int i = 0; i < drops.length; i++) {
                try {
                    stmnt = con.createStatement();
                    stmnt.executeUpdate(drops[i]);
                } catch (SQLSyntaxErrorException e) {
                    System.out.println("threw SQLSyntaxErrorException buts that probably because the table doesn't exist");
                }
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }
}