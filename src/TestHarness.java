package com.practiceit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.*;

/**************************************************
 * @author Ryan Rowe (1531352)
 *         2/12/16
 *         CSE 143 B/BP
 *         TA: Chloe Lathe
 *         Practice-It 2.0
 *         <p>
 *         Description
 **************************************************/

public class TestHarness {

    private Problem problem;
    private JSONArray tests;

    public TestHarness(Problem problem) {
        this.problem = problem;
    }

    /*public TestHarness(JSONObject json, String attempt) {
        this.problem = Problem.getProblem(json, attempt);
    }*/

    public Problem getProblem() {
        return this.problem;
    }

    public JSONArray compile() {
        Map<String, Object> map = problem.compile();
        return (JSONArray) map.get("errors");
    }

    public void setAttempt(String attempt) {
        problem.setAttempt(attempt);
    }

    public void run() throws Exception {
        try {
            this.tests = problem.run();
        } catch(Exception e) {
            throw e;
        }
        /* try {
            Method method = problemClass.getMethod(problem.getMethod());

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            PrintStream old = System.out;
            System.setOut(new PrintStream(baos));

            try {
                if(Modifier.isStatic(method.getModifiers())) {
                    method.invoke(null);
                } else {
                    method.invoke(problemClass.newInstance());
                }
            } catch(Exception e) {
                System.setOut(old);
                throw e;
            }
            JSONObject obj = new JSONObject();
            obj.put("output", baos.toString());

            return obj;
        } catch(Exception e) {
            throw e;
        } */
    }

    public enum Codes {
        NO_PROBLEM(5),
        COMPILER_ERROR(6);

        private int value;

        Codes(int value) {
            this.value = value;
        }

        public int code() {
            return value;
        }
    }

    public JSONObject toJSON() {
        JSONObject obj = new JSONObject();
        obj.put("type", problem.getType());
        if(tests != null) {
            obj.put("tests", tests);
        }
        return obj;
    }

    @SuppressWarnings("unchecked")
    public static JSONArray diagnosticsToJson(List<Diagnostic<? extends JavaFileObject>> diags,
                                              int insertLine) {
        JSONArray json = new JSONArray();

        for(Diagnostic diag : diags) {
            JSONObject diagnostic = new JSONObject();
            diagnostic.put("kind", diag.getKind().toString());
            diagnostic.put("line", diag.getLineNumber() - insertLine + 1);
            diagnostic.put("message", diag.getMessage(Locale.US));
            json.add(diagnostic);
        }

        return json;
    }
}
