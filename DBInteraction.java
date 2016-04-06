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
    public Integer isUser(String username, String password) {
        return 9;
    }
    
    // print profile details
    public String printProfile() {
        return "";
    }
    
    // edit a profile attribute
    public Boolean updateProfile(Integer id, String attribute, Object value) {
        return true;
    }
    
}