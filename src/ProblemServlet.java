package com.practiceit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.sql.SQLException;

/**************************************************
 * @author Ryan Rowe (1531352)
 *         2/14/16
 *         CSE 143 B/BP
 *         TA: Chloe Lathe
 *         Practice-It 2.0
 *         <p>
 *         Servlet for serving problems to users.
 **************************************************/

public class ProblemServlet extends HttpServlet {
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, java.io.IOException {

        try {
            int id = Integer.valueOf(request.getParameter("id"));

            try {
                Problem problem = Problem.getProblem(id);

                // We have successfully gotten the problem, forward it to the display page
                request.setAttribute("problem", problem);
                request.getRequestDispatcher("/problem/display.jsp").forward(request, response);
            } catch(SQLException e) {
                // There was an error connecting to the DB, 500 it
                request.getRequestDispatcher("/problem/500.jsp").forward(request, response);
            }
        } catch(NumberFormatException|IllegalStateException e){
            // There was an error getting the problem ID, 404 it
            // The id is a valid format, but there were no matching rows, 404 it
            request.getRequestDispatcher("/problem/404.html").forward(request, response);
        }
    }
}
