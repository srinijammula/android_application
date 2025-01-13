<%@page import="java.sql.*"%>
<%
DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url="jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection con=DriverManager.getConnection(url,"sjammul3","opuzotch");
Statement stmt=con.createStatement();
ResultSet rs=stmt.executeQuery(
    "SELECT f.family_id, p.percent_added, t.t_points " +
    "FROM Families f " +
    "JOIN Point_Accounts p ON f.family_id = p.family_id " +
    "JOIN Transactions t ON p.point_acct_no = t.point_acct_no " +
    "WHERE p.cid = '" + request.getParameter("cid") + "' AND t.tref = '" + request.getParameter("tref") + "'"
);
String result = "";
while(rs.next()){
    result += rs.getObject("family_id") + "," + rs.getObject("percent_added") + "," + rs.getObject("t_points") + "#";
}
con.close();
out.print(result);
%>
