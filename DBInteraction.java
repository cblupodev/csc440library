import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;


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
    
    // Any requests which violate any of the constraints mentioned in the description should not be entertained. 
    // If the student requests for a publication that is not currently available, the request should be added to a waitlist 
    // and an appropriate message should be displayed. If the requested publication is available, details like checkout date/time, 
    // return date/time should be taken as input (refer to the description).If the request to issue a book is granted
    // (refer to the constraints on various publications), then the status of the book (issued, waitlisted) should be updated.
    // also handle the case if the user is renewing or requesting
    public void checkoutPublication(String userid, String pubid) {
        
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
}