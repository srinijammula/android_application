<%@page import="java.sql.*"%>
<%
DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url="jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection con=DriverManager.getConnection(url,"sjammul3","opuzotch");
Statement stmt=con.createStatement();
ResultSet rs=stmt.executeQuery(
    "SELECT DISTINCT prize_id " +
    "FROM Redemption_History " +
    "WHERE cid = '" + request.getParameter("cid") + "'"
);
String result = "";
while(rs.next()){
    result += rs.getObject(1) + "#";
}
con.close();
out.print(result);
%>
