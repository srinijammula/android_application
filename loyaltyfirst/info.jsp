<%@page import="java.sql.*"%>
<%
DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
String url="jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu";
Connection con=DriverManager.getConnection(url,"sjammul3","opuzotch");
Statement stmt=con.createStatement();
ResultSet rs=stmt.executeQuery("SELECT c.cname, p.num_of_points FROM Customers c, Point_Accounts p WHERE c.cid = p.cid AND c.cid = '" + request.getParameter("cid") + "'");
String result="";
while(rs.next()){
    result += rs.getObject("cname") + "," + rs.getObject("num_of_points");
}
con.close();
out.print(result);
%>
