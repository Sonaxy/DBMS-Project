package IP_jsp;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DataHandler {
	private Connection conn;

    // Azure SQL connection credentials
    private String server = "moha0051.database.windows.net";
    private String database = "cs-dsa-4513-sql-db";
    private String username = "moha0051";
    private String password = "January@2022";

    // Resulting connection string
    final private String url =
            String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
                    server, database, username, password);

    // Initialize and save the database connection
    private void getDBConnection() throws SQLException {
        if (conn != null) {
            return;
        }

        this.conn = DriverManager.getConnection(url);
    }
    
    // Return the result of selecting everything from the employee table where salary 
    //condition is satisfied
    public ResultSet getAllEmployees(int emp_sal) throws SQLException 
    {
        getDBConnection(); // Prepare the database connection
        
        // Prepare the SQL statement
        final String sqlQuery = "SELECT * FROM Employee WHERE emp_sal > ?";
        final PreparedStatement stmt = conn.prepareStatement(sqlQuery);
        
        // Replace ?s in the query with user inputs
        stmt.setInt(1, emp_sal);
      
        
        return stmt.executeQuery();
    }

    // Inserts a record into the employee table with the given attribute values
    public boolean addEmployee(String emp_name, String emp_address, int emp_sal) throws SQLException 
    {

        getDBConnection(); // Prepare the database connection

        // Prepare the SQL statement
        final String sqlQuery = "INSERT INTO Employee (emp_name, emp_address, emp_sal) VALUES (?, ?, ?)";
        final PreparedStatement stmt = conn.prepareStatement(sqlQuery);

        // Replace the '?' in the above statement with the given attribute values
        stmt.setString(1, emp_name);
        stmt.setString(2, emp_address);
        stmt.setInt(3, emp_sal);

        // Execute the query, if only one record is updated, then we indicate success by returning true
        return stmt.executeUpdate() == 1;
    }

}
