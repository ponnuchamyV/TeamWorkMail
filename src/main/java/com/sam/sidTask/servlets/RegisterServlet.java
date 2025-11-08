package com.sam.sidTask.servlets;

import com.sam.sidTask.servlets.RegisterServlet;
import com.sam.sidTask.util.DbUtil;
import com.sam.sidTask.util.MailUtil;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

@WebServlet(name = "RegisterServlet", urlPatterns = {"/register"})
public class RegisterServlet extends HttpServlet {

    // SQL - INSERT into your existing table. Adjust table/column names to your DB schema.
    private static final String INSERT_SQL = "INSERT INTO registrations (firstname, lastname, email, phone) VALUES (?, ?, ?, ?)";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");

        String firstname = req.getParameter("firstname");
        String lastname  = req.getParameter("lastname");
        String email     = req.getParameter("email");
        String phone     = req.getParameter("phone");

        // Basic server-side validation
        if (firstname == null || lastname == null || email == null || phone == null ||
                firstname.isBlank() || lastname.isBlank() || email.isBlank() || phone.isBlank()) {
            req.setAttribute("message", "All fields are required.");
            req.getRequestDispatcher("/success.jsp").forward(req, resp);
            return;
        }

        // Store into DB
        try (Connection conn = DbUtil.getConnection();
             PreparedStatement ps = conn.prepareStatement(INSERT_SQL)) {

            ps.setString(1, firstname);
            ps.setString(2, lastname);
            ps.setString(3, email);
            ps.setString(4, phone);

            int rows = ps.executeUpdate();
            if (rows == 0) {
                req.setAttribute("message", "Failed to register (DB insert returned 0 rows).");
                req.getRequestDispatcher("/success.jsp").forward(req, resp);
                return;
            }

        } catch (SQLException ex) {
            ex.printStackTrace();
            req.setAttribute("message", "Database error: " + ex.getMessage());
            req.getRequestDispatcher("/success.jsp").forward(req, resp);
            return;
        }

        // Send confirmation email
        try {
            String subject = "Registration Successful";
            String body = String.format("Hello %s %s,\n\nThank you for registering.\n\nYour details:\nName: %s %s\nEmail: %s\nPhone: %s\n\nRegards,\nMyApp",
                    firstname, lastname, firstname, lastname, email, phone);

            MailUtil.sendEmail(email, subject, body);

        } catch (Exception e) {
            e.printStackTrace();
            // Not fatal for the registration flow; inform user but still mark registration success
            req.setAttribute("message", "Registered successfully, but failed to send email: " + e.getMessage());
            req.getRequestDispatcher("/success.jsp").forward(req, resp);
            return;
        }

        req.setAttribute("message", "Registered successfully. Confirmation email sent to " + email + ".");
        req.getRequestDispatcher("/success.jsp").forward(req, resp);
    }
}
