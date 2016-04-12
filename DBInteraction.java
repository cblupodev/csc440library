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
        String ID = "";
        try{
            String selectSQL = "SELECT ID FROM Patrons WHERE user_name=? AND password=?";
            PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet rs = preparedStatement.executeQuery();
            
            if(!rs.isBeforeFirst()){
                return "";
            } else {
                rs.next();
                return rs.getString("ID");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return ID;
    }

    // print profile details
    // return the string as it's displayed to the user
    public String printProfile(String id) {
        sop(id);
        String rtn = "";
        try {
            PreparedStatement ps = con.prepareStatement(
                "SELECT * FROM Patrons P, Students S WHERE P.ID=? AND P.ID=S.ID"
            );
            // need to see what details it wants
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            if(rs.isBeforeFirst()){
                // Student
                rs.next();
                rtn += "First Name: \t" + rs.getString("first_name") + "\n";
                rtn += "Last Name:  \t" + rs.getString("last_name") + "\n";
                rtn += "ID:         \t" + rs.getString("ID") + "\n";
                rtn += "Department: \t" + rs.getString("dept") + "\n";
                rtn += "Nationality:\t" + rs.getString("nationality") + "\n";
                rtn += "Phone:      \t" + rs.getString("phone") + "\n";
                rtn += "Alt Phone:  \t" + rs.getString("alt_phone") + "\n";
                rtn += "Birthdate:  \t" + rs.getString("dob") + "\n";
                rtn += "Address:    \t" + rs.getString("street") + " " + rs.getString("city") + "," + rs.getString("state") + " " + rs.getString("postcode") + "\n";
                rtn += "Sex:        \t" + rs.getString("sex") + "\n";
                rtn += "Class:      \t" + rs.getString("classification") + "\n";
                rtn += "Program:    \t" + rs.getString("degree_program") + "\n";
                rtn += "Category:   \t" + rs.getString("category");
            } else {
                ps = con.prepareStatement("SELECT * FROM Patrons P, Faculty F WHERE P.ID=? AND P.ID=F.ID");
                ps.setString(1, id);
                rs = ps.executeQuery();
                if(rs.isBeforeFirst()){
                    // Faculty
                    rs.next();
                    rtn += "First Name: \t" + rs.getString("first_name") + "\n";
                    rtn += "Last Name:  \t" + rs.getString("last_name") + "\n";
                    rtn += "ID:         \t" + rs.getString("ID") + "\n";
                    rtn += "Department: \t" + rs.getString("dept") + "\n";
                    rtn += "Nationality:\t" + rs.getString("nationality") + "\n";
                    rtn += "Category:   \t" + rs.getString("category") + "\n";
                } else {
                    // not student or facutly.  we done goofed
                    return "Who are you???";
                }
            }
            
            /*
            int i = 0;
            while(rsVal.next() && rsName.next()) {
                rtn += rsName.getString(i) + "     " + rsVal.getString(i) + "\n";
                i++;
            }*/
        } catch (SQLException e) { e.printStackTrace(); }
        return rtn;
    }
    

    // edit a profile attribute
    public Boolean updateProfile(String id, String attribute, Object value) {
        try{
            PreparedStatement stmnt;
            String dbname = "";
            

            if ( id.charAt(0) == 'S') {
                dbname = "Students";
            } else {
                dbname = "Faculty";
            }
            if( attribute.equals("first_name") || attribute.equals("last_name") || attribute.equals("dept") ||
                        attribute.equals("nationality") || attribute.equals("user_name") || attribute.equals("password")) {
                dbname = "Patrons";
            } 
            
            
            stmnt = con.prepareStatement
            (
                "UPDATE "+dbname+" SET "+attribute+" = ? WHERE ID = ?"
            );
            stmnt.setString(1, value.toString());
            stmnt.setString(2, id);
            stmnt.executeUpdate();
            
        } catch (SQLException e) { e.printStackTrace(); }
        return true;
    }
    
    // list all publications
    // On selecting the 'Publications' option, the list of all publications (including books, ebooks, journals and conference proceedings) will be displayed
    // return the string as it's displayed to the user
    public String listPublications() {
        String result = "";
        try{
            String selectSQL = "SELECT ID, title FROM Publication";
            PreparedStatement preparedStatement = con.prepareStatement(selectSQL);
            ResultSet rs = preparedStatement.executeQuery();
            while (rs.next()) {
            	result += "\n" + rs.getString("ID") + "\t" + rs.getString("title");
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
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                count = rs.getInt("count(*)");
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
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	title = rs.getString("title");
            	author = rs.getString("authors");
            	pub_year = rs.getString("pub_year");
            }
        } catch (SQLException e) { e.printStackTrace(); }
        try{
            String selectSQL = "SELECT * FROM Books WHERE ISBN = ?";
            PreparedStatement ps = con.prepareStatement(selectSQL);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	publisher = rs.getString("publisher");
            	edition = rs.getString("edition");
            }
            if( !publisher.equals("") ){
                result = title+" \t"+author+" \t"+edition+ " \t" +publisher+" \t"+pub_year;
                return result;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        String copy_number = "";
        try{
            String selectSQL = "SELECT * FROM Journal WHERE ISSN = ?";
            PreparedStatement ps = con.prepareStatement(selectSQL);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	copy_number += rs.getString("copy_number");
            }
            if( !copy_number.equals("") ){
                result = title+" \t"+author+" \t"+copy_number+" \t"+pub_year;
                return result;
            }
        } catch (SQLException e) { e.printStackTrace(); }
        String conf_number = "";
        String conf_name = "";
        try{
            String selectSQL = "SELECT * FROM Conf_Proceedings WHERE conf_number = ?";
            PreparedStatement ps = con.prepareStatement(selectSQL);
            ps.setString(1, id);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
            	conf_number = rs.getString("conf_number");
            	conf_name = rs.getString("conf_name");
            }
            if( !conf_number.equals("") ){
                result = title+" \t"+author+" \t"+conf_name+" \t"+conf_number + " \t" +pub_year;
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
            ResultSet rs = ps.executeQuery();
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
                            rsRes.next();
                            // book is reserved
                            // check if student has rights to this
                            String course = rsRes.getString("course");
                            PreparedStatement psCours = con.prepareStatement("SELECT * FROM Courses_Students WHERE patron_ID=? AND course_ID=?");
                            psCours.setString(1, userid);
                            psCours.setString(2, course);
                            ResultSet rsCours = psCours.executeQuery();
                            if(!rsCours.isBeforeFirst()){
                                // student doesn't have rights to this book
                                sop("You don't have access to this book");
                                return;
                            }
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
                        
                        PreparedStatement psHistory = con.prepareStatement("INSERT INTO Resource_Request_History VALUES (?, ?, ?, ?, ?, ?, ?)");
                        psHistory.setString(1, userid);
                        psHistory.setString(2, pubid);
                        psHistory.setTimestamp(3, outDate);
                        psHistory.setNull(4, java.sql.Types.TIMESTAMP);
                        psHistory.setTimestamp(5, inDate);
                        psHistory.setString(6, "weeks");
                        psHistory.setNull(7, java.sql.Types.INTEGER);
                        //psHistory.setInt(7, null);
                        psHistory.executeUpdate();
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
                    return;
                }
            } else {
                sop("you're on the hold list");
            }
        } catch (SQLException e) { e.printStackTrace(); }
    }
    
    // List of resources currently checked out by the user.
    public String printCheckedOutResources(String userid) {
        String rtn = "";
        
        try{
            // publications
            rtn += "Publications:\n";
            PreparedStatement psPub = con.prepareStatement("SELECT * FROM Pub_Check_Out WHERE patron_ID=?");
            psPub.setString(1, userid);
            ResultSet rsPub = psPub.executeQuery();
            while(rsPub.next()){
                rtn += rsPub.getString("pub_ID") + "\n";
            }
            
            // cameras
            rtn += "Cameras:\n";
            PreparedStatement psCam = con.prepareStatement("SELECT * FROM Camera_Check_Out WHERE patron_ID=?");
            psCam.setString(1, userid);
            ResultSet rsCam = psCam.executeQuery();
            while(rsCam.next()){
                rtn += rsCam.getString("camera_ID") + "\n";
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
                ps.setString(1, resourceid);
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
                        id = rs.getString("pub_ID");
                        title = rs.getString("title");
                        authors = rs.getString("authors");
                        year = rs.getString("pub_year");
                        checkOutDate = rs.getTimestamp("check_out_date");
                        dueDate = rs.getTimestamp("due_date");
                        
                        rtn += id + " \t" + title + " \t" + authors + " \t" + year + " \n";
                        rtn += "Check Out Date \t\tDue Date\n";
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
    public void renewCheckedOutResource(String userid, String resourceid, Timestamp newEndDate){
        try{
            if(resourceid.charAt(1) == 'C' && resourceid.charAt(2) == 'A'){
                // camera
                sop("You can't renew a checked out camera");
                return;
            } else {
                // publication
                // update pub_check_out
                PreparedStatement ps = con.prepareStatement("UPDATE Pub_Check_Out SET due_date=? WHERE pub_ID=? AND patron_ID=?");
                ps.setTimestamp(1, newEndDate);
                ps.setString(2, resourceid);
                ps.setString(3, userid);
                ps.executeUpdate();
                // update resource_request_history
                ps = con.prepareStatement("UPDATE Resource_Request_History SET due_date=? WHERE resource_ID=? AND patron_ID=?");
                ps.setTimestamp(1, newEndDate);
                ps.setString(2, resourceid);
                ps.setString(3, userid);
                ps.executeUpdate();
                return;
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
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Resource_Request_History WHERE patron_ID=?");
            ps.setString(1, userid);
            ResultSet rs = ps.executeQuery();
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
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Media_Rooms WHERE chairs>=?");
            ps.setInt(1, occupants);
            ResultSet rs =ps.executeQuery();
            while(rs.next()) {
                rtnMed += rs.getString(1) + "\n";
            }
            
            ps = con.prepareStatement("SELECT * FROM Conference_Rooms WHERE library=? AND capacity>=?");
            ps.setString(1, library);
            ps.setInt(2, occupants);
            rs = ps.executeQuery();
            while(rs.next()) {
                rtnCon += rs.getString(1) + "\n";
            }
            
            ps = con.prepareStatement("SELECT * FROM Study_Rooms WHERE library=? AND chairs>=?");
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
                String insert = "INSERT INTO Media_Booked VALUES("+timeSlot+"NULL, NULL,"+ roomId+","+userId+")";
                PreparedStatement stmnt;
                // stmnt.setTimestamp(1, timeSlot);
                // stmnt.setNull(2);
                // stmnt.setNull(3);
                // stmnt.setString(4, roomId);
                // stmnt.setString(5, userId);
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
    public void requestTechnologyConsultation(String userID, String location, Timestamp date1_start, Timestamp date1_end, Timestamp date2_start, Timestamp date2_end, Timestamp date3_start, Timestamp date3_end, String topic){
        ArrayList<String> assistants = new ArrayList<String>();
        ArrayList<String> remaining;
        
        try{
            // get list of all assistants with expertise
            PreparedStatement ps = con.prepareStatement("SELECT dmassistant_ID FROM Digital_Media_Assistants "+
                                                        "WHERE expertise1=? OR expertise2=? OR expertise3");
            ps.setString(1, topic);
            ps.setString(2, topic);
            ps.setString(3, topic);
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                assistants.add(assistants.size(), rs.getString(1));
            }
            if(assistants.size() == 0){
                return;
            }
            remaining = new ArrayList<String>(assistants);
            
            // create tcID
            ps = con.prepareStatement("SELECT COUNT(*) FROM Tech_Consult");
            rs = ps.executeQuery();
            String tcID = "TC" + (rs.getInt(1) + 1);
            
            
            // check for collisions on first timeslot
            ps = con.prepareStatement("SELECT D.dmassistant_ID FROM Digital_Media_Assistants D, Tech_Reservation T "+
                                                        "WHERE D.dmassistant_ID=T.assistant_ID "+
                                                        "AND (T.startTime<=? AND T.endTime>=?)");
            ps.setTimestamp(1, date1_end);
            ps.setTimestamp(2, date1_start);
            rs = ps.executeQuery();
            while(rs.next()){
                remaining.remove(rs.getString(1));
            }
            if(remaining.size() > 0){
                // got first timeslot
                String assistant = remaining.get(0);
                // tech_consult insert
                ps = con.prepareStatement("INSERT INTO Tech_Consult VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, tcID);
                ps.setString(2, userID);
                ps.setTimestamp(3, date1_start);
                ps.setTimestamp(4, date1_end);
                ps.setTimestamp(5, date2_start);
                ps.setTimestamp(6, date2_end);
                ps.setTimestamp(7, date3_start);
                ps.setTimestamp(8, date3_end);
                ps.setString(9, location);
                ps.setString(10, topic);
                ps.setString(11, assistant);
                ps.executeUpdate();
                // tech_reservation insert
                ps = con.prepareStatement("INSERT INTO Tech_Reservation VALUES (?, ?, ?, ?, ?)");
                ps.setTimestamp(1, date1_start);
                ps.setTimestamp(2, date1_end);
                ps.setString(3, tcID);
                ps.setString(4, userID);
                ps.setString(5, assistant);
                ps.executeUpdate();
                return;
            }
            
            remaining = new ArrayList<String>(assistants);
            // check for collisions on second timeslot
            ps = con.prepareStatement("SELECT D.dmassistant_ID FROM Digital_Media_Assistants D, Tech_Reservation T "+
                                                        "WHERE D.dmassistant_ID=T.assistant_ID "+
                                                        "AND (T.startTime<=? AND T.endTime>=?)");
            ps.setTimestamp(1, date2_end);
            ps.setTimestamp(2, date2_start);
            rs = ps.executeQuery();
            while(rs.next()){
                remaining.remove(rs.getString(1));
            }
            if(remaining.size() > 0){
                // got second timeslot
                String assistant = remaining.get(0);
                // tech_consult insert
                ps = con.prepareStatement("INSERT INTO Tech_Consult VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, tcID);
                ps.setString(2, userID);
                ps.setTimestamp(3, date1_start);
                ps.setTimestamp(4, date1_end);
                ps.setTimestamp(5, date2_start);
                ps.setTimestamp(6, date2_end);
                ps.setTimestamp(7, date3_start);
                ps.setTimestamp(8, date3_end);
                ps.setString(9, location);
                ps.setString(10, topic);
                ps.setString(11, assistant);
                ps.executeUpdate();
                // tech_reservation insert
                ps = con.prepareStatement("INSERT INTO Tech_Reservation VALUES (?, ?, ?, ?, ?)");
                ps.setTimestamp(1, date2_start);
                ps.setTimestamp(2, date2_end);
                ps.setString(3, tcID);
                ps.setString(4, userID);
                ps.setString(5, assistant);
                ps.executeUpdate();
                return;
            }
            
            remaining = new ArrayList<String>(assistants);
            // check for collisions on third timeslot
            ps = con.prepareStatement("SELECT D.dmassistant_ID FROM Digital_Media_Assistants D, Tech_Reservation T "+
                                                        "WHERE D.dmassistant_ID=T.assistant_ID "+
                                                        "AND (T.startTime<=? AND T.endTime>=?)");
            ps.setTimestamp(1, date3_end);
            ps.setTimestamp(2, date3_start);
            rs = ps.executeQuery();
            while(rs.next()){
                remaining.remove(rs.getString(1));
            }
            if(remaining.size() > 0){
                // got first timeslot
                String assistant = remaining.get(0);
                // tech_consult insert
                ps = con.prepareStatement("INSERT INTO Tech_Consult VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, tcID);
                ps.setString(2, userID);
                ps.setTimestamp(3, date1_start);
                ps.setTimestamp(4, date1_end);
                ps.setTimestamp(5, date2_start);
                ps.setTimestamp(6, date2_end);
                ps.setTimestamp(7, date3_start);
                ps.setTimestamp(8, date3_end);
                ps.setString(9, location);
                ps.setString(10, topic);
                ps.setString(11, assistant);
                ps.executeUpdate();
                // tech_reservation insert
                ps = con.prepareStatement("INSERT INTO Tech_Reservation VALUES (?, ?, ?, ?, ?)");
                ps.setTimestamp(1, date3_start);
                ps.setTimestamp(2, date3_end);
                ps.setString(3, tcID);
                ps.setString(4, userID);
                ps.setString(5, assistant);
                ps.executeUpdate();
                return;
            }
            return;
        } catch (SQLException e) { e.printStackTrace(); }
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
                    ps.setNull(6, java.sql.Types.TIMESTAMP);
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
                    ps.setNull(6, java.sql.Types.TIMESTAMP);
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
                id = rs.getString("camera_ID");
                library = rs.getString("library");
                make = rs.getString("make");
                model = rs.getString("model");
                lens = rs.getString("lens_configuration");
                memory = rs.getString("memory");
                checkOutDate = rs.getTimestamp("check_out_date");
                dueDate = rs.getTimestamp("due_date");
                
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
                ps.setNull(4, java.sql.Types.TIMESTAMP);
                //ps.setTimestamp(4, null);
                ps.setTimestamp(5, dueDate);
                ps.setString(6, "6 days");
                ps.setNull(7, java.sql.Types.INTEGER);
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
    }
    
    // Have to make this dumb thing because System.out.println takes too long to type
    private void sop(Object obj) {
    	System.out.println(obj);
    }
}