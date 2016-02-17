package com.practiceit;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.*;
import java.util.*;

/**************************************************
 * @author Ryan Rowe (1531352)
 *         2/17/16
 *         CSE 143 B/BP
 *         TA: Chloe Lathe
 *         Practice-It 2.0
 *         <p>
 *         Description
 **************************************************/

public class ListProblems extends HttpServlet {
    /**
     * MySQL Connection Details
     */
    private static final String QUERY = "SELECT * FROM problems ORDER BY id ASC";
    private static final int    TIMEOUT  = 10;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        try {
            DriverManager.registerDriver(new com.mysql.jdbc.Driver());
            DriverManager.setLoginTimeout(TIMEOUT);

            try(Connection conn = DriverManager.getConnection(MySQLConnectionInfo.URL
                                                              + MySQLConnectionInfo.DB,
                                                              MySQLConnectionInfo.USERNAME,
                                                              MySQLConnectionInfo.PASSWORD);
                PreparedStatement statement = conn.prepareStatement(QUERY)) {

                try(ResultSet resultSet = statement.executeQuery()) {
                    List<Map<String, String>> list = Problem.resultSetToMap(resultSet);

                    if(list.size() > 0) {
                        req.setAttribute("list", list);
                        req.getRequestDispatcher("/list.jsp").forward(req, resp);
                    } else {
                        throw new IllegalStateException("No rows found");
                    }
                }
            }
        } catch(SQLException|IllegalStateException e) {
            // Error connecting to MySQL server
            req.getRequestDispatcher("/problem/500.jsp").forward(req, resp);
        }
    }
}
