import java.sql.Connection;
import java.sql.Statement;
import java.util.Scanner;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Mohanty_Sonaxy_IP_Task5b {

    // Database credentials
    final static String HOSTNAME = "moha0051.database.windows.net";
    final static String DBNAME = "cs-dsa-4513-sql-db";
    final static String USERNAME = "moha0051";
    final static String PASSWORD = "January@2022";

    // Database connection string
    final static String URL = String.format("jdbc:sqlserver://%s:1433;database=%s;user=%s;password=%s;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=30;",
            HOSTNAME, DBNAME, USERNAME, PASSWORD);
    
 // Query templates
 	final static String QUERY_2a = "INSERT INTO Product (product_ID, prod_date, time_taken, worker_name, qc_name) VALUES"
 			+ "(?, ?, ?, ?, ?)";
 	final static String QUERY_2c = "INSERT INTO Repair VALUES (?, ?, ?)";
 	final static String QUERY_3a = "INSERT INTO Customer VALUES (?, ?)";
 	final static String QUERY_3b = "INSERT INTO Purchase VALUES (?, ?)";
 	final static String QUERY_6a = "INSERT INTO Repair_Accident VALUES (?, ?, ?)";
 	final static String QUERY_6b = "INSERT INTO Prod_Accident VALUES (?, ?, ?)";
 	final static String QUERY_7 = "SELECT P.prod_date, P.time_taken \r\n"
 			+ "FROM Product P\r\n"
 			+ "WHERE P.product_ID = ?";
 	final static String QUERY_8 = "SELECT  *\r\n"
 			+ "FROM Product P\r\n"
 			+ "WHERE P.worker_name = ?";
 	final static String QUERY_9 = "SELECT COUNT(C.complaint_ID)\r\n"
 			+ "FROM Complaint C\r\n"
 			+ "INNER JOIN Defect D ON C.complaint_ID = D.complaint_ID\r\n"
 			+ "INNER JOIN Product P ON D.product_ID = P.product_ID\r\n"
 			+ "WHERE P.qc_name = ?";
 	final static String QUERY_10 = "SELECT SUM(P.cost_prod3)\r\n"
 			+ "FROM Request R\r\n"
 			+ "INNER JOIN Cost C ON R.product_ID = C.product_ID\r\n"
 			+ "INNER JOIN Prod3_Acc P ON C.account_no = P.account_no\r\n"
 			+ "WHERE R.qc_name = ?";
 	final static String QUERY_11 = "SELECT C.*  \r\n"
 			+ "FROM Customer C\r\n"
 			+ "INNER JOIN Purchase P ON C.cust_name = P.cust_name\r\n"
 			+ "INNER JOIN Product2 Pr ON Pr.product_ID = P.product_ID\r\n"
 			+ "WHERE Pr.color = ?\r\n"
 			+ "ORDER BY C.cust_name";
 	final static String QUERY_12 = "SELECT *\r\n"
 			+ "FROM Employee E\r\n"
 			+ "WHERE E.emp_sal > ?";
 	final static String QUERY_13 = "SELECT SUM(A.days_lost) \r\n"
 			+ "FROM Accident A\r\n"
 			+ "INNER JOIN Repair_Accident Ra ON A.accident_no = Ra.accident_no\r\n"
 			+ "INNER JOIN Rep_Compl R ON R.product_ID = Ra.product_ID";
 	final static String QUERY_14 = "SELECT AVG(T.cost) as avg_cost FROM\r\n"
 			+ "(SELECT P.product_ID as ID, P1.cost_prod1 as cost, YEAR(P.prod_date)  as year\r\n"
 			+ "FROM Product P\r\n"
 			+ "INNER JOIN Cost C ON P.product_ID = C.product_ID\r\n"
 			+ "INNER JOIN Prod1_Acc P1 ON C.account_no = P1.account_no\r\n"
 			+ "UNION\r\n"
 			+ "SELECT P.product_ID as ID, P2.cost_prod2 as cost, YEAR(P.prod_date)  as year\r\n"
 			+ "FROM Product P\r\n"
 			+ "INNER JOIN Cost C ON P.product_ID = C.product_ID\r\n"
 			+ "INNER JOIN Prod2_Acc P2 ON C.account_no = P2.account_no\r\n"
 			+ "UNION\r\n"
 			+ "SELECT P.product_ID as ID, P3.cost_prod3 as cost, YEAR(P.prod_date)  as year \r\n"
 			+ "FROM Product P\r\n"
 			+ "INNER JOIN Cost C ON P.product_ID = C.product_ID\r\n"
 			+ "INNER JOIN Prod3_Acc P3 ON C.account_no = P3.account_no\r\n"
 			+ ") T\r\n"
 			+ "WHERE T.year = ?";
 	final static String QUERY_15a = "DELETE FROM Repair_Accident WHERE accident_no = (SELECT accident_no FROM Accident\r\n"
 			+ "WHERE accident_dt BETWEEN ? AND ?);";
 	final static String QUERY_15b = "DELETE FROM Prod_Accident WHERE accident_no = (SELECT accident_no FROM Accident\r\n"
 			+ "WHERE accident_dt BETWEEN ? AND ?);";
 	final static String QUERY_15c = "DELETE FROM Accident \r\n"
 			+ "WHERE accident_dt BETWEEN ? AND ?;";
 	final static String QUERY_16 = "INSERT INTO Employee VALUES (?, ?, ?)";
 	final static String QUERY_17 = "SELECT C.*  \r\n"
 			+ "FROM Customer C\r\n"
 			+ "INNER JOIN Purchase P ON C.cust_name = P.cust_name\r\n"
 			+ "INNER JOIN Product2 Pr ON Pr.product_ID = P.product_ID\r\n"
 			+ "WHERE Pr.color = 'Red'\r\n"
 			+ "ORDER BY C.cust_name";

 // User input prompt//
    final static String PROMPT = 
            "\nPlease select one of the options below: \n" +
            "1) Enter a new employee; \n" + 
            "2) Enter a new product associate with the person who made the product, repaired the product if it is repaired, or checked the product; \n"+ 
            "3) Enter a customer associated with some products; \n" +
            "4) Create a new account associated with a product; \n" +
            "5) Enter a complaint associated with a customer and product; \n" +
            "6) Enter an accident associated with an appropriate employee and product; \n" +
            "7) Retrieve the date produced and time spent to produce a particular product; \n" +
            "8) Retrieve all products made by a particular worker; \n" +
            "9) Retrieve the total number of errors a particular quality controller made; \n" +
            "10) Retrieve the total costs of the products in the product3 category which were repaired at the request of a particular quality controller; \n" +
            "11) Retrieve all customers (in name order) who purchased all products of a particular color; \n" +
            "12) Retrieve all employees whose salary is above a particular salary; \n" +
            "13) Retrieve the total number of work days lost due to accidents in repairing the products which got complaints; \n" +
            "14) Retrieve the average cost of all products made in a particular year; \n" +
            "15) Delete all accidents whose dates are in some range; \n" +
            "16) Import: enter new employees from a data file until the file is empty; \n" +
            "17) Export: Retrieve all customers (in name order) who purchased all products of a particular color and output them to a data file instead of screen; \n" +
            "18) Quit(exit the program)!";
    
    public static void main(String[] args) throws SQLException {

        System.out.println("WELCOME TO THE DATABASE SYSTEM OF MyProducts, Inc.");

        final Scanner sc = new Scanner(System.in); // Scanner is used to collect the user input
        String option = ""; // Initialize user option selection as nothing
        while (!option.equals("18")) { // As user for options until option 18 is selected
            System.out.println(PROMPT); // Print the available options
            option = sc.next(); // Read in the user option selection

            switch (option) { // Switch between different options
                case "1": // Insert a new employee option
                    // Collect the new employee data from the user
                    System.out.println("Please enter employee name:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                    sc.nextLine();
                    final String emp_name = sc.nextLine(); // Read in user input of employee name (white-spaces allowed).

                    System.out.println("Please enter employee address:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                    //sc.nextLine();
                    final String emp_address = sc.nextLine(); // Read in user input of employee address (white-spaces allowed).

                    System.out.println("Please enter employee salary:");
                    
                    final int salary = sc.nextInt(); // Read in user input of employee salary
 

                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement("EXEC New_Employee @emp_name = ?, @emp_address = ?, @salary = ?;")) {
                            // Populate the stored procedure with the data collected from the user
                            statement.setString(1, emp_name);
                            statement.setString(2, emp_address);
                            statement.setInt(3, salary);
                            
                         // No need to Call the stored procedure here
                            //ResultSet resultSet = statement.executeQuery();

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        }
                    }
                    
                    System.out.println("Is employee technical staff?");
                    System.out.println("Enter 1 for Yes 2 for No:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                    sc.nextLine();
                    final String ts_ind = sc.nextLine(); // Read in user input .
                    
                    String QUERY_1a = "";
    				if(ts_ind.equals("1"))
    				{
    					//Set the query to call a procedure to create a Technical Staff employee
    					QUERY_1a = "EXEC New_Employee_Tech ?,?,?,?,?";
    					

    					//Collect the required inputs for the technical staff
    					System.out.println("Please enter technical position :");
    					//sc.nextLine();
                        final String tech_position = sc.nextLine(); // Read in the user input of technical position

    					System.out.println("Does he/she has a BS degree?");
    					System.out.println("Enter 1 for Yes 2 for No:");
    					//sc.nextLine();
    					final String BS_ind = sc.nextLine(); // Read in user input

    					System.out.println("Does he/she has a MS degree?");
    					System.out.println("Enter 1 for Yes 2 for No:");
    					//sc.nextLine();
    					final String MS_ind = sc.nextLine(); // Read in user input 

    					System.out.println("Does he/she has a PhD degree?");
    					System.out.println("Enter 1 for Yes 2 for No:");
    					//sc.nextLine();
    					final String PhD_ind = sc.nextLine(); // Read in the user input

    					System.out.println("Connecting to the database...");
    					// Get a database connection and prepare a query statement
    					try (final Connection connection = DriverManager.getConnection(URL)) 
    					{
    						try (final PreparedStatement statement = connection.prepareStatement(QUERY_1a)) 
    						{
    							// Populate the query template with the data collected from the user
    							statement.setString(1, emp_name);
    							statement.setString(2, tech_position);
    							statement.setString(3, BS_ind);
    							statement.setString(4, MS_ind);
    							statement.setString(5, PhD_ind);

    							System.out.println("Dispatching the query...");
    							// Actually execute the populated query
    							final int rows_inserted = statement.executeUpdate();
    							System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
    						}
    					}
    					
    				}
    					else
    					{
    						// Unrecognized option, re-prompt the user for the correct one
    						System.out.println("No insert into the table.");
    						
    					}
    					
    					System.out.println("Is employee quality controller?");
    					System.out.println("Enter 1 for Yes 2 for No:");
                        // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                        // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                        //sc.nextLine();
                        final String qc_ind = sc.nextLine(); // Read in user input 
                        
                        String QUERY_1b = "";
        				if(qc_ind.equals("1"))
        				{
        					//Set the query to call a procedure to create a Quality Controller employee
        					QUERY_1b = "EXEC New_Employee_QC ?,?";

        					//Collect the required inputs 
        					System.out.println("Please enter product type as Product 1/Product 2/Product 3 :");
        					//sc.nextLine();
                            final String prod_type = sc.nextLine(); // Read in the user input of product type


        					System.out.println("Connecting to the database...");
        					// Get a database connection and prepare a query statement
        					try (final Connection connection = DriverManager.getConnection(URL)) 
        					{
        						try (final PreparedStatement statement = connection.prepareStatement(QUERY_1b)) 
        						{
        							// Populate the query template with the data collected from the user
        							statement.setString(1, emp_name);
        							statement.setString(2, prod_type);

        							System.out.println("Dispatching the query...");
        							// Actually execute the populated query
        							final int rows_inserted = statement.executeUpdate();
        							System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
        						}
        					}
        					
        				}
        					else
        					{
        						// Unrecognized option, re-prompt the user for the correct one
        						System.out.println("No insert into the table.");
        						
        					}
        					
        					System.out.println("Is employee worker?");
        					System.out.println("Enter 1 for Yes 2 for No:");
                            // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                            // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                            //sc.nextLine();
                            final String worker_ind = sc.nextLine(); // Read in user input 
                            
                            String QUERY_1c = "";
            				if(worker_ind.equals("1"))
            				{
            					//Set the query to call a procedure to create a Worker employee
            					QUERY_1c = "EXEC New_Employee_Worker ?,?";

            					//Collect the required inputs 
            					System.out.println("Please enter  maximum number of products a worker can produce per day :");
                                final int max_produce = sc.nextInt(); // Read in the user input for max hrs/day a worker works


            					System.out.println("Connecting to the database...");
            					// Get a database connection and prepare a query statement
            					try (final Connection connection = DriverManager.getConnection(URL)) 
            					{
            						try (final PreparedStatement statement = connection.prepareStatement(QUERY_1c)) 
            						{
            							// Populate the query template with the data collected from the user
            							statement.setString(1, emp_name);
            							statement.setInt(2, max_produce);

            							System.out.println("Dispatching the query...");
            							// Actually execute the populated query
            							final int rows_inserted = statement.executeUpdate();
            							System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
            						}
            					}
            					
            				}
            					else
            					{
            						// Unrecognized option, re-prompt the user for the correct one
            						System.out.println("No insert into the table.");
            						
            					}



                    break;
                case "2": // Insert a new product option
                	System.out.println("Please enter product ID:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                    final int product_ID = sc.nextInt(); // Read in user input 

                    System.out.println("Please enter production date(YYYY-MM-DD):");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                    sc.nextLine();
                    final String prod_date = sc.nextLine(); // Read in user input 

                    System.out.println("Please enter time spent(hh:mm:ss) to make the product:");
                    final String time_taken = sc.nextLine(); // Read in user input 
                    
                    System.out.println("Please enter employee who produced the product:");
                    final String worker_name = sc.nextLine(); // Read in user input 
                    
                    System.out.println("Please enter employee who tested the product:");
                    final String qc_name = sc.nextLine(); // Read in user input 
                    
                    System.out.println("Is the product repaired?");
                    System.out.println("Enter 1 for Yes 2 for No");
                    final String rep_ind = sc.nextLine(); //if the product is repaired then enter repairing details for the associated tables 
                    
                    if(rep_ind.equals("1")) {
                    System.out.println("Please enter employee who repaired the product if product is repaired:");
                    final String ts_name = sc.nextLine();
                    
                    System.out.println("Please enter repair date(YYYY-MM-DD):");
                    final String repair_dt = sc.nextLine();
 
                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement("EXEC New_Product_Repair @product_ID = ?, @prod_date = ?, @time_taken = ?, @worker_name = ?, @qc_name = ?, @ts_name = ?;")) {
                            // Populate the stored procedure with the data collected from the user
                            statement.setInt(1, product_ID);
                            statement.setString(2, prod_date);
                            statement.setString(3, time_taken);
                            statement.setString(4, worker_name);
                            statement.setString(5, qc_name);
                            statement.setString(6, ts_name);
                            
                         // No need to Call the stored procedure here
                            //ResultSet resultSet = statement.executeQuery();

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                            
                        }
                    }
                    
                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement(QUERY_2c)) {
                            // Populate the query with the data collected from the user
                            statement.setInt(1, product_ID);
                            statement.setString(2, ts_name);
                            statement.setString(3, repair_dt);
                            
                         // No need to Call the stored procedure here
                            //ResultSet resultSet = statement.executeQuery();

                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                            
                        }
                    }
                    System.out.println("Is repair requested by a quality controller?");
                    System.out.println("Enter 1 for Yes and 2 for No");
                    final String req_ind = sc.nextLine();
                    
                    if(req_ind.equals("1")) {
                    	System.out.println("Connecting to the database...");
                        // Get a database connection and prepare a query statement
                        try (final Connection connection = DriverManager.getConnection(URL)) {
                            try (
                                final PreparedStatement statement = connection.prepareStatement("EXEC New_Request @product_ID = ?, @qc_name = ?, @ts_name = ?;")) {
                                // Populate the stored procedure with the data collected from the user
                                statement.setInt(1, product_ID);
                                statement.setString(2, qc_name);
                                statement.setString(3, ts_name);
                                
                             // No need to Call the stored procedure here
                                //ResultSet resultSet = statement.executeQuery();

                                System.out.println("Dispatching the query...");
                                // Actually execute the populated query
                                final int rows_inserted = statement.executeUpdate();
                                System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                                
                            }
                        }
                    }
                    else
					{
						// Unrecognized option, re-prompt the user for the correct one
						System.out.println("No insert into the table.");
						
					}	
                    
                    }
                    // if the product is not repaired then product table is updated without any technical staff info
                    else {
                    	System.out.println("Connecting to the database...");
    				// Get a database connection and prepare a query statement
    				try (final Connection connection = DriverManager.getConnection(URL)) 
    				{
    					try (final PreparedStatement statement = connection.prepareStatement(QUERY_2a)) 
    					{
    						// Populate the query template with the data collected from the user
    						statement.setInt(1, product_ID);
    						statement.setString(2, prod_date);
    						statement.setString(3, time_taken);
    						statement.setString(4,  worker_name);
    						statement.setString(5, qc_name);

    						System.out.println("Dispatching the query...");
    						// Actually execute the populated query
    						final int rows_inserted = statement.executeUpdate();
    						System.out.println(String.format("Done. QUERY RESULT : %d", rows_inserted));
    					}
    				}

                    }
                    // the below associated tables are updated with the user info
                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement("EXEC New_Produce @product_ID = ?, @worker_name = ?;")) {
                            // Populate the stored procedure with the data collected from the user
                            statement.setInt(1, product_ID);
                            statement.setString(2, worker_name);
                            
                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        }
                    }
                    
                    System.out.println("Connecting to the database...");
                    // Get a database connection and prepare a query statement
                    try (final Connection connection = DriverManager.getConnection(URL)) {
                        try (
                            final PreparedStatement statement = connection.prepareStatement("EXEC New_Certify @product_ID = ?, @qc_name = ?;")) {
                            // Populate the stored procedure with the data collected from the user
                            statement.setInt(1, product_ID);
                            statement.setString(2, qc_name);
                            
                            System.out.println("Dispatching the query...");
                            // Actually execute the populated query
                            final int rows_inserted = statement.executeUpdate();
                            System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
                        }
                    }
                    
                    System.out.println("Is product of type 1/ type 2/ type 3?");
                    System.out.println("Enter 1 for type 1, 2 for type 2, 3 for type 3:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                    //sc.nextLine();
                    final String p_ind = sc.nextLine(); // Read in user input for product type and update the appropriate product-type table
                    
                    String QUERY_2b = "";
    				if(p_ind.equals("1"))
    				{
    					//Set the query to call a procedure to create Product of type 1
    					QUERY_2b = "EXEC New_Product1 ?,?,?";
    					

    					//Collect the required inputs for the type 1 product
    					System.out.println("Please enter size of the product:");
    					//sc.nextLine();
                        final int size = sc.nextInt(); // Read in the user input 

    					System.out.println("Please enter major software used:");
    					sc.nextLine();
    					final String major_software = sc.nextLine(); // Read in user input 

    					System.out.println("Connecting to the database...");
    					// Get a database connection and prepare a query statement
    					try (final Connection connection = DriverManager.getConnection(URL)) 
    					{
    						try (final PreparedStatement statement = connection.prepareStatement(QUERY_2b)) 
    						{
    							// Populate the query template with the data collected from the user
    							statement.setInt(1, product_ID);
    							statement.setInt(2, size);
    							statement.setString(3, major_software);
    							//statement.setString(4, MS_ind);
    							//statement.setString(5, PhD_ind);

    							System.out.println("Dispatching the query...");
    							// Actually execute the populated query
    							final int rows_inserted = statement.executeUpdate();
    							System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
    						}
    					}
    					break;
    					
    				}
    					
    				else if(p_ind.equals("2"))
    				{
    					//Set the query to call a procedure to create product of type 2
    					QUERY_2b = "EXEC New_Product2 ?,?,?";
    					
    					System.out.println("Please enter size of the product:");
    					final int size = sc.nextInt();
    					
    					System.out.println("Please enter color of the product:");
    					sc.nextLine();
    					final String color = sc.nextLine();

    					System.out.println("Connecting to the database...");
    					// Get a database connection and prepare a query statement
    					try (final Connection connection = DriverManager.getConnection(URL)) 
    					{
    						try (final PreparedStatement statement = connection.prepareStatement(QUERY_2b)) 
    						{
    							// Populate the query template with the data collected from the user
    							statement.setInt(1, product_ID);
    							statement.setInt(2, size);
    							statement.setString(3, color);
    							
    							System.out.println("Dispatching the query...");
    							// Actually execute the populated query
    							final int rows_inserted = statement.executeUpdate();
    							System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
    						}
    					}
    					break;
    					
    				}
    				else if(p_ind.equals("3"))
    				{
    					//Set the query to call a procedure to create a product of type 3
    					QUERY_2b = "EXEC New_Product3 ?,?,?";
    					
    					System.out.println("Please enter size of the product:");
    					final int size = sc.nextInt();
    					
    					System.out.println("Please enter weight of the product:");
    					final int prod_weight = sc.nextInt();

    					System.out.println("Connecting to the database...");
    					// Get a database connection and prepare a query statement
    					try (final Connection connection = DriverManager.getConnection(URL)) 
    					{
    						try (final PreparedStatement statement = connection.prepareStatement(QUERY_2b)) 
    						{
    							// Populate the query template with the data collected from the user
    							statement.setInt(1, product_ID);
    							statement.setInt(2, size);
    							statement.setInt(3, prod_weight);
    							
    							System.out.println("Dispatching the query...");
    							// Actually execute the populated query
    							final int rows_inserted = statement.executeUpdate();
    							System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
    						}
    					}
    					break;
    					
    				}
    				else
    				{
    					// Unrecognized option, re-prompt the user for the correct one
    					System.out.println("Unrecognized option! Please select the option and try again.");
    					break;
    				}
    				
                case "3": // Insert a new customer option
                	System.out.println("Please enter customer name:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                    sc.nextLine();
                	final String cust_name = sc.nextLine(); // Read in user input 

                    System.out.println("Please enter customer address:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                    final String cust_address = sc.nextLine(); 
                    System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) 
					{
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_3a)) 
						{
							// Populate the query template with the data collected from the user
							statement.setString(1, cust_name);
							statement.setString(2, cust_address);
							
							System.out.println("Dispatching the query...");
							// Actually execute the populated query
							final int rows_inserted = statement.executeUpdate();
							System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
						}
					}
					// populating associated purchase table for the new customer
					System.out.println("Enter number of products customer brought:");
					final int n_prod = sc.nextInt();
					
					int i = 0;
					while(i < n_prod) {
						System.out.println("Please enter the product ID");
						final int p_id = sc.nextInt();
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) 
						{
							try (final PreparedStatement statement = connection.prepareStatement(QUERY_3b)) 
							{
								// Populate the query template with the data collected from the user
								statement.setInt(1, p_id);
								statement.setString(2, cust_name);
								
								System.out.println("Dispatching the query...");
								// Actually execute the populated query
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
							}
						}
						i++;
						
					}
					break;
                case "4": // Insert a new account option
                	System.out.println("Please enter account no:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                	final int account_no = sc.nextInt(); // Read in user input 

                    System.out.println("Please enter the date the account established:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                    sc.nextLine();
                    final String est_date = sc.nextLine(); 
                    
                    System.out.println("Please enter the cost of the product:");
                    final int cost = sc.nextInt();
                    
                    System.out.println("Please enter the product ID associated with this account:");
                    final int prod_ID = sc.nextInt();
                    // user-input to recognize the type of account created
                    System.out.println("Please enter the account type:");
                    System.out.println("Enter 1 for product1-account, 2 for product2-account, 3 for product3-account");
                    sc.nextLine();
                    final String acc_ind = sc.nextLine();
                    
                    String QUERY_4 = "";
                    if (acc_ind.equals("1")) {
                    	QUERY_4 = "EXEC New_Account1 ?,?,?,?"; //account is created for product type 1
                    	System.out.println("Connecting to the database...");
    					// Get a database connection and prepare a query statement
    					try (final Connection connection = DriverManager.getConnection(URL)) 
    					{
    						try (final PreparedStatement statement = connection.prepareStatement(QUERY_4)) 
    						{
    							// Populate the query template with the data collected from the user
    							statement.setInt(1, account_no);
    							statement.setString(2, est_date);
    							statement.setInt(3, cost);
    							statement.setInt(4,  prod_ID);
    							
    							System.out.println("Dispatching the query...");
    							// Actually execute the populated query
    							final int rows_inserted = statement.executeUpdate();
    							System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
    						}
    					}
    					break;
                    }
                    else if (acc_ind.equals("2")) {
                    	QUERY_4 = "EXEC New_Account2 ?,?,?,?"; //account is created for product type 2
                    	System.out.println("Connecting to the database...");
    					// Get a database connection and prepare a query statement
    					try (final Connection connection = DriverManager.getConnection(URL)) 
    					{
    						try (final PreparedStatement statement = connection.prepareStatement(QUERY_4)) 
    						{
    							// Populate the query template with the data collected from the user
    							statement.setInt(1, account_no);
    							statement.setString(2, est_date);
    							statement.setInt(3, cost);
    							statement.setInt(4,  prod_ID);
    							
    							System.out.println("Dispatching the query...");
    							// Actually execute the populated query
    							final int rows_inserted = statement.executeUpdate();
    							System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
    						}
    					}
    					break;
                    }
                    else if (acc_ind.equals("3")) { //account is created for product type 3
                    	QUERY_4 = "EXEC New_Account3 ?,?,?,?";
                    	System.out.println("Connecting to the database...");
    					// Get a database connection and prepare a query statement
    					try (final Connection connection = DriverManager.getConnection(URL)) 
    					{
    						try (final PreparedStatement statement = connection.prepareStatement(QUERY_4)) 
    						{
    							// Populate the query template with the data collected from the user
    							statement.setInt(1, account_no);
    							statement.setString(2, est_date);
    							statement.setInt(3, cost);
    							statement.setInt(4,  prod_ID);
    							
    							System.out.println("Dispatching the query...");
    							// Actually execute the populated query
    							final int rows_inserted = statement.executeUpdate();
    							System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
    						}
    					}
    					break;
                    }
                    
					
                    else
    				{
    					// Unrecognized option, re-prompt the user for the correct one
    					System.out.println("Unrecognized option! Please select the option and try again.");
    					break;
    				}
                case "5": // Insert a new complaint option
                	System.out.println("Please enter complaint ID:");
                    
                	final int complaint_ID = sc.nextInt(); // Read in user input 

                    System.out.println("Please enter the date of the complaint:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                    sc.nextLine();
                    final String date_of_complaint = sc.nextLine(); 
                    
                    System.out.println("Please enter complaint description:");
                    final String complaint_desc = sc.nextLine();
                    
                    System.out.println("Please enter treatment expected:");
                    System.out.println("Enter 1 for Refund or 2 for Exchange");
                    final int t_ind = sc.nextInt();
                    
                    System.out.println("Please enter the product ID for which the complaint is raise:");
                    final int pID = sc.nextInt();
                    
                    System.out.println("Please enter the customer name who raised the complaint:");
                    sc.nextLine();
                    final String c_name = sc.nextLine();
                    
                    System.out.println("Please enter the technician name who will work on this complaint");
                    final String t_name = sc.nextLine();
                    
                    System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) 
					{
						try (final PreparedStatement statement = connection.prepareStatement("EXEC New_Complaint @complaint_ID=?, @date_of_complaint=?, @complaint_desc=?, @t_ind=?, @product_ID=?, @cust_name=?, @ts_name=?;")) 
						{
							// Populate the query template with the data collected from the user
							statement.setInt(1, complaint_ID);
							statement.setString(2, date_of_complaint);
							statement.setString(3, complaint_desc);
							statement.setInt(4,  t_ind);
							statement.setInt(5, pID);
							statement.setString(6, c_name);
							statement.setString(7, t_name);
							
							System.out.println("Dispatching the query...");
							// Actually execute the populated query
							final int rows_inserted = statement.executeUpdate();
							System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
						}
					}
                    break;
                    
                case "6": // Insert a new accident option
                	System.out.println("Please enter accident number:");
                    final int accident_no = sc.nextInt(); // Read in user input 

                    System.out.println("Please enter the date of accident:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                    sc.nextLine();
                    final String accident_dt = sc.nextLine(); 
                    
                    System.out.println("Please enter number of work days lost due to the accident:");
                    final int days_lost = sc.nextInt();
                    
                    System.out.println("Please enter product ID with which the accident is associated:");
                    final int prid = sc.nextInt();
                    
                    System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) 
					{
						try (final PreparedStatement statement = connection.prepareStatement("EXEC New_Accident @accident_no=?, @accident_dt=?, @days_lost=?, @product_ID=?;")) 
						{
							// Populate the query template with the data collected from the user
							statement.setInt(1, accident_no);
							statement.setString(2, accident_dt);
							statement.setInt(3, days_lost);
							statement.setInt(4, prid);
							
							System.out.println("Dispatching the query...");
							// Actually execute the populated query
							final int rows_inserted = statement.executeUpdate();
							System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
						}
					}
					// user-input will determine which type of accident has taken place and related space-holders will be updated 
					System.out.println("Please enter reason of accident:");
					System.out.println("1 for Repair or 2 Produce:");
					sc.nextLine();
					final String a_ind = sc.nextLine();
					
					if(a_ind.equals("1")) { 
						System.out.println("Please enter the technical staff involved in the accident:");
						final String rep_acc = sc.nextLine();
						
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) 
						{
							try (final PreparedStatement statement = connection.prepareStatement(QUERY_6a)) 
							{
								// Populate the query template with the data collected from the user
								statement.setInt(1, accident_no);
								statement.setString(2, rep_acc);
								statement.setInt(3,  prid);
								
								System.out.println("Dispatching the query...");
								// Actually execute the populated query
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
							}
						}
						break;
					}
					
					else if(a_ind.equals("2")) {
						System.out.println("Please enter the worker involved in the accident:");
						final String prod_acc = sc.nextLine();
						
						System.out.println("Connecting to the database...");
						// Get a database connection and prepare a query statement
						try (final Connection connection = DriverManager.getConnection(URL)) 
						{
							try (final PreparedStatement statement = connection.prepareStatement(QUERY_6b)) 
							{
								// Populate the query template with the data collected from the user
								statement.setInt(1, accident_no);
								statement.setString(2, prod_acc);
								statement.setInt(3,  prid);
								
								System.out.println("Dispatching the query...");
								// Actually execute the populated query
								final int rows_inserted = statement.executeUpdate();
								System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
							}
						}
						break;
					}
					
					else
    				{
    					// Unrecognized option, re-prompt the user for the correct one
    					System.out.println("Unrecognized option! Please select the option and try again.");
    					break;
    				}
					
                case "7": // the date produced and time spent to produce a particular product option
                	System.out.println("Please enter product ID:");
                    final int pr = sc.nextInt(); // Read in user input 

                    System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) 
					{
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_7)) 
						{
							// Populate the query template with the data collected from the user
							statement.setInt(1, pr);
							
							ResultSet resultSet = statement.executeQuery();
                            System.out.println("Contents of the Product table:");
                            System.out.println("date produced | time spent to produce ");

                            while (resultSet.next()) {
                                System.out.println(String.format("%s | %s  ",
                                    resultSet.getString(1),
                                    resultSet.getString(2)));
                            }
						}
					}
					break;
					
                case "8": // products made by a particular worker option
                	System.out.println("Please enter worker name:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                	sc.nextLine();
                	final String w = sc.nextLine(); // Read in user input 

                    System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) 
					{
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_8)) 
						{
							// Populate the query template with the data collected from the user
							statement.setString(1, w);
							
							ResultSet resultSet = statement.executeQuery();
                            System.out.println("Contents of the Product table:");
                            System.out.println("product ID | date produced | time spent | worker | quality controller | technical staff (if any) ");

                            while (resultSet.next()) {
                                System.out.println(String.format("%s | %s | %s | %s | %s | %s  ",
                                    resultSet.getString(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3),
                                    resultSet.getString(4),
                                    resultSet.getString(5),
                                    resultSet.getString(6)));
                            }
						}
					}
					break;
					
                case "9": // total number of errors a particular quality controller made option
                	System.out.println("Please enter quality controller name:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                	sc.nextLine();
                	final String q = sc.nextLine(); // Read in user input 

                    System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) 
					{
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_9)) 
						{
							// Populate the query template with the data collected from the user
							statement.setString(1, q);
							
							ResultSet resultSet = statement.executeQuery();
                            System.out.println("total no.of errors");

                            while (resultSet.next()) {
                                System.out.println(String.format("%s",
                                    resultSet.getString(1)));
                            }
						}
					}
					break;
					
                case "10": // total costs of the products in the product3 category which were repaired 
                	//at the request of a particular quality controller option
                	System.out.println("Please enter quality controller name:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                	sc.nextLine();
                	final String qc = sc.nextLine(); // Read in user input 

                    System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) 
					{
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_10)) 
						{
							// Populate the query template with the data collected from the user
							statement.setString(1, qc);
							
							ResultSet resultSet = statement.executeQuery();
                            System.out.println("total cost");

                            while (resultSet.next()) {
                                System.out.println(String.format("%s",
                                    resultSet.getString(1)));
                            }
						}
					}
					break;
                case "11": // all customers (in name order) who purchased all products of a particular color option
                	System.out.println("Please enter product color:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                	sc.nextLine();
                	final String c = sc.nextLine(); // Read in user input 

                    System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) 
					{
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_11)) 
						{
							// Populate the query template with the data collected from the user
							statement.setString(1, c);
							
							ResultSet resultSet = statement.executeQuery();
                            System.out.println("customer name | address");

                            while (resultSet.next()) {
                                System.out.println(String.format("%s | %s",
                                    resultSet.getString(1),
                                    resultSet.getString(2)));
                            }
						}
					}
					break;
					
                case "12": // all employees whose salary is above a particular salary option
                	System.out.println("Please enter salary:");
                    final int s = sc.nextInt(); // Read in user input 

                    System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) 
					{
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_12)) 
						{
							// Populate the query template with the data collected from the user
							statement.setInt(1, s);
							
							ResultSet resultSet = statement.executeQuery();
                            System.out.println("employee name | address | salary");

                            while (resultSet.next()) {
                                System.out.println(String.format("%s | %s | %s",
                                    resultSet.getString(1),
                                    resultSet.getString(2),
                                    resultSet.getString(3)));
                            }
						}
					}
					break;
					
                case "13": // total number of work days lost due to accidents in repairing the products which got complaints option
                	System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) 
					{
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_13)) 
						{
							ResultSet resultSet = statement.executeQuery();
                            System.out.println("total number of work days lost");

                            while (resultSet.next()) {
                                System.out.println(String.format("%s",
                                    resultSet.getString(1)));
                            }
						}
					}
					break;
					
                case "14": // average cost of all products made in a particular year option
                	System.out.println("Please enter production year:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                	sc.nextLine();
                	final String yr = sc.nextLine(); // Read in user input 

                    System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) 
					{
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_14)) 
						{
							// Populate the query template with the data collected from the user
							statement.setString(1, yr);
							
							ResultSet resultSet = statement.executeQuery();
                            System.out.println("average cost");

                            while (resultSet.next()) {
                                System.out.println(String.format("%s",
                                    resultSet.getString(1)));
                            }
						}
					}
					break;
					
                case "15": // all accidents whose dates are in some range
                	System.out.println("Please enter start date:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                	sc.nextLine();
                	final String s_dt = sc.nextLine(); // Read in user input 

                	System.out.println("Please enter end date:");
                    // Preceding nextInt, nextFloar, etc. do not consume new line characters from the user input.
                    // We call nextLine to consume that newline character, so that subsequent nextLine doesn't return nothing.
                	final String e_dt = sc.nextLine();
                	
                	System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) 
					{
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_15a)) 
						{
							// Populate the query template with the data collected from the user
							statement.setString(1, s_dt);
							statement.setString(2,  e_dt);
							
							System.out.println("Dispatching the query...");
							
							// Actually execute the delete query for repair related accidents
							final int rows_deleted = statement.executeUpdate();
							System.out.println(String.format("Done. QUERY RESULT :%d", rows_deleted));

                            }
						}
					System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) 
					{
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_15b)) 
						{
							// Populate the query template with the data collected from the user
							statement.setString(1, s_dt);
							statement.setString(2,  e_dt);
							
							System.out.println("Dispatching the query...");
							
							// Actually execute the delete query for production related accidents
							final int rows_deleted = statement.executeUpdate();
							System.out.println(String.format("Done. QUERY RESULT :%d", rows_deleted));

                            }
						}
					System.out.println("Connecting to the database...");
					// Get a database connection and prepare a query statement
					try (final Connection connection = DriverManager.getConnection(URL)) 
					{
						try (final PreparedStatement statement = connection.prepareStatement(QUERY_15c)) 
						{
							// Populate the query template with the data collected from the user
							statement.setString(1, s_dt);
							statement.setString(2,  e_dt);
							
							System.out.println("Dispatching the query...");
							
							// Actually execute the delete query of accident table (primary table)
							final int rows_deleted = statement.executeUpdate();
							System.out.println(String.format("Done. QUERY RESULT :%d", rows_deleted));

                            }
						}
					
					break;
					
                case "16":
    				//Import data from file to Employee Table

    				//Capture file name to store the output
    				System.out.println("Please enter the file name:");
    				sc.nextLine();
    				final String importFile = sc.nextLine(); // Read in user input - file name

    				// Get a database connection and prepare a query statement
    				try (final Connection connection = DriverManager.getConnection(URL)) 
    				{
    					try (final PreparedStatement statement = connection.prepareStatement(QUERY_16)) 
    					{
    						//CSV Reader to read the input file
    						BufferedReader csvFileReader = new BufferedReader(new FileReader(importFile));
    						String currentLine = null;
    						int recordCount = 0;
    						//IGNORE HEADER
    						csvFileReader.readLine();

    						//Loop to read the file till the end
    						while((currentLine = csvFileReader.readLine()) != null)
    						{
    							//currentLine = csvFileReader.readLine();
    							System.out.println(currentLine);
    							String record[] = currentLine.split(",");

    							// Populate data collected from the file
    							statement.setString(1, record[0]);
    							statement.setString(2, record[1]);
    							statement.setInt(3,Integer.parseInt(record[2]));

    							//System.out.println("Dispatching the query...");
    							// Actually execute the populated query
    							statement.executeUpdate();
    							//System.out.println(String.format("Done. %d rows inserted.", rows_inserted));
    							recordCount++;
    						}
    						System.out.println(recordCount+" records have been inserted successfully!");
    						csvFileReader.close(); //Close reader
    						break;
    					}
    					catch (SQLException e) 
    					{
    						System.out.println("Datababse error:");
    						e.printStackTrace();
    					} 
    					catch (IOException e) 
    					{
    						System.out.println("File IO error:");
    						e.printStackTrace();
    					}				
    				}			
    				break;
    				
            	case "17":
    				//Export data from CUSTOMER table
    				//Capture file name to store the output
    				System.out.println("Please enter the file name:");
    				sc.nextLine();
    				final String exportFile = sc.nextLine(); // Read in user input - file name

    				System.out.println("Connecting to the database...");
    				// Get a database connection and prepare a query statement
    				try (final Connection connection = DriverManager.getConnection(URL)) 
    				{
    					try (final PreparedStatement statement = connection.prepareStatement(QUERY_17)) 
    					{
    						//Writer object to write the records to database
    						BufferedWriter writer = new BufferedWriter(new FileWriter(exportFile));

    						// Writing header for the file       
    						writer.write("cust_name,cust_address");
    						System.out.println("Dispatching the query...");

    						//Execute query and get data from database
    						ResultSet result = statement.executeQuery();
    	
    						//Loop to write every record to file
    						while(result.next())
    						{
    							//Extract fields from record to put in place 
    							String export_name = result.getString("cust_name");
    							String export_address = result.getString("cust_address");
    							String record = String.format("\"%s\",%s",export_name, export_address);

    							writer.newLine();
    							//Write record to file
    							writer.write(record);            
    						}
    						//Closing the writer object
    						writer.close();
    						System.out.println("File Export Successful!");
    					}
    					catch (SQLException e) 
    					{
    						System.out.println("Datababse error:");
    						e.printStackTrace();
    					} 
    					catch (IOException e) 
    					{
    						System.out.println("File IO error:");
    						e.printStackTrace();
    					}				
    				}
    				break;
    				
            	case "18": // Do nothing, the while loop will terminate upon the next iteration
                    System.out.println("Exiting! Goodbye!");
                    break;
                    
                default: // Unrecognized option, re-prompt the user for the correct one
                    System.out.println(String.format(
                        "Unrecognized option: %s\n" + 
                        "Please try again!", 
                        option));
                    break;
            }
        }

        sc.close(); // Close the scanner before exiting the application
    }
}