import java.sql.*;
import java.util.*;

// use to interact with the database using jdbc
class DBInteraction {
    
    Connection con = null;
    
    public DBInteraction(Connection con) {
        this.con = con;
    }
    
    // return if the user is in the database
    // Return the users ID
    public String isUser(String username, String password) {
        String user_name = "";
        try{
            String selectSQL = "SELECT user_name FROM Patrons WHERE user_name = ?";
            PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
            preparedStatement.setString(1, username);
            ResultSet rs = preparedStatement.executeQuery(selectSQL );
            while (rs.next()) {
            	user_name = rs.getString("user_name");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return user_name;
    }
    
    // print profile details
    // return the string as it's displayed to the user
    public String printProfile(String id) {
        String rtn = "";
        try {
            DatabaseMetaData md = con.getMetaData();
            
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM Patrons WHERE ID = ?"
            );
            
            // TODO need to join with the student and faculty table
            
            ps.setString(1, id);
            ResultSet rsVal = ps.executeQuery();
            ResultSet rsName = md.getColumns(null, null, "Patrons", null);
            
            // construct the string to print names and values
            int i = 0;
            while(rsVal.next() && rsName.next()) {
                rtn += rsName.getString(i) + "     " + rsVal.getString(i) + "\n";
                i++;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return rtn;
    }
    
    // edit a profile attribute
    public Boolean updateProfile(String id, String attribute, Object value) {
        return true;
    }
    
    // list all publications
    // On selecting the 'Publications' option, the list of all publications (including books, ebooks, journals and conference proceedings) will be displayed
    // return the string as it's displayed to the user
    public String listPublications() {
        String result = "";
        try{
            String selectSQL = "SELECT title FROM Publication";
            PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
            ResultSet rs = preparedStatement.executeQuery(selectSQL );
            while (rs.next()) {
            	result += "\n"+rs.getString("title");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return result;        
    }
    
    // find if the publication exists
    public Boolean hasPublication(String id) {
        int count = 0;
        try{
            String selectSQL = "SELECT count(*) FROM Publication WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(selectSQL);
            ps.setString(1, id);
            PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
            ResultSet rs = preparedStatement.executeQuery(selectSQL );
            while (rs.next()) {
                count = rs.getInt("count");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        if( count > 0){
            return true;
        } else {
            return false;  
        }
    }
    
    // get publiction info
    // return the string as it's displayed to the user
    public String printPublicationInfo(String id) {
        String title = "";
        String author = "";
        String pub_year = "";
        String result = "";
        String publisher = "";
        String edition = "";
        try{
            String selectSQL = "SELECT * FROM Publication WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(selectSQL);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery(selectSQL );
            while (rs.next()) {
            	title = rs.getString("title");
            	author = rs.getString("author");
            	pub_year = rs.getString("pub_year");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        try{
            String selectSQL = "SELECT * FROM Books WHERE ISBN = ?";
            PreparedStatement ps = con.prepareStatement(selectSQL);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery(selectSQL );
            while (rs.next()) {
            	publisher = rs.getString("publisher");
            	edition = rs.getString("edition");
            }
            if( !publisher.equals("") ){
                result = title+" "+author+" "+edition+ " " +publisher+" "+pub_year;
                return result;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        String copy_number = "";
        try{
            String selectSQL = "SELECT * FROM Journal WHERE ISSN = ?";
            PreparedStatement ps = con.prepareStatement(selectSQL);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery(selectSQL );
            while (rs.next()) {
            	copy_number += rs.getString("copy_number");
            }
            if( !copy_number.equals("") ){
                result = title+" "+author+" "+copy_number+" "+pub_year;
                return result;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        String conf_number = "";
        String conf_name = "";
        try{
            String selectSQL = "SELECT * FROM Conf_Proceedings WHERE conf_number = ?";
            PreparedStatement ps = con.prepareStatement(selectSQL);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery(selectSQL );
            while (rs.next()) {
            	conf_number = rs.getString("conf_number");
            	conf_name = rs.getString("conf_name");
            }
            if( !conf_number.equals("") ){
                result = title+" "+author+" "+conf_name+" "+conf_number + " " +pub_year;
                return result;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return result;
        //Meh I'm sure this will probably work.
    }
    
    // see if the publication is checkout out by the current user
    public Boolean isPublicationCheckedOutBy(String userid, String pubid) {
        int count = 0;
        try{
            String selectSQL = "SELECT * FROM Publication WHERE ID = ?";
            PreparedStatement ps = con.prepareStatement(selectSQL);
            ps.setString(1, pubid);
            ResultSet rs = ps.executeQuery(selectSQL );
            while (rs.next()) {
            	count = +rs.getInt("count");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        if( count > 0){
            return true;
        } else {
            return false;
        }
    }
    
    /*
    Any requests which violate any of the constraints mentioned in the description should not be entertained. 
    If the student requests for a publication that is not currently available, the request should be added to a waitlist 
    and an appropriate message should be displayed. If the requested publication is available, details like checkout date/time, 
    return date/time should be taken as input (refer to the description).If the request to issue a book is granted
    (refer to the constraints on various publications), then the status of the book (issued, waitlisted) should be updated.
    also handle the case if the user is renewing or requesting
    
    Assume that checkout time always starts at the time the user requests the item
    https://moodle1516-courses.wolfware.ncsu.edu/mod/forum/discuss.php?d=239661 
    
    Don't forget to account for reserved books
    
    A patron is only allowed to have one copy of particular publication checked out, at a time.
    
    Students belonging to the hold should not be allowed any further check outs until the outstanding dues are cleared
    
    From the Project Description stated below
    Checkout and Return Policy
    The checkout process is initiated by the patron (after finding the desired item(s)). The checkout process updates the 
    appropriate tables in the database with respect to availability and assigns return dates to items based on relevant rules 
    for the type of holding and type of patron (mentioned throughout the description). Checkout should only be possible 
    for available items. Additional requests for items not currently available will be placed on a wait queue and serviced on a 
    first-come-first-serve based. However, faculty always have priority on the wait queue. Checked out items can be renewed by a 
    patron only if no patrons are waiting for item. If it is possible to renew, the due dates and relevant data are updated in database.
    
    faculty differences https://github.ncsu.edu/cblupo/csc440library/issues/4
    
    rs.isBeforeFirst() means at least one tuple is returned
    */
    public void checkoutPublication(String userid, String pubid, Timestamp outDate, Timestamp inDate, boolean isStudent) {
        // TODO maybe handle situation where people waiting for a book automatically get it checkout out to them when it's returned (removed from the wait queue)
        // TODO ask the user for electronic or not
        // 1 also check hold list
        try {
            PreparedStatement psHold = con.prepareStatement("SELECT COUNT(ID) FROM Student_Hold_List WHERE ID=?");
            psHold.setString(1, userid);
            ResultSet rsHold = psHold.executeQuery();
            if (rsHold.isBeforeFirst()) {
                
                // 2 A patron is only allowed to have one copy of particular publication checked out, at a time.
                PreparedStatement psCO = con.prepareStatement("SELECT * FROM Pub_Check_Out WHERE patron_ID=? AND pub_ID=? AND (check_out_date<=? AND due_date>=?)");
                psCO.setString(1, userid);
                psCO.setString(2, pubid);
                psCO.setTimestamp(3, inDate);
                psCO.setTimestamp(4, outDate);
                ResultSet rsCO = psCO.executeQuery();
                //check for results
                if(!rsCO.isBeforeFirst()){
                    // publication is checked out
                    // 3 check if the book is on reserve and if the student is able to access it, irrelevant if faculty
                    if(isStudent){
                        // since student and thi
                        PreparedStatement psRes = con.prepareStatement("SELECT isbn, course FROM Reserved WHERE isbn=?");
                        psRes.setString(1, pubid);
                        ResultSet rsRes = psRes.executeQuery();
                        if (rsRes.isBeforeFirst()) {
                            // book is reserved
                            // check if student has rights to this
                            String course = rsRes.getString("course");
                            PreparedStatement psCours = con.prepareStatement("SELECT * FROM Courses_Students WHERE patron_ID=? AND course_ID=?");
                            psCours.setString(1, userid);
                            psCours.setString(2, course);
                            ResultSet rsCours = psCours.executeQuery();
                                // student doesn't have rights to this book
                                sop("You don't have access to this book");
                                return;
                            }
                        }
                    // 4 check if pub is checked out first
                    PreparedStatement psChecked = con.prepareStatement("SELECT H.copy_num FROM Pub_Check_Out P, Hardcopy H "+
                                                                       "WHERE P.pub_ID=H.ISBN AND P.pub_ID=? AND (P.check_out_date<=? AND P.due_date>=?)");
                    psChecked.setString(1, pubid);
                    psChecked.setTimestamp(2, inDate);
                    psChecked.setTimestamp(3, outDate);
                    ResultSet rsChecked = psChecked.executeQuery();
                    // get list of copy numbers that are currently checked out
                    ArrayList<Integer> out = new ArrayList<Integer>();
                    while(rsChecked.next()){
                        out.add(out.size(), rsChecked.getInt(1));
                    }
                    
                    psChecked = con.prepareStatement("SELECT copy_num FROM Hardcopy WHERE ISBN=?");
                    psChecked.setString(1, pubid);
                    rsChecked = psChecked.executeQuery();
                    // get list of all copies
                    ArrayList<Integer> all = new ArrayList<Integer>();
                    while(rsChecked.next()){
                        all.add(all.size(), rsChecked.getInt(1));
                    }
                    
                    // remove all checked out copies
                    for (int x = 0; x < out.size(); x++){
                        all.remove(out.get(x));
                    }
                    // check if copies are left
                    // need to capture if that checkout book was checkoued out by 'userid' cause of the else blcok
                    if(all.size() > 0){
                        // copies are available
                        PreparedStatement psCheckOut = con.prepareStatement("INSERT INTO Pub_Check_Out VALUES(?, ?, ?, ?, ?)");
                        psCheckOut.setString(1, userid);
                        psCheckOut.setString(2, pubid);
                        psCheckOut.setInt(3, all.get(0));
                        psCheckOut.setTimestamp(4, outDate);
                        psCheckOut.setTimestamp(5, inDate);
                        psCheckOut.executeUpdate();
                        sop("You have requested the publication!");
                        return;
                    } else {
                        // copies are not available, it's checkout out
                        // put put on wait  queue
                        // students and faculty go on different wait queues
                            
                        // see the requestCameraReservation() implimentation
                        String selectSQL;
                        PreparedStatement ps;
                        if (isStudent) {
                            selectSQL = "SELECT MAX(request_position) FROM Publication_Wait_Stud_Stud WHERE pub_ID=? GROUPBY pub_ID";
                        } else {
                            selectSQL = "SELECT MAX(request_position) FROM Publication_Wait_Stud_Fac WHERE pub_ID=? GROUPBY pub_ID";
                        }
                        ps = con.prepareStatement(selectSQL);
                        ps.setString(1, pubid);
                        ResultSet rs = ps.executeQuery();
                        
                        // check for result
                        int place = 0;
                        if(!rs.isBeforeFirst()){
                            // enter at place 1
                            place = 1;
                        } else {
                            // enter at next place
                            place = rs.getInt(1) + 1;
                        }
                        
                        // insert to waitlist
                        String insertSQL;
                        if (isStudent) {
                            insertSQL = "INSERT INTO Publication_Wait_Queue_Stud VALUES (?, ?, ?)";
                        } else {
                            insertSQL = "INSERT INTO Publication_Wait_Queue_Fac VALUES (?, ?, ?)";
                        }
                        ps = con.prepareStatement(insertSQL);
                        ps.setString(1, userid);
                        ps.setString(2, pubid);
                        ps.setInt(3, place);
                        ps.executeUpdate();
                        
                        sop("That publication is already checked out");
                        return;
                    }
                } else {
                    sop("You already have that publication checked out");
                    // TODO prompt them to renew it 
                    renewCheckedOutResource(userid, pubid);
                    return;
                }
            } else {
                sop("you're on the hold list");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        // TODO PUT ON RESOURCE REAUIEST HISTORYU 
    }
    
    // List of resources currently checked out by the user.
    public String printCheckedOutResources(String userid) {
        String rtn = "";
        
        try{
            // publications
            rtn += "Publications";
            PreparedStatement psPub = con.prepareStatement("SELECT * FROM Pub_Check_Out WHERE patron_ID=?");
            psPub.setString(1, userid);
            ResultSet rsPub = psPub.executeQuery();
            while(rsPub.next()){
                rtn += rsPub.getString(1) + "\n";
            }
            
            // cameras
            rtn += "Cameras";
            PreparedStatement psCam = con.prepareStatement("SELECT * FROM Camera_Check_Out WEHRE patron_ID=?");
            psCam.setString(1, userid);
            ResultSet rsCam = psCam.executeQuery();
            while(rsCam.next()){
                rtn += rsCam.getString(1) + "\n";
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        return rtn;
    }
    
    // For a particular resource, its details, check-out date/time, due date/time should be displayed
    public String printResourceDetails(String userid, String resourceid){
        String rtn = "";
        
        try{
            if(resourceid.charAt(1) == 'C' && resourceid.charAt(2) == 'A'){
                // camera
                rtn += printCameraDetails(resourceid);
            } else {
                // publication
                PreparedStatement ps = con.prepareStatement("SELECT * FROM Publication P, Pub_Check_Out CO WHERE P.ID=? AND P.ID=CO.pub_ID");
                ps.setString(1, "resourceid");
                ResultSet rs = ps.executeQuery();
                
                if(!rs.isBeforeFirst()){
                    rtn = "Invalid ID";
                } else {
                    String id = "";
                    String title = "";
                    String authors = "";
                    String year = "";
                    Timestamp checkOutDate;
                    Timestamp dueDate;
                    
                    while(rs.next()){
                        id = rs.getString("P.ID");
                        title = rs.getString("P.title");
                        authors = rs.getString("P.authors");
                        year = rs.getString("P.pub_year");
                        checkOutDate = rs.getTimestamp("CO.check_out_date");
                        dueDate = rs.getTimestamp("CO.due_date");
                        
                        rtn += id + " \t" + title + " \t" + authors + " \t" + year + " \n";
                        rtn += "Check Out Date \tDue Date\n";
                        rtn += checkOutDate + " \t" + dueDate + "\n";
                    }
                }
            }
        } catch(SQLException e) { e.printStackTrace(); };
        
        return rtn;
    }
    
    // Renews the check out for the particular resource
    // There is no limit to the number of times a publication can be renewed. Also, it can be renewed for the same amount of time as it can be issued for in the first place
    // https://moodle1516-courses.wolfware.ncsu.edu/mod/forum/discuss.php?d=243748
    public void renewCheckedOutResource(String userid, String resourceid){
        //IMPLEMENT
        // >>>> Is this the same as just pushing the due date farther back?
        try{
            if(resourceid.charAt(1) == 'C' && resourceid.charAt(2) == 'A'){
                // camera
                PreparedStatement ps = con.prepareStatement("TEMP");
                ps.executeQuery();
            } else {
                // publication

            }
        } catch(SQLException e) { e.printStackTrace(); };
    }
    
    // The option 'Resource Request' will display the list of resources requested by the user. 
    // resources are Publications, Conference/Study/Media-production rooms, Technology Consultation, and Cameras
    // from forum, Resource request basically just shows a details of all the resources requested in the past as well as future reservations of that patron
    // return the string as it's displayed to the user
    public String printResourceRequests(String userid) {
        String rtn = "";
        try {
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT * FROM Resource_Request_History");
            while(rs.next()) {
                rtn += rs.getString(1) + "\t" +rs.getString(2)+ "\t" +rs.getTimestamp(3)+ "\t" +rs.getTimestamp(4)+ "\t" + rs.getTimestamp(5) + "\t" + rs.getString(6) + "\t" + rs.getInt(7) + "\n";
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        return rtn; 
    }
    
    // The option 'Notifications' should display the list of reminders of due date for the issued books, 
    // camera-pickup or study-room reservation (as mentioned in the description).
    // return the string as it's displayed to the user
    public String printNotifications(String userid) {
        String rtn = "";
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Messages WHERE patron_ID=?");
            ps.setString(1, userid);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()){
                rtn += rs.getString(1) + "\t" + rs.getString(2) + "\n";
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return rtn; 
    }
    
    public String printDueBalance(String userid) {
        String rtn = "";
        try{
            PreparedStatement ps = con.prepareStatement("SELECT SUM(value) FROM Fees WHERE patron_ID=?");
            ps.setString(1, userid);
            ResultSet rs = ps.executeQuery();
            
            if(!rs.isBeforeFirst()){
                // no fees
                rtn += "You have no fees!";
            } else {
                // fees
                rs.next();
                rtn += "You owe $" + rs.getInt(1);
            }
            
        } catch (SQLException e) { e.printStackTrace(); }
        return rtn;
    }
    // Clear all dues 
    // Can we assume that when someone chooses to clear their due-balance all overdue materials are checked back in? Answer is yes
    // https://moodle1516-courses.wolfware.ncsu.edu/mod/forum/discuss.php?d=239657
    public void clearDueBalance(String userid) {
        try {
            PreparedStatement ps = con.prepareStatement("DELETE FROM Fees WHERE patron_ID=?");
            ps.setString(1, userid);
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    // print info for conference, study, and media rooms
    public String printRooms(String library, int occupants) {
        String rtn = "";
        String rtnCon = "";
        String rtnStu = "";
        String rtnMed = "";
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Media_Rooms WHERE occupants>=?");
            ps.setInt(1, occupants);
            ResultSet rs =ps.executeQuery();
            while(rs.next()) {
                rtnMed += rs.getString(1) + "\n";
            }
            
            ps = con.prepareStatement("SELECT * FROM Conference_Rooms WHERE library=? AND occupants>=?");
            ps.setString(1, library);
            ps.setInt(2, occupants);
            rs = ps.executeQuery();
            while(rs.next()) {
                rtnCon += rs.getString(1) + "\n";
            }
            
            ps = con.prepareStatement("SELECT * FROM Study_Rooms WHERE library=? AND occupants>=?");
            ps.setString(1, library);
            ps.setInt(2, occupants);
            rs = ps.executeQuery();
            while(rs.next()) {
                rtnStu +=  rs.getString(1) + "\n";
            }
            
        } catch (SQLException e) { e.printStackTrace(); }
        rtn += "Media Rooms\n" + rtnMed;
        rtn += "Conference Rooms\n" + rtnCon;
        rtn += "Study Rooms\n" + rtnStu;
        return rtn;
    }
    
    // Find if the room id is availble for booking to the user
    // Conference rooms can't be booked by students
    // return "Conference" OR "Media" OR "Study" OR "" (if not available)
    // Conference_Rooms, Media_Rooms, Study_Rooms
    public String roomAvailability(String id, Boolean isStudent) {
        try {
            Statement stconf = con.createStatement();
            ResultSet conf = stconf.executeQuery("SELECT room_number FROM Conference_Rooms");
            Statement ststudy = con.createStatement();
            ResultSet study = ststudy.executeQuery("SELECT room_number FROM Study_Rooms");
            Statement stmedia = con.createStatement();
            ResultSet media = stmedia.executeQuery("SELECT room_number FROM Media_Rooms");
            while (conf.next()) {
                if (conf.getString("room_number").equals(id) && !isStudent) {
                    return "Conference";
                } else {
                    return "";
                }
            }
            while (study.next()) {
                if (study.getString("room_number").equals(id)) {
                    return "Study";
                }
            }
            
            while (media.next()) {
                if (media.getString("room_number").equals(id)) {
                    return "Media";
                }
            }
            
        } catch (SQLException e) { e.printStackTrace(); }
        return "";
    }
    
    // book a media room
    // Checking out the room can only happen after the designated start time for reservation
    public void reserveMediaRoom(String userId, String roomId, String instrumentm, int chairNum, Timestamp timeSlot) {
        String timeslot = "";
        try{
            String selectSQL = "SELECT timeSlot FROM Media_Booked WHERE room_number = ?";
            PreparedStatement ps = con.prepareStatement(selectSQL);
            ps.setString(1, roomId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                timeslot = rs.getString("timeSlot");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        if( timeslot.equals(timeSlot) ) {
            return;  
        } else {
            try{
                String insert = "INSERT INTO Media_Booked("+timeSlot+", NULL, NULL,"+ roomId+","+ userId+")";
                PreparedStatement stmnt;
                stmnt = con.prepareStatement( insert );
                stmnt.executeUpdate();
            
            } catch (SQLException e) { e.printStackTrace(); }  
            try{
                String insert = "INSERT INTO Resource_Request_History("+
                userId+","+roomId+", NULL, NULL, NULL, NULL, NULL)";
                PreparedStatement stmnt;
                stmnt = con.prepareStatement( insert );
                stmnt.executeUpdate();
            
            } catch (SQLException e) { e.printStackTrace(); }
        }
    }
    
    // default the technology consultations to an hour
    public void requestTechnologyConsultation(String userID, String location, String date1, String date2, String date3, String topic){
        //IMPLEMENT
        // >>>> Why is there timeslot1a and timeslot1b?
    }
    
    // Add feedback for a technology consultation
    public boolean addTechnologyConsultationFeedback(String consID, String feedback){
        try{
            PreparedStatement ps = con.prepareStatement("UPDATE Tech_Consultation_Log SET feedback=? WHERE consID=?");
            ps.setString(1, feedback);
            ps.setString(2, consID);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) { 
            // UPDATE failed
            return false;
        }
    }
    
    // Find all technology consultations
    public String displayTechnologyConsultations(String userID){
        String rtn = "";
        
        try {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Tech_Consultation_Log WHERE patronID=?");
            ps.setString(1, userID);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                rtn += rs.getString(1);
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
        return rtn;
    }
    
    // reserve a non-media room
    // faculty differences https://github.ncsu.edu/cblupo/csc440library/issues/4
    // Checking out the room can only happen after the designated start time for reservation
    public void reserveConferenceStudyRoom(String userid, String roomid, String library, Timestamp reserveDate, Timestamp endTime) {
        try{
            String type = "";
            // determine conference or study
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Study_Rooms WHERE room_number=? AND library=?");
            ps.setString(1, roomid);
            ps.setString(2, library);
            ResultSet rs = ps.executeQuery();
            if(!rs.isBeforeFirst()){
                ps = con.prepareStatement("SELECT * FROM Conference_Rooms WHERE room_number=? AND library=?");
                ps.setString(1, roomid);
                ps.setString(2, library);
                rs = ps.executeQuery();
                if(!rs.isBeforeFirst()){
                    sop("Invalid room ID");
                    return;
                } else {
                    type = "Conf";
                }
            } else {
                type = "Study";
            }
            
            ps = con.prepareStatement("SELECT * FROM Study_Booked SB, Conf_Booked, CB "+
                                                        "WHERE ((SB.room_number=? AND SB.library=?) OR (CB.room_number=? AND CB.library=?)) "+
                                                        "AND ((SB.date_begin_reservation<=? AND SB.date_end_reservation>=?) OR (CB.date_begin_res<=? AND CB.date_end_res>=?))");
            ps.setString(1, roomid);
            ps.setString(2, library);
            ps.setString(3, roomid);
            ps.setString(4, library);
            ps.setTimestamp(5, endTime);
            ps.setTimestamp(6, reserveDate);
            ps.setTimestamp(7, endTime);
            ps.setTimestamp(8, reserveDate);
            rs = ps.executeQuery();
            
            if(!rs.isBeforeFirst()){
                sop("Unable to reserve room");
                return;
            } else {
                if(type.equals("Study")){
                    // insert into study booked
                    ps = con.prepareStatement("INSERT INTO Study_Booked VALUES (?, ?, ?, ?, ?, ?)");
                    ps.setString(1, userid);
                    ps.setString(2, roomid);
                    ps.setString(3, library);
                    ps.setTimestamp(4, reserveDate);
                    ps.setTimestamp(5, endTime);
                    //ps.setTimestamp(6, null);
                    ps.executeUpdate();
                } else {
                    // insert into conf booked
                    ps = con.prepareStatement("INSERT INTO Conf_Booked VALUES (?, ?, ?, ?, ?, ?)");
                    ps.setString(1, roomid);
                    ps.setString(2, library);
                    ps.setString(3, userid);
                    ps.setTimestamp(4, reserveDate);
                    ps.setTimestamp(5, endTime);
                    //ps.setTimestamp(6, null);
                    ps.executeUpdate();
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        
    }
    
    // display all cameras
    public String printCameras(){
        String rtn = "Camera IDs\n";
        try{
            String selectSQL = "SELECT * FROM Cameras";
            PreparedStatement ps = con.prepareStatement(selectSQL);
            ResultSet rs = ps.executeQuery();
            
            String id = "";
            
            while(rs.next()){
                id = rs.getString(1);
                
                rtn += id + "\n";
            }
            
            rs.close();
        }
        catch (SQLException e) {e.printStackTrace();}
        return rtn;
    }
    
    // display the details of the selected camera
    public String printCameraDetails(String cameraID){
        String rtn = "ID \tLibrary \tMake \t\tModel \tLens \t\t\tMemory\n";
        try{
            String selectSQL = "SELECT * FROM Cameras C, Camera_Check_Out CO WHERE C.camera_ID=? AND C.camera_ID=CO.camera_ID";
            PreparedStatement ps = con.prepareStatement(selectSQL);
            ps.setString(1, cameraID);
            ResultSet rs = ps.executeQuery();
            
            // check for result
            if(!rs.isBeforeFirst()){
                return "Invalid ID";
            }
            
            String id = "";
            String library = "";
            String make = "";
            String model = "";
            String lens = "";
            String memory = "";
            Timestamp checkOutDate;
            Timestamp dueDate;
            
            while(rs.next()){
                id = rs.getString("C.camera_ID");
                library = rs.getString("C.library");
                make = rs.getString("C.make");
                model = rs.getString("C.model");
                lens = rs.getString("C.lens_configuration");
                memory = rs.getString("C.memory");
                checkOutDate = rs.getTimestamp("CO.check_out_date");
                dueDate = rs.getTimestamp("CO.due_date");
                
                rtn += id + " \t" + library + " \t\t" + make + " \t" + model + " \t" + lens + " \t" + memory + " \n";
                rtn += "Check Out Date \tDue Date\n";
                rtn += checkOutDate + " \t" + dueDate + "\n";
            }
            
            rs.close();
        }
        catch (SQLException e) {e.printStackTrace();}
        return rtn;
    }
    
    // attempt to reserve a camera
    // return "camera reserved" and due date if it is reserved
    // return "on waitlist" if camera is already reserved
    public String requestCameraReservation(String userid, String cameraid, Timestamp date, Timestamp dueDate){
        String rtn = "";
        // TODO: TEST
        try{
            String selectSQL = "SELECT * FROM Camera_Check_Out WHERE camera_ID=? AND (check_out_date>=? AND due_date<=?)";
            PreparedStatement ps = con.prepareStatement(selectSQL);
            ps.setString(1, cameraid);
            ps.setTimestamp(2, dueDate);
            ps.setTimestamp(3, date);
            ResultSet rs = ps.executeQuery();
            
            // check for result
            if(!rs.isBeforeFirst()){
                // no conflicts, reserve camera
                // insert into camera check out
                String insertSQL = "INSERT INTO Camera_Check_Out VALUES (?, ?, ?, ?)";
                ps = con.prepareStatement(insertSQL);
                ps.setString(1, userid);
                ps.setString(2, cameraid);
                ps.setTimestamp(3, date);
                ps.setTimestamp(4, dueDate);
                ps.executeUpdate();
                
                // insert into resource request history
                insertSQL = "INSERT INTO Resource_Request_History VALUES (?, ?, ?, ?, ?, ?, ?)";
                ps = con.prepareStatement(insertSQL);
                ps.setString(1, userid);
                ps.setString(2, cameraid);
                ps.setTimestamp(3, date);
                //ps.setTimestamp(4, null);
                ps.setTimestamp(5, dueDate);
                ps.setString(6, "6 days");
                // ps.setInt(7, null);
                ps.executeUpdate();
                
                rtn = "Camera " + cameraid + " will be due at " + dueDate.toString();
            } else {
                // conflicts, put on waitlist
                selectSQL = "SELECT MAX(place) FROM Camera_Wait_Queue WHERE camera_ID=? GROUPBY camera_ID";
                ps = con.prepareStatement(selectSQL);
                ps.setString(1, cameraid);
                rs = ps.executeQuery();
                
                // check for result
                int place = 0;
                if(!rs.isBeforeFirst()){
                    // enter at place 1
                    place = 1;
                } else {
                    // enter at next place
                    place = rs.getInt(1) + 1;
                }
                
                // insert to waitlist
                String insertSQL = "INSERT INTO Camera_Wait_Queue VALUES (?, ?, ?)";
                ps = con.prepareStatement(insertSQL);
                ps.setString(1, userid);
                ps.setString(2, cameraid);
                ps.setInt(3, place);
                ps.executeUpdate();
                
                rtn = "You are in position " + place + " on the waitlist";
            }
            
        } catch(SQLException e) {e.printStackTrace();}
        
        return rtn;
    }
    
    // update the late fees
    // books $2 per day
    // cameras $1 per day
    // run ONCE PER DAY
    public void recalculateLateFees() {
        try{
            PreparedStatement ps = con.prepareStatement("SELECT fee_ID, type, value, date_paid FROM Fees");
            ResultSet rs = ps.executeQuery();
            
            String id = "";
            String type = "";
            int value = 0;
            Timestamp date_paid = null;
            
            while(rs.next()){
                id = rs.getString(1);
                type = rs.getString(2);
                value = rs.getInt(3);
                date_paid = rs.getTimestamp(4);
                
                if(date_paid == null){
                    // fee has not been paid, increment
                    if(type.equals("Camera")){
                        value += 1;
                    } else if(type.equals("Book")){
                        value += 2;
                    }
                    ps = con.prepareStatement("UPDATE Fees SET value=? WHERE fee_ID=?");
                    ps.setInt(1, value);
                    ps.setString(2, id);
                    ps.executeUpdate();
                }
            }
        } catch(SQLException e) {e.printStackTrace();}
        
        
        // try{
        //     String selectSQL = "SELECT patron_ID, camera_ID, due_date FROM Camera_Check_Out WHERE sysdate > TO_date(due_date, 'yyyy-mm-dd') DAYS FROM DUAL";
        //     PreparedStatement ps = con.prepareStatement(selectSQL);
        //     ResultSet rs = ps.executeQuery();
        //     String camPatID = "";
        //     String camCamID = "";
        //     double camLate_fee = 0;
        //     Timestamp due_date;
        //     while(rs.next()){
        //         camPatID = rs.getString(1);
        //         camCamID = rs.getString(2);
        //         due_date = rs.getTimestamp(3);
        //         camLate_fee = trunc(sysdate) - TO_date(due_date, "yyyy-mm-dd");
        //         String updateSQL = "UPDATE Camera_Check_Out SET late_fee=? WHERE patron_ID=? AND camera_ID=?";
        //         ps = con.prepareStatement(updateSQL);
        //         ps.setDouble(1, camLate_fee);
        //         ps.setString(2, camPatID);
        //         ps.setString(3, camCamID);
        //         ps.executeQuery();
        //     }
                
        //     selectSQL = "SELECT patron_ID, pub_ID, copy_num, due_date FROM Pub_Check_Out WHERE sysdate > TO_date(due_date, 'yyyy-mm-dd') DAYS FROM DUAL";
        //     PreparedStatement ps = con.prepareStatement(selectSQL);
        //     ps.executeQuery();
            
        //     String pubPatID = "";
        //     String pubID = "";
        //     int copy_num;
        //     double pubLate_fee = 0;
            
        //     while(rs.next()){
        //         pubPatID = rs.getString(1);
        //         pubID = rs.getString(2);
        //         copy_num = rs.getInt(3);
        //         due_date = rs.getTimestamp(4);
        //         pubLate_fee = 2*(trunc(sysdate) - TO_date(due_date, "yyyy-mm-dd"));
        //         String updateSQL = "UPDATE Pub_Check_Out SET late_fee=? WHERE patron_ID=? AND pub_ID=? AND copy_num=?";
        //         ps = con.prepareStatement(updateSQL);
        //         ps.setDouble(1, pubLate_fee);
        //         ps.setString(2, pubPatID);
        //         ps.setString(3, pubID);
        //         ps.setDouble(4, pubLate_fee);
        //         ps.executeQuery();
        //     }
        // } catch(SQLException e) {e.printStackTrace();}
    }
    
    // Have to make this dumb thing because System.out.println takes too long to type
    private void sop(Object obj) {
    	System.out.println(obj);
    }
}