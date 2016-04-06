import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;


// use to construct the database
class DBBuilder {
    
    private Connection con = null;
    
    public DBBuilder(Connection con) {
        this.con = con;
    }
    
    // create tables from https://drive.google.com/open?id=1BoMzhg6PL0osWdHVDvKXC_lRCbDRALIDwIVQD68d3pY
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
        // import from https://drive.google.com/open?id=14-YvxM3s_P8XuszpTIbGdTIREDulbbQmm8u59GlqyYw
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