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

    // Get the attribute values passed from the input form.
    String emp_name = request.getParameter("emp_name");
    String emp_address = request.getParameter("emp_address");
    String salaryString = request.getParameter("emp_sal");
    

    /*
     * If the user hasn't filled out all the attributes, form will again be invoked
     */
    if (emp_name.equals("") || emp_address.equals("") || salaryString.equals("")) {
        response.sendRedirect("addEmployeeForm.jsp");
    } else {
        int emp_sal = Integer.parseInt(salaryString);
        
        // Now perform the query with the data from the form.
        boolean success = handler.addEmployee(emp_name, emp_address, emp_sal);
        if (!success) { // Something went wrong
            %>
                <h2>There was a problem inserting the employee</h2>
            <%
        } else { // Confirm success to the user
            %>
            <h2>Employee:</h2>

            <ul>
                <li>Name: <%=emp_name%></li>
                <li>Address: <%=emp_address%></li>
                <li>Salary: <%=salaryString%></li>
                
            </ul>

            <h2>Was successfully inserted.</h2>
            
            
            <%
        }
    }
    %>
    </body>
</html>
