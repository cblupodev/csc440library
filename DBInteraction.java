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
    public String printProfile(String id) {
        DatabaseMetaData md = con.getMetaData();
        
        String rtn = "";
        PreparedStatement ps = con.prepareStatement(
            "SELECT * FROM Patrons WHERE ID = ?"
        );
        
        // TODO need to join with the student and faculty table
        
        ps.setString(1, id);
        ResultSet rsVal = ps.executeQuery();
        ResultSet rsName = md.getColumns(null, null, "Patrons", null);
        
        // construct the string to print names and values
        int i = 0
        while(rsVal.next() && rsName.next()) {
            rtn += rsName.getString(i) + "     " + rsVal.getString(i) + "\n";
            i++;
        }
        return rtn;
    }
    
    // edit a profile attribute
    public Boolean updateProfile(String id, String attribute, Object value) {
        return true;
    }
    
    // list all publications
    // On selecting the ‘Publications’ option, the list of all publications (including books, ebooks, journals and conference proceedings) will be displayed
    public String listPublications() {
        return "";        
    }
    
    // find if the publication exists
    public Boolean hasPublication(String id) {
        return true;
    }
    
    // get publiction info
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
    
}