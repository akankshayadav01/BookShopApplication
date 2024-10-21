
package com.idiot.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebServlet("/register")
public class RegisterServlet extends HttpServlet {
    private static final String query = "INSERT INTO BOOKDATA(ID,BOOKEDITION,BOOKPRICE) VALUES(?,?,?)";
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // Get PrintWriter
        PrintWriter pw = res.getWriter();
        // Set content type
        res.setContentType("text/html");
        
        // Get the book info from the request
        String ID = req.getParameter("ID");
        String bookEdition = req.getParameter("bookEdition");
        
        // Parse the book price and handle any potential NumberFormatException
        float bookPrice = 0.0f;
        try {
            bookPrice = Float.parseFloat(req.getParameter("bookPrice"));
        } catch (NumberFormatException nfe) {
            pw.println("<h2>Error: Book Price is required and must be a valid number!</h2>");
            return;
        }
        
        // Load JDBC driver
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException cnf) {
            cnf.printStackTrace();
            pw.println("<h2>Database Driver not found!</h2>");
            return;
        }
        
        // Generate the connection and execute query
        try (Connection con = DriverManager.getConnection("jdbc:mysql:///book", "root", "root");
             PreparedStatement ps = con.prepareStatement(query)) {
             
            ps.setString(1, ID);
            ps.setString(2, bookEdition);
            ps.setFloat(3, bookPrice);
            
            int count = ps.executeUpdate();
            if (count == 1) {
                pw.println("<h2>Record Registered Successfully</h2>");
            } else {
                pw.println("<h2>Record Not Registered Successfully</h2>");
            }
            
        } catch (SQLException se) {
            se.printStackTrace();
            pw.println("<h2>Error: " + se.getMessage() + "</h2>");
        } catch (Exception e) {
            e.printStackTrace();
            pw.println("<h2>Error: " + e.getMessage() + "</h2>");
        }
        
        // Provide links back to home or book list
        pw.println("<a href='home.html'>Home</a>");
        pw.println("<br>");
        pw.println("<a href='bookList'>Book List</a>");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        doGet(req, res);
    }
}
