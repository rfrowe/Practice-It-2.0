package com.practiceit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**************************************************
 * @author Ryan Rowe (1531352)
 *         2/14/16
 *         CSE 143 B/BP
 *         TA: Chloe Lathe
 *         Practice-It 2.0
 *         <p>
 *         Description
 **************************************************/

public class TestingServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Problem problem = (Problem) request.getSession().getAttribute("problem");
        PrintWriter out = response.getWriter();

        TestHarness testHarness = new TestHarness(problem);

        if(problem == null) {
            JSONObject obj = new JSONObject();
            obj.put("error", TestHarness.Codes.NO_PROBLEM.code());
            out.print(obj);
        } else {
            if(problem instanceof Mystery) {
                StringBuilder attempt = new StringBuilder();
                int count = 1;
                String str = request.getParameter("label" + count);
                while(str != null) {
                    // Replace empties with spaces to fix
                    // regex splitting
                    if(str.equals("")) {
                        str = " ";
                    }

                    attempt.append(str);
                    attempt.append('\n');
                    str = request.getParameter("label" + ++count);
                }
                testHarness.setAttempt(attempt.toString());
            } else {
                testHarness.setAttempt(request.getParameter("attempt"));

                JSONArray errors = testHarness.compile();

                if(errors != null) {
                    JSONObject obj = testHarness.toJSON();
                    obj.put("error", TestHarness.Codes.COMPILER_ERROR.code());
                    obj.put("errors", errors);
                    out.print(obj);
                    out.close();
                }
            }

            try {
                testHarness.run();
            } catch(Exception e) {

            }

            out.print(testHarness.toJSON().toString());
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        doPost(req, resp);
    }
}
