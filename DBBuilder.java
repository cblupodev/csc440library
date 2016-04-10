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
                "   ISBN varchar(16) NOT NULL,"+
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
                "	ID varchar(16),"+
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
                "	camera_ID varchar(16) NOT NULL,"+
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
                "CREATE TABLE Publication ("+
                "	title varchar(16) NOT NULL,"+
                "	ID varchar(16) NOT NULL,"+
                "	library varchar(16) NOT NULL,"+
                "	has_electronic int NOT NULL,"+
                "	authors varchar(64),"+
                "	pub_year int,"+
                "	PRIMARY KEY (ID)"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Camera_Check_Out ("+
                "	patron_ID varchar(16) NOT NULL,"+
                "	camera_ID varchar(16) NOT NULL,"+
                "	check_out_date date NOT NULL,"+
                "	due_date date NOT NULL,"+
                "	late_fee int NOT NULL,"+
                "	PRIMARY KEY (patron_ID, camera_ID),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (camera_ID) REFERENCES Cameras (camera_ID)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Camera_Wait_Queue ("+
                "	patron_ID varchar(16) NOT NULL,"+
                "	camera_ID varchar(16) NOT NULL,"+
                "	place int NOT NULL,"+
                "	PRIMARY KEY (patron_ID, camera_ID),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (camera_ID) REFERENCES Cameras (camera_ID)"+
                "		ON DELETE CASCADE"+
                ")"                
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Conference_Rooms ("+
                "	place int NOT NULL,"+
                "	floor int NOT NULL,"+
                "	capacity int NOT NULL,"+
                "	library varchar(16) NOT NULL,"+
                "	PRIMARY KEY (place, library)"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Conf_Booked ("+
                "	place int NOT NULL,"+
                "	library varchar(16) NOT NULL,"+
                "   patron_ID varchar(16) NOT NULL,"+
                "	duration int NOT NULL,"+
                "	date_begin_res timestamp NOT NULL,"+
                "	checked_in timestamp,"+
                "	PRIMARY KEY (place, library, date_begin_res),"+
                "	FOREIGN KEY (place, library) REFERENCES Conference_Rooms (place, library)"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Conf_Proceedings("+
                "	conf_number varchar(16) NOT NULL,"+
                "	conf_name varchar(16) NOT NULL,"+
                "	PRIMARY KEY (conf_number),"+
                "	FOREIGN KEY (conf_number) REFERENCES Publication (ID)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Courses ("+
                "	course_ID varchar(16) NOT NULL,"+
                "	isbn varchar(16) NOT NULL,"+
                "	PRIMARY KEY (course_ID),"+
                "	FOREIGN KEY (isbn) REFERENCES Books (ISBN)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Courses_Students ("+
                "	course_ID varchar(16) NOT NULL,"+
                "	patron_ID varchar(16) NOT NULL,"+
                "	PRIMARY KEY (course_ID),"+
                "	FOREIGN KEY (course_ID) REFERENCES Courses (course_ID)"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        // todo 
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Courses_Faculty ("+
                "	course_ID varchar(16) NOT NULL,"+
                "	patron_ID varchar(16) NOT NULL,"+
                "	PRIMARY KEY (course_ID),"+
                "	FOREIGN KEY (course_ID) REFERENCES Courses (course_ID)"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Digital_Media_Assistants ("+
                "   dmassistant_ID varchar(16) NOT NULL,"+
                "   name varchar(16) NOT NULL,"+
                "   expertise1 varchar(32) NOT NULL,"+
                "	expertise2 varchar(32) NOT NULL,"+
                "	expertise3 varchar(32) NOT NULL,"+
                "	PRIMARY KEY (dmassistant_ID)"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Faculty ("+
                "	ID varchar(16) NOT NULL,"+
                "	category varchar(32) NOT NULL,"+
                "	PRIMARY KEY (ID),"+
                "	FOREIGN KEY (ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Fees ("+
                "	patron_ID varchar(16) NOT NULL,"+
                "   type varchar(16) NOT NULL,"+
                "	value int NOT NULL,"+
                "	fee_ID varchar(16) NOT NULL,"+
                "	PRIMARY KEY (fee_ID),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Hardcopy ("+
                "	copy_num varchar(16),"+
                "	ISBN varchar(16),"+
                "	PRIMARY KEY (copy_num, ISBN),"+
                "	FOREIGN KEY (ISBN) REFERENCES Books (ISBN)"+
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
                "   PRIMARY KEY (issn)"+                
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Media_Rooms ("+
                "	room_number int NOT NULL,"+
                "	item1 varchar(16),"+
                "	item2 varchar(16),"+
                "	item3 varchar(16),"+
                "	chairs int NOT NULL,"+
                "   room_ID char(3) NOT NULL,"+
                "	PRIMARY KEY(room_number)"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Media_Booked ("+
                "	timeSlot timestamp NOT NULL,"+
                "	checked_in timestamp,"+
                "	checked_out timestamp,"+
                "	room_number int NOT NULL,"+
                "	patronID varchar(16) NOT NULL,"+
                "	FOREIGN KEY (patronID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (room_number) REFERENCES Media_Rooms (room_number)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Messages ("+
                "	msg_ID varchar(16) NOT NULL,"+
                "	msg varchar(512) NOT NULL,"+
                "	patron_ID varchar(16) NOT NULL,"+
                "	PRIMARY KEY (msg_ID),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }

        // todo datetime invalid data type
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Pub_Check_Out ("+
                "	patron_ID varchar(16),"+
                "	pub_ID varchar(16),"+
                "	check_out_date timestamp NOT NULL,"+
                "	due_date timestamp NOT NULL,"+
                "	late_fee int,"+
                "	PRIMARY KEY (patron_ID, pub_ID),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (pub_ID) REFERENCES Publication (ID)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Publication_Wait_Queue_Fac ("+
                "	patron_ID varchar(16),"+
                "	pub_ID varchar(16),"+
                "	library varchar(16),"+
                "	request_position int NOT NULL,"+
                "	PRIMARY KEY (patron_ID, pub_ID, library),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (pub_ID) REFERENCES Publication (ID)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Publication_Wait_Queue_Stud ("+
                "	patron_ID varchar(16),"+
                "	pub_ID varchar(16),"+
                "	library varchar(16),"+
                "	request_position int NOT NULL,"+
                "	PRIMARY KEY (patron_ID, pub_ID, library),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (pub_ID) REFERENCES Publication (ID)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        // todo need to change datetime datatype
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Resource_Request_History("+
                "	patron_ID varchar(16),"+
                "	resource_ID varchar(16),"+
                "	check_out_time timestamp NOT NULL,"+
                "	check_in_time timestamp,"+
                "   duration int,"+
                "   tech_log_id INT,"+
                "   PRIMARY KEY (patron_ID, resource_ID, check_out_time)"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Reserved ("+
                "   isbn varchar(16) NOT NULL,"+
                "	due_date date,"+
                "	course varchar(16),"+
                "	PRIMARY KEY (isbn),"+
                "	FOREIGN KEY (isbn) REFERENCES Books (isbn)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Student_Hold_List ("+
                "	ID varchar(16),"+
                "   PRIMARY KEY (ID),"+
                "   FOREIGN KEY (ID) REFERENCES Patrons (ID)"+
                "	    ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Students ("+
                "	ID varchar(16),"+
                "	phone character(14) NOT NULL,"+
                "	alt_phone character(14),"+
                "	street varchar(64) NOT NULL,"+
                "	city varchar(64) NOT NULL,"+
                "   state character(2) NOT NULL,"+
                "	postcode character(5) NOT NULL,"+
                "	dob timestamp NOT NULL,"+
                "	sex varchar(32),"+
                "	classification varchar(16) NOT NULL,"+
                "   degree_program varchar(16) NOT NULL,"+
                "   category varchar(16) NOT NULL,"+
                "	PRIMARY KEY (ID),"+
                "	FOREIGN KEY (ID) REFERENCES Patrons (ID)"+
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
        
        // todo table or view doesn't exist
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Study_Booked ("+
                "	patron_ID varchar(16) NOT NULL,"+
                "	room_number int NOT NULL,"+
                "	library varchar(16) NOT NULL,"+
                "	date_begin_reservation timestamp NOT NULL,"+
                "	duration int NOT NULL,"+
                "	checked_in timestamp,"+
                "	PRIMARY KEY (room_number, library, date_begin_reservation),"+
                "	FOREIGN KEY (patron_ID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (room_number, library) REFERENCES Study_Rooms (room_number, library)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Tech_Consult ("+
                "   tcID varchar(16)NOT NULL,"+
                "	patronID varchar(16) NOT NULL,"+
                "	timeSlot1a timestamp NOT NULL,"+
                "	timeSlot1b timestamp NOT NULL,"+
                "	timeSlot2a timestamp NOT NULL,"+
                "	timeSlot2b timestamp NOT NULL,"+
                "	timeSlota timestamp NOT NULL,"+
                "	timeSlotb timestamp NOT NULL,"+
                "	library varchar(16) NOT NULL,"+
                "	help_category varchar(16) NOT NULL,"+
                "	PRIMARY KEY (tcID),"+
                "	FOREIGN KEY (patronID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Tech_Consultation_Log ("+
                "      tech_log_id varchar(16) NOT NULL,"+
                "	   patronID varchar(16) NOT NULL,"+
                "      scheduled int NOT NULL,"+
                "      library varchar(16) NOT NULL,"+
                "      feedback varchar(256),"+
                "      time_slota timestamp NOT NULL,"+
                "      time_slotb timestamp NOT NULL,"+
                "      tcID varchar(16) NOT NULL,"+
                "      helpCategory varchar(16) NOT NULL,"+
                "      PRIMARY KEY(tech_log_id),"+
                "      FOREIGN KEY(patronID) REFERENCES Patrons(ID)"+
                "	    	ON DELETE CASCADE,"+
                "      FOREIGN KEY (tcID) REFERENCES TechConsult(tcID)"+
                "	    	ON DELETE CASCADE"+
                ")"
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
        
        try{
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE Tech_Reservation ("+
                "	timeSlot timestamp NOT NULL,"+
                "	tcID varchar(16) NOT NULL,"+
                "	patronID varchar(16) NOT NULL,"+
                "	assistant_ID varchar(16) NOT NULL,"+
                "	FOREIGN KEY (tcID) REFERENCES TechConsult (tcID)"+
                "		ON DELETE CASCADE,"+
                "	FOREIGN KEY (patronID) REFERENCES Patrons (ID)"+
                "		ON DELETE CASCADE"+
                ")"                
            );
            ps.executeUpdate();
        }  catch (SQLException e) { e.printStackTrace(); }
    }
    
    public void fillTables() {
        // import from https://drive.google.com/open?id=14-YvxM3s_P8XuszpTIbGdTIREDulbbQmm8u59GlqyYw
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Patrons VALUES ( Jesse, Pinkman, S1, Chemistry, American, false, jpink, jpink )"
    //             "INSERT INTO Students VALUES  (S1, 123456789, 123456787, 1511 Graduate Lane, Raleigh, 27606,NC, 1988/Mar/10, Male, Undergraduate, BS, First Year)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try { // Walt Jr.
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Patrons VALUES (Walt Jr., NULL, S2, Chemistry, American, true, wjr, wjr)"
    //             "INSERT INTO Students VALUES (S2, 123456780, 123456781, 1512 Graduate Lane, Raleigh, NC, 27606, 1988/Mar/11, Male, Undergraduate, BS, Second Year)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Patrons VALUES (Gale, Boetticher, S3, Chemistry, Chile, true, gboet, gboet)"
    //             "INSERT INTO Students VALUES (S3, 123456782, 123456783, 1513 Graduate Lane, Raleigh, NC, 27606, 1988/Mar/12, Male, Undergraduate, BS, Third Year)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try { // Gale Boetticher.
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Patrons VALUES (Saul, Goodman, S4, Chemistry, American, false, sgood, sgood)"
    //             "INSERT INTO Students VALUES (S4, 123456784, 123456785, 1514 Graduate Lane, Raleigh, NC, 27606, 1988/Mar/01, Male, Graduate, MS, Second Year)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Patrons VALUES (Walter, White, F1, Chemistry, American, false, wwhite, wwhite)"
    //             "INSERT INTO Faculty VALUES (F1, Professor)"
    //             "INSERT INTO Courses_Faculty (CH101, F1)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try { //Jesse Pinkman
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Patrons VALUES (Gustavo, Fring, F2, Chemistry, American, false, gfring, gfring)"
    //             "INSERT INTO Faculty VALUES (F2, Assistant Professor)"
    //             "INSERT INTO Courses_Faculty (CH102, F2)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try { //Saul Goodman
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Patrons VALUES (Hank, Schrader, F3, Chemistry, American, false, hschrad, hschrad)"
    //             "INSERT INTO Faculty VALUES (F3, Associate Professor)"
    //             "INSERT INTO Courses_Faculty (CH103, F3)"
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Patrons VALUES (Skyler, White, F4, Chemistry, American, false, swhite, swhite)"
    //             "INSERT INTO Faculty VALUES (F4, Professor)"
    //             "INSERT INTO Courses_Faculty (CH104, F4)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Courses VALUES (CH101, B1)"
    //             "INSERT INTO Courses_Students VALUES (CH101, S1)"
    //             "INSERT INTO Courses_Students VALUES (CH101, S2)"
    //             "INSERT INTO Courses_Students VALUES (CH101, S3)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Courses VALUES (CH102, B2)"
    //             "INSERT INTO Courses_Students VALUES (CH101, S2)"
    //             "INSERT INTO Courses_Students VALUES (CH101, S3)"
    //             "INSERT INTO Courses_Students VALUES (CH101, S4)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //      try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Courses VALUES (CH103, B3)"
    //             "INSERT INTO Courses_Students VALUES (CH101, S3)"
    //             "INSERT INTO Courses_Students VALUES (CH101, S4)"
    //             "INSERT INTO Courses_Students VALUES (CH101, S1)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Courses VALUES (CH104, B4)"
    //             "INSERT INTO Courses_Students VALUES (CH101, S1)"
    //             "INSERT INTO Courses_Students VALUES (CH101, S2)"
    //             "INSERT INTO Courses_Students VALUES (CH101, S4)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Publication VALUES (Introduction to Chemistry, B1, true, SK Goyal, 2005)"
    //             "INSERT INTO Books VALUES (1, B1, Pub1)"
    //             "INSERT INTO Hardcopy VALUES (1, B1)"
    //             "INSERT INTO Hardcopy VALUES (2, B1)"
    //             "INSERT INTO Reserved VALUES (B1, CH101, 2016/Aug/08)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Publication VALUES (Introduction to Organic Chemistry, B2, true, HC Verma, 2006)"
    //             "INSERT INTO Books VALUES (2, B2, Pub2)"
    //             "INSERT INTO Hardcopy VALUES (1, B2)"
    //             "INSERT INTO Hardcopy VALUES (2, B2)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Publication VALUES (Introduction to Physical Chemistry, B3, false, Resnick Halliday Walker, 2000)"
    //             "INSERT INTO Books VALUES (3, B3, Pub3)"
    //             "INSERT INTO Hardcopy VALUES (1, B3)"
    //             "INSERT INTO Hardcopy VALUES (2, B3)"
    //             "INSERT INTO Reserved VALUES (B3, CH103, 2016/Aug/08)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Publication VALUES (Introduction to Inorganic Chemistry, B4, false, RC Mukherjee, 2005)
    //             "INSERT INTO Books VALUES (4, B4, Pub4)
    //             "INSERT INTO Hardcopy VALUES (1, B4)
    //             "INSERT INTO Hardcopy VALUES (2, B4)

    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     //Courses
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Publication VALUES (Journal of Web Semantic, J1, true, Roberto Navigli, 2010)
    //             "INSERT INTO Journal VALUES (1, J1)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Publication VALUES (International Journal on Semantic Web and Information, J2, true, Tim Berners Lee, 2011)
    //             "INSERT INTO Journal VALUES (1, J2)
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Publication VALUES (Optimization Techniques for Large Scale Graph Analytics on Map Reduce, C1, true, HyeongSik Kim, 2013)
    //             "INSERT INTO Conf_Proceedings VALUES (C1, WWW)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Publication VALUES (An agglomerative query model for discovery in linked data: semantics and approach, C2, true, Sidan Gao, 2014)
    //             "INSERT INTO Conf_Proceedings VALUES (C2, SIGMOD)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Conference_Rooms VALUES (1, Hunt, 3, 2)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Study_Rooms VALUES (2, Hunt, 3, 3)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Study_Rooms VALUES (3, Hill, 2, 4)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Conference_Rooms VALUES (4, Hunt, 3, 3)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Study_Rooms VALUES (5, Hunt, 3, 4)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Study_Rooms VALUES (6, Hill, 3, 4)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Study_Rooms VALUES (7, Hunt, 2, 2)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Cameras VALUES (CA1, Hunt, Olympus, E-620, 14-42mm lens 1:3.5-5.6, 16G)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     //C4
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Cameras VALUES (CA2, Hunt, Cannon, EOS Rebel T4i, 18-135mm EF-S IS STM Lens, 32G)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Cameras VALUES (CA3, Hunt, Cannon, EOS Rebel T4i, 18-135mm EF-S IS STM Lens, 32G)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History VALUES (S1, B2, 2015/Nov/08 00:00:00, 2015/Nov/13 00:00:00)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History VALUES (S4, B4, 2015/Nov/07 00:00:00, 2015/Nov/11 00:00:00)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     //Books
    //     //B1
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Pub_Check_Out VALUES (S2, B4, 1, 2015/Jul/01, 2015/Aug/08, 488.00)
    //             "INSERT INTO Student_Hold_List VALUES (S2)
    //             "INSERT INTO Resource_Request_History VALUES (S2, B4, 2015/Jul/01 00:00:00, NULL, NULL, NULL)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Pub_Check_Out VALUES (S3, B2, 1, 2015/Oct/01 00:00:00, 2015/Oct/10 00:00:00, 362.00)
    //             "INSERT INTO Student_Hold_List VALUES (S3)
    //             "INSERT INTO Resource_Request_History VALUES (S3, B2, 2015/Oct/01 00:00:00, NULL, NULL, NULL)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History VALUES (F1, R3, 2015/Nov/01 09:00:00, 2015/Nov/01 11:30:00, NULL, NULL)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History VALUES (S1, R5, 2015/Oct/12 15:00:00, 2015/Oct/12 17:00:00, NULL, NULL)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History VALUES (F4, R6, 2015/Nov/02 11:00:00, 2015/Nov/02 13:30:00, NULL, NULL)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     //B2
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History VALUES (F2, R1, 2015/Oct/20 09:00:00, 2015/Oct/20 10:30:00, NULL, NULL)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History VALUES (S3, CA2, 2015/Nov/13 00:00:00, 2015/Nov/19 00:00:00, NULL, NULL)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History VALUES (S2, CA3, 2015/Oct/16 00:00:00, NULL, NULL, NULL)"
    //             "INSERT INTO Camera_Check_Out VALUES (S2, CA3, 2015/Oct/16, 2015/Oct/22, 4080.00)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Media_Rooms VALUES (1, Mini Keyboard, Microphones, Cassette deck, 2)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     //b3
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Media_Rooms VALUES (2, Guitar, Microphones, NULL, 4)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Media_Rooms VALUES (3, 88-key MIDI Keyboard, Microphones, Drum, 6)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Media_Rooms VALUES (4, Drum, Guitar, Cassette deck, 4)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History VALUES (S2, MP1, 2015/Mar/21 09:00:00, NULL, 02:00:00)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History VALUES (S1, MP4, 2015/Mar/12 15:00:00, 2015/Mar/12 17:00:00, 02:00:00)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History VALUES (F4, MP3, 2015/Mar/22 11:00:00, 2015/Mar/22 13:30:00, 01:30:00)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History VALUES (F2, MP1, 2015/Feb/20 09:00:00, 2015/Feb/20 10:30:00, 01:30:00)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Digital_Media_Assistants VALUES (A1, Todd Alquist, Video Editing, Audio Editing, Hunt)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Digital_Media_Assistants VALUES (A2, Hank Schrader, Video Editing, 3D Printing,Hill)"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     //Journals
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Publication ("+
    //             "Journal Of Web Semantic,"+
    //             "J1,"+
    //             "true,"+
    //             "Roberto Navigli,"+
    //             "2010,"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Journals ("+
    //             "J1,"+
    //             "1,"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Publication ("+
    //             "International Journal on Semantic Web and Information,"+
    //             "J2,"+
    //             "true,"+
    //             "Tim Berners Lee,"+
    //             "2011,"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Journals ("+
    //             "J2,"+
    //             "1,"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Publication ("+
    //             "Optimization Techniques for Large Scale Graph Analytics on Map Reduce,"+
    //             "C1,"+
    //             "true,"+
    //             "HyeongSik Kim,"+
    //             "2013,"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Conf_Proceedings ("+
    //             "C1,"+
    //             "WWW,"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }

    //   try{
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Publication ("+
    //             "An agglomerative query model for discovery in linked data: semantics and approach,"+
    //             "C2,"+
    //             "true,"+
    //             "Sidan Gao,"+
    //             "2014,"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Conf_Proceedings ("+
    //             "C2,"+
    //             "SIGMOD,"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Conference_Rooms ("+
  	 //           "1,"+
    //             "Hunt,"+
	   //         "3,"+
	   //         "2"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Study_Rooms ("+
  	 //           "2,"+
    //             "Hunt,"+
	   //         "3,"+
	   //         "3"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Study_Rooms ("+
  	 //           "3,"+
    //             "Hunt,"+
	   //         "2,"+
	   //         "4"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Conference_Rooms ("+
  	 //           "4,"+
    //             "Hunt,"+
	   //         "3,"+
	   //         "3"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Study_Rooms ("+
  	 //           "5,"+
    //             "Hunt,"+
	   //         "3,"+
	   //         "4"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Study_Rooms ("+
  	 //           "6,"+
    //             "Hunt,"+
	   //         "3,"+
	   //         "4"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Study_Rooms ("+
  	 //           "7,"+
    //             "Hunt,"+
	   //         "2,"+
	   //         "2"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     //Cameras
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Cameras ("+
  	 //           "CA1,"+
    //             "Hunt,"+
	   //         "Olympus,"+
	   //         "E-620"+
	   //         "14-42mm lens 1:3.5-5.6,"+
	   //         "16G"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Cameras ("+
  	 //           "CA2,"+
    //             "Hunt,"+
	   //         "Cannon,"+
	   //         "EOS Rebel T4i"+
	   //         "18-135mm EF-S IS STM Lens,"+
	   //         "32G"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Cameras ("+
  	 //           "CA3,"+
    //             "Hunt,"+
	   //         "Cannon,"+
	   //         "EOS Rebel T4i"+
	   //         "18-135mm EF-S IS STM Lens,"+
	   //         "32G"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History ("+
    //             "S1,"+
    //             "B2,"+
    //             "2015-11-08 00:00:00,"+
    //             "2015-11-13 00:00:00,"+
    //             "NULL,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History ("+
    //             "S4,"+
    //             "B4,"+
    //             "2015-11-07 00:00:00,"+
    //             "2015-11-11 00:00:00,"+
    //             "NULL,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) {   e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Pub_Check_Out ("+
    //             "S2,"+
    //             "B4,"+
    //             "2015-07-01 00:00:00,"+
    //             "2015-08-08 00:00:00,"+
    //             "488.00"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Student_Hold_List ("+
    //             "S2"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO  Resource_Request_History ("+
    //             "S2,"+
    //             "B4,"+
    //             "2015-07-01 00:00:00,"+
    //             "NULL,"+
    //             "NULL,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Pub_Check_Out ("+
    //             "S3,"+
    //             "B2,"+
    //             "2015-10-01 00:00:00,"+
    //             "2015-10-10 00:00:00,"+
    //             "362.00"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Student_Hold_List ("+
    //             "S3"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO  Resource_Request_History ("+
    //             "S3,"+
    //             "B2,"+
    //             "2015-10-01 00:00:00,"+
    //             "NULL,"+
    //             "NULL,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     //Room Reservations
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO  Resource_Request_History ("+
    //             "F1,"+
    //             "R3,"+
    //             "2015-11-01 09:00:00,"+
    //             "2015-11-01 11:30:00,"+
    //             "NULL,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO  Resource_Request_History ("+
    //             "S1,"+
    //             "R5,"+
    //             "2015-11-12 15:00:00,"+
    //             "2015-11-12 17:30:00,"+
    //             "NULL,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO  Resource_Request_History ("+
    //             "F4,"+
    //             "R6,"+
    //             "2015-11-02 11:00:00,"+
    //             "2015-11-02 13:30:00,"+
    //             "NULL,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO  Resource_Request_History ("+
    //             "F2,"+
    //             "R1,"+
    //             "2015-10-20 9:00:00,"+
    //             "2015-10-20 10:30:00,"+
    //             "NULL,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO  Resource_Request_History ("+
    //             "S1,"+
    //             "CA1,"+
    //             "2015-10-30 00:00:00,"+
    //             "NULL,"+
    //             "NULL,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Camera_Check_Out ("+
    //             "S1,"+
    //             "CA1,"+
    //             "2015-10-30 00:00:00,"+
    //             "2015-11-05 00:00:00,"+
    //             "3960.00,"+
    //             "NULL,"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO  Resource_Request_History ("+
    //             "S2,"+
    //             "CA3,"+
    //             "2015-10-16 00:00:00,"+
    //             "NULL,"+
    //             "NULL,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Camera_Check_Out ("+
    //             "S2,"+
    //             "CA3,"+
    //             "2015-10-16 00:00:00,"+
    //             "2015-10-22 00:00:00,"+
    //             "4080.00,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     //FEES---------------------------------------------
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Fees ("+
    //             "S2,"+
    //             "Camera,"+
    //             "4080,"+
    //             "F1"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Fees ("+
    //             "S1,"+
    //             "Camera,"+
    //             "3960,"+
    //             "F2"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Fees ("+
    //             "S3,"+
    //             "Book,"+
    //             "362.00,"+
    //             "F3"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Fees ("+
    //             "S2,"+
    //             "Book,"+
    //             "488,"+
    //             "F4"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     //Media Rooms
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Media_Rooms ("+
    //             "1,"+
    //             "Mini Keyboard,"+
    //             "Microphones,"+
    //             "Cassete Deck,"+
    //             "2,"+
    //             "MP1,"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Media_Rooms ("+
    //             "2,"+
    //             "Guitar,"+
    //             "Microphones,"+
    //             "NULL,"+
    //             "4,"+
    //             "MP2"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Media_Rooms ("+
    //             "3,"+
    //             "88-Key MIDI Keyboard,"+
    //             "Microphones,"+
    //             "Drum,"+
    //             "6,"+
    //             "MP3"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Media_Rooms ("+
    //             "4,"+
    //             "Drum,"+
    //             "Guitar,"+
    //             "Cassete Deck,"+
    //             "4,"+
    //             "MP4"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History ("+
    //             "S2,"+
    //             "MP1,"+
    //             "2015-03-21 09:00:00,"+
    //             "NULL,"+
    //             "02:00:00,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History ("+
    //             "S1,"+
    //             "MP4,"+
    //             "2015-03-12 17:00:00,"+
    //             "NULL,"+
    //             "02:00:00,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History ("+
    //             "F4,"+
    //             "MP3,"+
    //             "2015-03-22 11:00:00,"+
    //             "2015-03-22 13:00:00,"+
    //             "01:30:00,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History ("+
    //             "F2,"+
    //             "MP1,"+
    //             "2015-02-20 09:00:00,"+
    //             "2015-02-20 10:30:00,"+
    //             "01:30:00,"+
    //             "NULL"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Digital_Media_Assistants ("+
    //             "A1,"+
    //             "Todd Alquist,"+
    //             "Video Editing,"+
    //             "Audio Editing,"+
    //             "NULL,"+
    //             "Hunt"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Digital_Media_Assistants ("+
    //             "A2,"+
    //             "Hank Schrader,"+
    //             "Video Editing,"+
    //             "3D Printing,"+
    //             "Audio Editing,"+
    //             "Hill"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Digital_Media_Assistants ("+
    //             "A3,"+
    //             "Mary Schrader,"+
    //             "3D Printing,"+
    //             "Audio Editing,"+
    //             "NULL,"+
    //             "Hill"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
    //     try {
            
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Tech_Consult_Log ("+
    //             "TCL1,"+
    //             "S1,"+
    //             "false,"+
    //             "Hunt,"+
    //             "NULL,"+
    //             "2016-01-20 17:00:00,"+
    //             "2016-01-20 18:00:00,"+
    //             "A3,"+
    //             "3D Printing"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
    //     try {
    //         Statement s = con.createStatement();
    //         s.executeUpdate(
    //             "INSERT INTO Resource_Request_History ("+
    //             "S1,"+
    //             "NULL,"+
    //             "2016-01-20 17:00:00,"+
    //             "2016-01-20 18:00:00,"+
    //             "01:00:00,"+
    //             "A3"+
    //             ")"
    //         );
    //     } catch (SQLException e) { e.printStackTrace(); }
        
        Statement stmnt;
        String[] inserts = 
        {
            "INSERT INTO Patrons VALUES ('Jesse', 'Pinkman', 'S1', 'Chemistry', 'American', 'false', 'jpink', 'jpink')",
            "INSERT INTO Students VALUES  ('S1', '123456789', '123456787', '1511 Graduate Lane', 'Raleigh', '27606,NC', '1988/Mar/10', 'Male', 'Undergraduate', 'BS', 'First Year')",
            "INSERT INTO Patrons VALUES ('Walt Jr.', 'NULL', 'S2', 'Chemistry', 'American', 'true', 'wjr', 'wjr')",
            "INSERT INTO Students VALUES ('S2', '123456780', '123456781', '1512 Graduate Lane', 'Raleigh', 'NC', '27606', '1988/Mar/11', 'Male', 'Undergraduate', 'BS', 'Second Year')",
            "INSERT INTO Patrons VALUES ('Gale', 'Boetticher', 'S3', 'Chemistry', 'Chile', 'true', 'gboet', 'gboet')",
            "INSERT INTO Students VALUES ('S3', '123456782', '123456783', '1513 Graduate Lane', 'Raleigh', 'NC', '27606', '1988/Mar/12', 'Male', 'Undergraduate', 'BS', 'Third Year')",
            "INSERT INTO Patrons VALUES ('Saul', 'Goodman', 'S4', 'Chemistry', 'American', 'false', 'sgood', 'sgood')",
            "INSERT INTO Students VALUES ('S4', '123456784', '123456785', '1514 Graduate Lane', 'Raleigh', 'NC', '27606', '1988/Mar/01', 'Male', 'Graduate', 'MS', 'Second Year')",
            "INSERT INTO Patrons VALUES ('Walter', 'White', 'F1', 'Chemistry', 'American', 'false', 'wwhite', 'wwhite')",
            "INSERT INTO Faculty VALUES ('F1', 'Professor')",
            "INSERT INTO Courses_Faculty ('CH101', 'F1')",
            "INSERT INTO Patrons VALUES ('Gustavo', 'Fring', 'F2', 'Chemistry', 'American', 'false', 'gfring', 'gfring')",
            "INSERT INTO Faculty VALUES ('F2', 'Assistant Professor')",
            "INSERT INTO Patrons VALUES ('Hank', 'Schrader', 'F3', 'Chemistry', 'American', 'false', 'hschrad', 'hschrad')",
            "INSERT INTO Faculty VALUES ('F3', 'Associate Professor')",
            "INSERT INTO Courses_Faculty ('CH103', 'F3')",
            "INSERT INTO Patrons VALUES ('Skyler', 'White', 'F4', 'Chemistry', 'American', 'false', 'swhite', 'swhite')",
            "INSERT INTO Faculty VALUES ('F4', 'Professor')",
            "INSERT INTO Courses_Faculty ('CH104', 'F4')",
            "INSERT INTO Courses VALUES ('CH101', 'B1')",
            "INSERT INTO Courses_Students VALUES ('CH101', 'S1')",
            "INSERT INTO Courses_Students VALUES ('CH101', 'S2')",
            "INSERT INTO Courses_Students VALUES ('CH101', 'S3')",
            "INSERT INTO Courses VALUES ('CH102', 'B2')",
            "INSERT INTO Courses_Students VALUES ('CH101', 'S2')",
            "INSERT INTO Courses_Students VALUES ('CH101', 'S3')",
            "INSERT INTO Courses_Students VALUES ('CH101', 'S4')",
            "INSERT INTO Courses VALUES ('CH103', 'B3')",
            "INSERT INTO Courses_Students VALUES ('CH101', 'S3')",
            "INSERT INTO Courses_Students VALUES ('CH101', 'S4')",
            "INSERT INTO Courses_Students VALUES ('CH101', 'S1')",
            "INSERT INTO Courses VALUES ('CH104', 'B4')",
            "INSERT INTO Courses_Students VALUES ('CH101', 'S1')",
            "INSERT INTO Courses_Students VALUES ('CH101', 'S2')",
            "INSERT INTO Courses_Students VALUES ('CH101', 'S4')",
            "INSERT INTO Publication VALUES ('Introduction to Chemistry', 'B1', 'true', 'SK Goyal', '2005')",
            "INSERT INTO Books VALUES ('1', 'B1', 'Pub1')",
            "INSERT INTO Hardcopy VALUES ('1', 'B1')",
            "INSERT INTO Hardcopy VALUES ('2', 'B1')",
            "INSERT INTO Reserved VALUES ('B1', 'CH101', '2016/Aug/08')",
            "INSERT INTO Publication VALUES ('Introduction to Organic Chemistry', 'B2', 'true', 'HC Verma', '2006')",
            "INSERT INTO Books VALUES ('2', 'B2', 'Pub2')",
            "INSERT INTO Hardcopy VALUES ('1', 'B2')",
            "INSERT INTO Hardcopy VALUES ('2', 'B2')",
            "INSERT INTO Publication VALUES ('Introduction to Physical Chemistry', 'B3', 'false', 'Resnick Halliday Walker', '2000')",
            "INSERT INTO Books VALUES ('3', 'B3', 'Pub3')",
            "INSERT INTO Hardcopy VALUES ('1', 'B3')",
            "INSERT INTO Hardcopy VALUES ('2', 'B3')",
            "INSERT INTO Reserved VALUES ('B3', 'CH103', '2016/Aug/08')",
            "INSERT INTO Publication VALUES ('Introduction to Inorganic Chemistry', 'B4', 'false', 'RC Mukherjee', '2005')",
            "INSERT INTO Books VALUES ('4', 'B4', 'Pub4')",
            "INSERT INTO Hardcopy VALUES ('1', 'B4')",
            "INSERT INTO Hardcopy VALUES ('2', 'B4')",
            "INSERT INTO Publication VALUES ('Journal of Web Semantic', 'J1', 'true', 'Roberto Navigli', '2010')",
            "INSERT INTO Journal VALUES ('1', 'J1')",
            "INSERT INTO Publication VALUES ('International Journal on Semantic Web and Information', 'J2', 'true', 'Tim Berners Lee', '2011')",
            "INSERT INTO Journal VALUES ('1', 'J2')",
            "INSERT INTO Publication VALUES ('Optimization Techniques for Large Scale Graph Analytics on Map Reduce', 'C1', 'true', 'HyeongSik Kim', '2013')",
            "INSERT INTO Conf_Proceedings VALUES ('C1', 'WWW')",
            "INSERT INTO Publication VALUES ('An agglomerative query model for discovery in linked data: semantics and approach', 'C2', 'true', 'Sidan Gao', '2014')",
            "INSERT INTO Conf_Proceedings VALUES ('C2', 'SIGMOD')",
            "INSERT INTO Conference_Rooms VALUES ('1', 'Hunt', '3', '2')",
            "INSERT INTO Study_Rooms VALUES ('2', 'Hunt', '3', '3')",
            "INSERT INTO Study_Rooms VALUES ('3', 'Hill', '2', '4')",
            "INSERT INTO Conference_Rooms VALUES ('4', 'Hunt', '3', '3')",
            "INSERT INTO Study_Rooms VALUES ('5', 'Hunt', '3', '4')",
            "INSERT INTO Study_Rooms VALUES ('6', 'Hill', '3', '4')",
            "INSERT INTO Study_Rooms VALUES ('7', 'Hunt', '2', '2')",
            "INSERT INTO Cameras VALUES ('CA1', 'Hunt', 'Olympus', 'E-620', '14-42mm lens 1:3.5-5.6', '16G')",
            "INSERT INTO Cameras VALUES ('CA2', 'Hunt', 'Cannon', 'EOS Rebel T4i', '18-135mm EF-S IS STM Lens', '32G')INSERT INTO Cameras VALUES ('CA3', 'Hunt', 'Cannon', 'EOS Rebel T4i', '18-135mm EF-S IS STM Lens', '32G')",
            "INSERT INTO Resource_Request_History VALUES ('S1', 'B2', '2015/Nov/08 00:00:00', '2015/Nov/13 00:00:00')",
            "INSERT INTO Resource_Request_History VALUES ('S4', 'B4', '2015/Nov/07 00:00:00', '2015/Nov/11 00:00:00')",
            "INSERT INTO Pub_Check_Out VALUES ('S2', 'B4', '1', '2015/Jul/01', '2015/Aug/08', '488.00')",
            "INSERT INTO Student_Hold_List VALUES ('S2')",
            "INSERT INTO Resource_Request_History VALUES ('S2', 'B4', '2015/Jul/01 00:00:00', 'NULL', 'NULL', 'NULL')",
            "INSERT INTO Pub_Check_Out VALUES ('S3', 'B2', '1', '2015/Oct/01 00:00:00', '2015/Oct/10 00:00:00', '362.00')",
            "INSERT INTO Student_Hold_List VALUES ('S3')",
            "INSERT INTO Resource_Request_History VALUES ('S3', 'B2', '2015/Oct/01 00:00:00', 'NULL', 'NULL', 'NULL')",
            "INSERT INTO Resource_Request_History VALUES ('F1', 'R3', '2015/Nov/01 09:00:00', '2015/Nov/01 11:30:00', 'NULL', 'NULL')",
            "INSERT INTO Resource_Request_History VALUES ('S1', 'R5', '2015/Oct/12 15:00:00', '2015/Oct/12 17:00:00', 'NULL', 'NULL')",
            "INSERT INTO Resource_Request_History VALUES ('F4', 'R6', '2015/Nov/02 11:00:00', '2015/Nov/02 13:30:00', 'NULL', 'NULL')",
            "INSERT INTO Resource_Request_History VALUES ('F2', 'R1', '2015/Oct/20 09:00:00', '2015/Oct/20 10:30:00', 'NULL', 'NULL')",
            "INSERT INTO Resource_Request_History VALUES ('S3', 'CA2', '2015/Nov/13 00:00:00', '2015/Nov/19 00:00:00', 'NULL', 'NULL')",
            "INSERT INTO Resource_Request_History VALUES ('S1', 'CA1', '2015/Oct/13 00:00:00', 'NULL', 'NULL', 'NULL')",
            "INSERT INTO Camera_Check_Out VALUES ('S1', 'CA1', '2015/Nov/13', '2015/Nov/05', '3960.00')",
            "INSERT INTO Resource_Request_History VALUES ('S2', 'CA3', '2015/Oct/16 00:00:00', 'NULL', 'NULL', 'NULL')",
            "INSERT INTO Camera_Check_Out VALUES ('S2', 'CA3', '2015/Oct/16', '2015/Oct/22', '4080.00')",
            "INSERT INTO Media_Rooms VALUES ('1', 'Mini Keyboard', 'Microphones', 'Cassette deck', '2')",
            "INSERT INTO Media_Rooms VALUES ('2', 'Guitar', 'Microphones', 'NULL', '4')",
            "INSERT INTO Media_Rooms VALUES ('3', '88-key MIDI Keyboard', 'Microphones', 'Drum', '6')",
            "INSERT INTO Media_Rooms VALUES ('4', 'Drum', 'Guitar', 'Cassette deck', '4')",
            "INSERT INTO Resource_Request_History VALUES ('S2', 'MP1', '2015/Mar/21 09:00:00', 'NULL', '02:00:00')",
            "INSERT INTO Resource_Request_History VALUES ('S1', 'MP4', '2015/Mar/12 15:00:00', '2015/Mar/12 17:00:00', '02:00:00')",
            "INSERT INTO Resource_Request_History VALUES ('F4', 'MP3', '2015/Mar/22 11:00:00', '2015/Mar/22 13:30:00', '01:30:00')",
            "INSERT INTO Resource_Request_History VALUES ('F2', 'MP1', '2015/Feb/20 09:00:00', '2015/Feb/20 10:30:00', '01:30:00')",
            "INSERT INTO Digital_Media_Assistants VALUES ('A1', 'Todd Alquist', 'Video Editing', 'Audio Editing', 'Hunt')",
            "INSERT INTO Digital_Media_Assistants VALUES ('A2', 'Hank Schrader', 'Video Editing', '3D Printing,Hill')",
            "INSERT INTO Digital_Media_Assistants VALUES ('A3', 'Mary Schrader', '3D Printing', 'Audio Editing,Hill')",
            "INSERT INTO Tech_Consult VALUES ('S1', 'false', 'Hunt', 'NULL', '2016/Jan/20 17:00:00', '2016/Jan/20 18:00:00', 'A3', '3D Printing', '', '', ',')",
            "INSERT INTO Resource_Request_History VALUES ('S1', 'NULL', '2016/Jan/20 17:00:00', '2016/Jan/20 18:00:00', 'NULL', '0')"
        };
        for (int i = 0; i < inserts.length; i++) {
            try {
                stmnt = con.createStatement();
                stmnt.executeUpdate(inserts[i]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        
    }
    
    // http://www.tutorialspoint.com/jdbc/jdbc-batch-processing.htm
    public void deleteTables() {
        try {
            Statement stmnt;
            String[] drops = 
            {
                "DROP TABLE Books CASCADE CONSTRAINTS",
                "DROP TABLE Digital_Media_Assistants CASCADE CONSTRAINTS",
                "DROP TABLE Camera_Check_Out CASCADE CONSTRAINTS",
                "DROP TABLE Camera_Wait_Queue CASCADE CONSTRAINTS",
                "DROP TABLE Cameras CASCADE CONSTRAINTS",
                "DROP TABLE Conf_Booked CASCADE CONSTRAINTS",
                "DROP TABLE Conf_Proceedings CASCADE CONSTRAINTS",
                "DROP TABLE Conference_Rooms CASCADE CONSTRAINTS",
                "DROP TABLE Courses CASCADE CONSTRAINTS",
                "DROP TABLE Faculty CASCADE CONSTRAINTS",
                "DROP TABLE Fees CASCADE CONSTRAINTS",
                "DROP TABLE Hardcopy CASCADE CONSTRAINTS",
                "DROP TABLE Journal CASCADE CONSTRAINTS",
                "DROP TABLE Media_Rooms CASCADE CONSTRAINTS",
                "DROP TABLE Media_Booked CASCADE CONSTRAINTS",
                "DROP TABLE Messages CASCADE CONSTRAINTS",
                "DROP TABLE Patrons CASCADE CONSTRAINTS",
                "DROP TABLE Pub_Check_Out CASCADE CONSTRAINTS",
                "DROP TABLE Publication CASCADE CONSTRAINTS",
                "DROP TABLE Publication_Wait_Queue_Fac CASCADE CONSTRAINTS",
                "DROP TABLE Publication_Wait_Queue_Stud CASCADE CONSTRAINTS",
                "DROP TABLE Reserved CASCADE CONSTRAINTS",
                "DROP TABLE Resource_Request_History CASCADE CONSTRAINTS",
                "DROP TABLE Student_Hold_List CASCADE CONSTRAINTS",
                "DROP TABLE Students CASCADE CONSTRAINTS",
                "DROP TABLE Study_Booked CASCADE CONSTRAINTS",
                "DROP TABLE Study_Rooms CASCADE CONSTRAINTS",
                "DROP TABLE Tech_Consult CASCADE CONSTRAINTS",
                "DROP TABLE Tech_Consultation_Log CASCADE CONSTRAINTS",
                "DROP TABLE Tech_Reservation CASCADE CONSTRAINTS",
                "DROP TABLE Courses_Students CASCADE CONSTRAINTS",
                "DROP TABLE Courses_Faculty CASCADE CONSTRAINTS"
            };
            for (int i = 0; i < drops.length; i++) {
                try {
                    stmnt = con.createStatement();
                    stmnt.executeUpdate(drops[i]);
                } catch (SQLSyntaxErrorException e) {
                    System.out.println("threw SQLSyntaxErrorException buts that probably because   " + drops[i] + "   doesn't exist");
                }
            }
        } catch (SQLException e) { 
            e.printStackTrace(); 
        }
    }
    
    // Have to make this dumb thing because System.out.println takes too long to type
    private void sop(Object obj) {
    	System.out.println(obj);
    }
}