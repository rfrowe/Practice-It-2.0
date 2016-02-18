package com.practiceit;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

public class Mystery extends Problem {
    public Mystery(int id, Map<String, String> results) {
        super(id, results);
    }

    public Mystery(JSONObject json, String attempt) {
        super(json, attempt);
    }

    @SuppressWarnings("unchecked")
    public JSONArray run() {
        JSONArray tests = new JSONArray();

        List<String> solutions = splitLines(getOutput());
        List<String> answers = splitLines(getAttempt());

        for(int i = 0; i < Math.min(solutions.size(), answers.size()); i++) {
            int code = -1;
            if(solutions.get(i).equals(answers.get(i))) {
                code = 1;
            } else if(strip(solutions.get(i)).equalsIgnoreCase(strip(answers.get(i)))) {
                code = 0;
            }

            JSONObject test = new JSONObject();
            test.put("code", code);
            test.put("output", answers.get(i));
            tests.add(test);
        }

        return tests;
    }

    public static List<String> splitLines(String str) {
        return new ArrayList<>(Arrays.asList(str.split("[\\r\\n]")));
    }

    private String strip(String str) {
        return str.replaceAll("[ \t]+", "");
    }

    @Override
    public Map<String, Object> compile() {
        return null;
    }
}
