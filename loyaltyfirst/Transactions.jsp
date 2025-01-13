<%@page import="java.sql.*"%>
<%
DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url="jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection con=DriverManager.getConnection(url,"sjammul3","opuzotch");
Statement stmt=con.createStatement();
ResultSet rs=stmt.executeQuery(
    "SELECT t.tref, t.t_date, t.t_points, t.amount " +
    "FROM Transactions t JOIN Customers c ON t.cid = c.cid " +
    "WHERE c.cid = '" + request.getParameter("cid") + "'"
);
String result = "";
while(rs.next()){
    result += rs.getObject("tref") + "," + rs.getObject("t_date") + "," + rs.getObject("t_points") + "," + rs.getObject("amount") + "#";
}
con.close();
out.print(result);
%>
