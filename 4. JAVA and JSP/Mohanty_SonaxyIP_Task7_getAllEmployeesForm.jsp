<!DOCTYPE html>
<html>
    <head>
        <meta charset="UTF-8">
        <title>Employee Details</title>
    </head>
    <body>
        <h2>Employee Details</h2>
        <!--
            Form for collecting user input for the employee table.
            Upon form submission, getAllEmployees.jsp file will be invoked.
        -->
        <form action="getAllEmployees.jsp">
            <!-- The form organized in an HTML table for better clarity. -->
            <table border=1>
                <tr>
                    <th colspan="2">Enter the Employee Salary:</th>
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
                    <input type=submit value=Search>
                    </div></td>
                </tr>
            </table>
        </form>
    </body>
</html>
