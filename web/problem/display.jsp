<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.practiceit.Problem" %>
<%@ page import="com.practiceit.Mystery" %>
<%@ page import="java.util.*" %>

<%
    Problem problem = (Problem) request.getAttribute("problem");

    if(problem != null) {
        session.setAttribute("problem", problem);
    } else {
        // There was an error, let's 404 it
        request.getRequestDispatcher("/problem/404.html").forward(request, response);
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>

    <meta charset="UTF-8">
    <title>Practice-It 2.0</title>
    <script src="${pageContext.request.contextPath}/lib/codemirror.js"></script>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/lib/codemirror.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/theme/lesser-dark.css"/>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css"/>
    <script src="${pageContext.request.contextPath}/mode/clike.js"></script>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/2.1.3/jquery.min.js"></script>
    <script>

        $(window).load(function () {
            $("#solutionForm").submit(function (e) {
                e.preventDefault();

                // Disable form...
                if (codeMirror != null) {
                    codeMirror.save();
                }
                var solutionForm = $("#solutionForm");

                var data = solutionForm.serialize();
                solutionForm.find("input").prop("disabled", true);
                $("#submit").text("Running...");

                var url = "./testing"; // the script where you handle the form input.

                $.ajax({
                    type: "POST",
                    url: url,
                    data: data,
                    // serializes the form's elements.
                    dataType: "json",
                    success: function (data) {
                        parseResults(data);
                    }, error: function () {
                        alert("There was an error checking your answer. Please try again later");
                        enableForm();
                    }
                });

                return false; // avoid to execute the actual submit of the form.
            })
        });

        function parseResults(data) {
            var error = data.error;
            if (error == null) {
                var tests = data.tests;
                if (data.type.toUpperCase() === "MYSTERY") {
                    for (var i = 0; i < tests.length; i++) {
                        var label = $("#label" + (i + 1));
                        switch (tests[i].code) {
                            case -1:
                                label.removeClass();
                                label.addClass("incorrect");
                                break;
                            case 1:
                                label.removeClass();
                                label.addClass("correct");
                                break;
                            case 0:
                                label.removeClass();
                                label.addClass("almost");
                        }
                    }
                } else {
                    clearLines();
                }
            } else {
                switch (error) {
                    case 5:
                        alert("There was an error testing your code on our servers. Please reload the page.");
                        break;
                    case 6:
                        var s = "Compiler Error:\n";
                        clearLines();
                        for (var j = 0; j < data.errors.length; j++) {
                            var x = data.errors[j];
                            codeMirror.addLineClass(x.line - 1, "background", "line-error");
                            s += x.line;
                            s += "\n";
                            s += x.type;
                            s += "\n";
                            s += x.message;
                            s += "\n";
                        }
                        alert(s);
                        break;
                    default:
                        alert(error);
                }
            }

            enableForm();
        }

        function clearLines() {
            for (var i = 0; i < codeMirror.lineCount(); i++) {
                codeMirror.removeLineClass(i, "background", "line-error");
            }
        }

        function enableForm() {
            $("#solutionForm").find("input").prop("disabled", false);
            $("#submit").text("Test It");
        }

        var codeMirror;
        window.onload = function () {
            codeMirror = CodeMirror.fromTextArea(document.getElementsByName("attempt")[0],
                    {
                        indentUnit: 4,
                        tabSize: 4,
                        smartIndent: true,
                        indentWithTabs: false,
                        electricChars: true,
                        showCursorWhenSelecting: true,
                        matchBrackets: true,
                        undoDepth: 99,
                        autofocus: false,
                        extraKeys: {
                            "Tab": "indentMore",
                            "Shift-Tab": "indentLess"
                        },
                        lineNumbers: true,
                        mode: "text/x-java",
                        lineWrapping: true,
                        theme: "lesser-dark"
                    });
        };
    </script>
</head>
<body>
<section class="main">
    <% if(problem != null) { %>
    <h1 class="title"><%= problem.getTitle() %></h1>
    <h3 class="subtitle"><%= problem.getType() %> Problem</h3>
    <section class="description">
        <%= problem.getDescription() %>
    </section>

    <section class="solution">
        <form id="solutionForm" method="post">
            <% if(!(problem instanceof Mystery)) {%>
            <textarea name="attempt" style="width: 50%"></textarea>
            <% } else { %>
            <table>
                <%
                    List<String> labels = Mystery.splitLines(problem.getLabels());
                    for(int i = 0; i < labels.size(); i++) {
                        String label = labels.get(i); %>
                <tr>
                    <td><label for="label<%=i + 1%>"><%=label%>
                    </label></td>
                    <td><input type="text" id="label<%=i + 1%>" name="label<%=i + 1%>"></td>
                </tr>
                <% } %>
            </table>
            <%
                } %>
            <input name="id" type="hidden" value="<%= problem.getId() %>"
                   placeholder="Code"/>
            <button type="submit" id="submit" class="trans">Test It</button>
        </form>
    </section>
    <% } %>
</section>
</body>
</html>