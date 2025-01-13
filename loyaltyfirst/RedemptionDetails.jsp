<%@page import="java.sql.*"%>
<%
DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url="jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection con=DriverManager.getConnection(url,"sjammul3","opuzotch");
Statement stmt=con.createStatement();
ResultSet rs=stmt.executeQuery(
    "SELECT p.p_description, p.points_needed, r.r_date, e.center_name " +
    "FROM Prizes p " +
    "JOIN Redemption_History r ON p.prize_id = r.prize_id " +
    "JOIN ExchgCenters e ON r.center_id = e.center_id " +
    "WHERE r.prize_id = '" + request.getParameter("prizeid") + "' AND r.cid = '" + request.getParameter("cid") + "'"
);
String result = "";
while(rs.next()){
    result += rs.getObject("p_description") + "," + rs.getObject("points_needed") + "," + rs.getObject("r_date") + "," + rs.getObject("center_name") + "#";
}
con.close();
out.print(result);
%>
