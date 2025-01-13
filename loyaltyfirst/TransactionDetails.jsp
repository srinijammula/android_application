<%@page import="java.sql.*"%>
<%
DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url="jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection con=DriverManager.getConnection(url,"sjammul3","opuzotch");
Statement stmt=con.createStatement();
ResultSet rs=stmt.executeQuery(
    "SELECT t.t_date, t.t_points, p.prod_name, p.prod_points, pt.quantity " +
    "FROM Transactions t " +
    "JOIN Transactions_Products pt ON t.tref = pt.tref " +
    "JOIN Products p ON pt.prod_id = p.prod_id " +
    "WHERE t.tref = '" + request.getParameter("tref") + "'"
);
String result = "";
while(rs.next()){
    result += rs.getObject("t_date") + "," + rs.getObject("t_points") + "," + rs.getObject("prod_name") + "," + rs.getObject("prod_points") + "," + rs.getObject("quantity") + "#";
}
con.close();
out.print(result);
%>
