<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Add Employee</title>
    </head>
    <body>
        <h2>Add Employee</h2>
        <!--
            Form for collecting user input for the new employee record.
            Upon form submission, addEmployee.jsp file will be invoked.
        -->
        <form action="addEmployee.jsp">
            <!-- The form organized in an HTML table for better clarity. -->
            <table border=1>
                <tr>
                    <th colspan="2">Enter the Employee Data:</th>
                </tr>
                <tr>
                    <td>Employee Name:</td>
                    <td><div style="text-align: center;">
                    <input type=text name=emp_name>
                    </div></td>
                </tr>
                <tr>
                    <td>Address:</td>
                    <td><div style="text-align: center;">
                    <input type=text name=emp_address>
                    </div></td>
                </tr>
                <tr>
                    <td>Salary:</td>
                    <td><div style="text-align: center;">
                    <input type=text name=emp_sal>
                    </div></td>
                </tr>
                <tr>
                    <td><div style="text-align: center;">
                    <input type=reset value=Clear>
                    </div></td>
                    <td><div style="text-align: center;">
                    <input type=submit value=Insert>
                    </div></td>
                </tr>
            </table>
        </form>
    </body>
</html>
