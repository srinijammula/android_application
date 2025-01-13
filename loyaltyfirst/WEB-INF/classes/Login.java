
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

/**
 *
 * @author sanjanaarekal
 */
@WebServlet("/login")
public class Login extends HttpServlet{

  public void doGet(HttpServletRequest request, HttpServletResponse response){
    
    try{
        String user=request.getParameter("user");
        String pass=request.getParameter("pass");
        
        DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
        Connection conn=DriverManager.getConnection("jdbc:oracle:thin:@artemis.vsnet.gmu.edu:1521/vse18c.vsnet.gmu.edu","sjammul3","opuzotch");
        Statement stmt=conn.createStatement();
        ResultSet rs=stmt.executeQuery("Select * from login where username='"+user+"' AND passwd='"+pass+"'");
        PrintWriter out=response.getWriter();
        String result="";
        while(rs.next()){
            result+="Yes:"+rs.getObject(1);
            
        }
        if(result.equals("")){
            out.print("No");
        }
        else{
            out.print(result);
        }
    }
    catch(Exception e){}
    }
}
