package com.practiceit;

import net.openhft.compiler.CompilerUtils;
import net.openhft.compiler.InMemoryCompilerException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

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

public abstract class CodingProblem extends Problem {
    protected CodingProblem(int id, Map<String, String> results) {
        super(id, results);
    }

    protected CodingProblem(JSONObject json, String attempt) {
        super(json, attempt);
    }

    @Override
    public Map<String, Object> compile() {
        Map<String, Object> map = new HashMap<>();

        String attemptName = getType() + "Attempt";
        String solutionName = getType() + "Solution";
        String attempt = getWrapper().replace(getType(), getType() + "Attempt").replace("INSERT",
                                                                                        getAttempt());
        String solution = getWrapper().replace(getType(), getType() + "Solution").replace
                ("INSERT", getSolution());

        compileClass(map, solutionName, solution, true);
        compileClass(map, attemptName, attempt, false);

        return map;
    }

    private void compileClass(Map<String, Object> results, String
            name, String code, boolean solution) {
        try {
            Class c = CompilerUtils.CACHED_COMPILER.loadFromJava(name, code);
            results.put(((solution) ? "solution" : "attempt"), c);
        } catch(InMemoryCompilerException | ClassNotFoundException e) {
            System.out.println("Failed to compile: com.practiceit." + name);

            JSONArray arr = new JSONArray();
            if(e instanceof InMemoryCompilerException) {
                InMemoryCompilerException ex = (InMemoryCompilerException) e;
                ex.setInsertLine(getInsertLine());
                for(Map<String, Object> error : ex.getErrorList()) {
                    System.out.println(error);
                    arr.add(new JSONObject(error));
                }
            }

            results.put("errors", arr);
        }
    }
}
