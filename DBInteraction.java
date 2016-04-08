import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
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
        return "9";
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
        return "";        
    }
    
    // find if the publication exists
    public Boolean hasPublication(String id) {
        return true;
    }
    
    // get publiction info
    // return the string as it's displayed to the user
    public String printPublicationInfo(String id) {
        return "";
    }
    
    // see if the publication is checkout out by the current user
    public Boolean isPublicationCheckedOutBy(String userid, String pubid) {
        return true;
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
    
    From the Project Description ↓
    Checkout and Return Policy
    The checkout process is initiated by the patron (after finding the desired item(s)). The checkout process updates the 
    appropriate tables in the database with respect to availability and assigns return dates to items based on relevant rules 
    for the type of holding and type of patron (mentioned throughout the description). Checkout should only be possible 
    for available items. Additional requests for items not currently available will be placed on a wait queue and serviced on a 
    first-come-first-serve based. However, faculty always have priority on the wait queue. Checked out items can be renewed by a 
    patron only if no patrons are waiting for item. If it is possible to renew, the due dates and relevant data are updated in database.
    */
    public void checkoutPublication(String userid, String pubid, String outDate, String inDate) {
        
    }
    
    // List of resources currently checked out by the user.
    public ArrayList<Integer> getCheckedOutResources(String userid) {
        ArrayList<Integer> ary = new ArrayList<Integer>();
        
        return ary;
    }
    
    // For a particular resource, its details, check-out date/time, due date/time should be displayed
    public String printResourceDetails(String resourceid){
        return "";
    }
    
    // Renews the check out for the particular resource
    
    // There is no limit to the number of times a publication can be renewed. Also, it can be renewed for the same amount of time as it can be issued for in the first place
    // https://moodle1516-courses.wolfware.ncsu.edu/mod/forum/discuss.php?d=243748
    public void renewCheckedOutResource(String userid, String resourceid){
        
    }
    
    // The option 'Resource Request' will display the list of resources requested by the user. 
    // resources are Publications, Conference/Study/Media-production rooms, Technology Consultation, and Cameras
    // from forum, Resource request basically just shows a details of all the resources requested in the past as well as future reservations of that patron
    // return the string as it's displayed to the user
    public String printResourceRequests(String userid) {
        // use the ResourceRequest table
        return "";
    }
    
    
    // The option 'Notifications' should display the list of reminders of due date for the issued books, 
    // camera-pickup or study-room reservation (as mentioned in the description).
    // return the string as it's displayed to the user
    public String printNotifications(String userid) {
       return ""; 
    }
    
    public String printDueBalance(String userid) {
        return "";
    }
    
    // Clear all dues 
    // Can we assume that when someone chooses to clear their due-balance all overdue materials are checked back in? Answer → yes
    // https://moodle1516-courses.wolfware.ncsu.edu/mod/forum/discuss.php?d=239657
    public void clearDueBalance(String userid) {
        
    }
    
    // print info for conference, study, and media rooms
    public String printRooms(String library, int occupants) {
        return "";
    }
    
    // Find if the room id is availble for booking to the user
    // Conference rooms can't be booked by students
    // return "Conference" OR "Media" OR "Study" OR "" (if not available)
    public String roomAvailability(int id, Boolean isStudent) {
        return "Media";
    }
    
    // book a media room
    public void reserveMediaRoom(String userId, int roomId, String instrumentm, int chairNum) {
        
    }
    
    public void requestTechnologyConsultation(String userID, String location, String date1, String date2, String date3, String topic){
        
    }
    
    // Add feedback for a technology consultation
    public boolean addTechnologyConsultationFeedback(String consID, String feedback){
        
        return true;
    }
    
    // Find all technology consultations
    public String displayTechnologyConsultations(String userID){
        return "";
    }
    
    // reserve a non-media room
    public void reserveConferenceStudyRoom(String userid, int roomid, String reserveDate, String reserveTime) {
        
    }
}