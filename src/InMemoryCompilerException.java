package com.practiceit;

import javax.tools.Diagnostic;
import javax.tools.JavaFileObject;
import java.util.*;

/**************************************************
 * @author Ryan Rowe (1531352)
 *         2/15/16
 *         InMemoryJavaCompiler
 **************************************************/

public class InMemoryCompilerException extends Exception {

    private List<Diagnostic<? extends JavaFileObject>> diags;
    private int                                        line;

    public InMemoryCompilerException(List<Diagnostic<? extends JavaFileObject>> diags) {
        this.diags = diags;
        line = -1;
    }

    public List<Map<String, Object>> getErrorList() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        for(Diagnostic diag : diags) {
            Map<String, Object> diagnostic = new HashMap<String, Object>();

            diagnostic.put("kind", diag.getKind().toString());
            diagnostic.put("line", diag.getLineNumber() - line + 1);
            diagnostic.put("message", diag.getMessage(Locale.US));

            list.add(diagnostic);
        }

        return list;
    }

    public void setInsertLine(int line) {
        this.line = line;
    }
}
