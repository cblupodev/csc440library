import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;

class DBBuilder {
    
    private Connection con = null;
    
    public DBBuilder(Connection con) {
        this.con = con;
    }
    
    public void createTables() {
        try {
            PreparedStatement ps = con.prepareStatement(
                "CREATE TABLE IF NOT EXISTS Patrons ("+
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
        
        // TODO fill in the rest
    }
    
    public void fillTables() {
        
    }
    
    public void deleteTables() {
        try {
            PreparedStatement ps = con.prepareStatement(
                "DROP TABLE IF EXISTS Patrons;"+
                "DROP TABLE IF EXISTS asdf;"
            );
            ps.executeUpdate();
        } catch (SQLException e) { e.printStackTrace(); }
    }
}