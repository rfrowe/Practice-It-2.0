package com.practiceit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.mdkt.compiler.InMemoryJavaCompiler;

import java.util.*;

/**************************************************
 * @author Ryan Rowe (1531352)
 *         2/13/16
 *         CSE 143 B/BP
 *         TA: Chloe Lathe
 *         Practice-It 2.0
 *         <p>
 *         Description
 **************************************************/

public class MethodReturn extends Problem {
    public MethodReturn(int id, Map<String, String> results) {
        super(id, results);
    }

    public MethodReturn(JSONObject json, String attempt) {
        super(json, attempt);
    }

    @Override
    JSONArray run() {
        return null;
    }

    @Override
    public Map<String, Object> compile() throws Exception {
        Map<String, Object> map = new HashMap<>();

        String solutionType = "com.practiceit." + getType() + "Solution";
        String attemptType = "com.practiceit." + getType();
        System.err.println(getAttempt());
        String attempt = getWrapper().replace("INSERT", getAttempt());
        String solution = getWrapper().replace(getType(), getType() + "Solution").replace("INSERT",
                                                                                getSolution());

        InMemoryJavaCompiler compiler = new InMemoryJavaCompiler();
        try {
            Class c = compiler.compile(attemptType, attempt);
            map.put("attempt", c);
        } catch(ClassFormatError e) {

        }
        if(!compiler.getResult()) {
            System.out.println("failed");
            map.put("errors", TestHarness.diagnosticsToJson(compiler.getDiagnostics(),
                                                            getInsertLine()));
            //System.out.println("failed " + c.getName());
        } else {
            System.out.println("worked");
            //System.out.println(c.getDeclaredMethod("evens").toString());
        }

        map.put("solution", compiler.compile(solutionType, solution));

        return map;
    }
}
