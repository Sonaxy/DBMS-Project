<%@ page language="java" contentType="text/html; charset=UTF-8"
pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Query Result</title>
</head>
    <body>
    <%@page import="IP_jsp.DataHandler"%>
    <%@page import="java.sql.ResultSet"%>
    <%@page import="java.sql.Array"%>
    <%
    // The handler is the one in charge of establishing the connection.
    DataHandler handler = new DataHandler();
	
    //Get the attribute from the user
    String salaryString = request.getParameter("emp_sal");
    
    int emp_sal = Integer.parseInt(salaryString);
	 final ResultSet Employee = handler.getAllEmployees(emp_sal);
	%>
	<!-- The table for displaying all the employee records -->
	<table cellspacing="2" cellpadding="2" border="1">
		<tr>
			<!-- The table headers row -->
			<td align="center">
				<h4>Employee Name</h4>
			</td>
			<td align="center">
				<h4>Address</h4>
			</td>
			<td align="center">
				<h4>Salary</h4>
			</td>
		</tr>
		<%
                while(Employee.next()) { // For each employee record returned...
                        // Extract the attribute values for every row returned
                        final String name = Employee.getString("emp_name");
                        final String address = Employee.getString("emp_address");
                        final String salary = Employee.getString("emp_sal");
                        
                        out.println("<tr>"); // Start printing out the new table row
                        out.println( // Print each attribute value
                             "<td align=\"center\">" + name +
                             "</td><td align=\"center\"> " + address +
                             "</td><td align=\"center\"> " + salary + "</td>");
                        out.println("</tr>");
                    }
                    %>
	</table>
</body>
</html>