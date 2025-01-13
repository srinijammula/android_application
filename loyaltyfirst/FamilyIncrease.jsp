<%@page import="java.sql.*"%>
<%
DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url="jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection con=DriverManager.getConnection(url,"sjammul3","opuzotch");
Statement stmt=con.createStatement();
int rowsUpdated = stmt.executeUpdate(
    "UPDATE Point_Accounts " +
    "SET num_of_points = num_of_points + " + request.getParameter("npoints") + " " +
    "WHERE family_id = '" + request.getParameter("fid") + "' " +
    "AND cid != '" + request.getParameter("cid") + "'"
);
con.close();
out.print("Point accounts updated successfully: " + rowsUpdated + " rows updated");
%>
