import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;

class DBInteraction() {
    
    Connection con = null;
    
    public DBInteraction(Connection con) {
        this.con = con
    }
    
    public Boolean isUser(String username, String password) {
        return true;
    }
    
}