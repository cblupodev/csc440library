import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;

public DBBuilder() {
    
    private Connection con = null;
    
    public class (Connection con) {
        this.con = con;
    }
    
    public void createTables() {
        PreparedStatement ps = new con.preparedStatement(
            "CREATE TABLE IF NOT EXISTS Patrons ("+
            "   first_name varchar(64) NOT NULL,"+,
            "	last_name varchar(64) NOT NULL,"+,
            "	ID character(16),"+,
            "	dept character(3),"+,
            "	nationality varchar(64),"+,
            "	delinquent boolean NOT NULL,"+,
            "	PRIMARY KEY (ID)"+,
            ")";
        );
        ps.executeQuery();
        
        // TODO fill in the rest
    }
    
    public void fillTables() {
        
    }
    
    public void deleteTables() {
        PreparedStatement ps = new con.preparedStatement(
            "DROP TABLE IF EXISTS Patrons;"+,
            "DROP TABLE IF EXISTS asdf;"
        );
    }
}