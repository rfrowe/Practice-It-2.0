package com.practiceit;

import javax.tools.*;
import javax.tools.JavaCompiler.CompilationTask;
import java.net.URI;
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

public class InMemoryJavaCompiler {
    private static final Iterable<String> options = Collections.singletonList("-Xlint:unchecked");
    private DiagnosticCollector<JavaFileObject> diagnostics;
    private JavaCompiler                        compiler;

    public InMemoryJavaCompiler() {
        diagnostics = new DiagnosticCollector<>();
        compiler = ToolProvider.getSystemJavaCompiler();
    }

    public Class compileString(String className, String java) throws
                                                              InMemoryCompilerException,
                                                              ClassNotFoundException {

        JavaFileObject file = new JavaSourceFromString(className, java);

        Iterable<? extends JavaFileObject> compilationUnits = Arrays.asList(file);
        try {

            CompilationTask task =
                    compiler.getTask(null, null, diagnostics, options, null, compilationUnits);

            boolean success = task.call();

            System.out.println(" Success: " + success);

            List<Diagnostic<? extends JavaFileObject>> diagnosticList =
                    diagnostics.getDiagnostics();

            if(success && diagnosticList.isEmpty()) {
                return Class.forName(className);
            } else {
                throw new InMemoryCompilerException(diagnosticList);
            }

        } catch(ClassFormatError e) {
            throw new InMemoryCompilerException(diagnostics.getDiagnostics());
        }
    }

    private static class JavaSourceFromString extends SimpleJavaFileObject {
        final String code;

        JavaSourceFromString(String name, String code) {
            super(URI.create("string:///" + name.replace('.', '/') + Kind.SOURCE.extension),
                  Kind.SOURCE);
            this.code = code;
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) {
            return code;
        }
    }
}
