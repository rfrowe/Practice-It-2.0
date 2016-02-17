<%@ page import="com.practiceit.TestHarness" %>
<%@ page import="com.practiceit.Problem" %>
<%@ page import="org.json.simple.JSONObject" %>
<%@ page import="com.practiceit.Mystery" %>
<%--
  Created by IntelliJ IDEA.
  User: Ryan
  Date: 2/13/16
  Time: 14:20
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%!
    @SuppressWarnings("unchecked")
    private static void logException(JSONObject obj, Throwable e, TestHarness.Codes c) {
        JSONObject exception = new JSONObject();
        StackTraceElement[] stack = e.getStackTrace();
        StringBuilder build = new StringBuilder(e.toString() + "\n");
        for(StackTraceElement element : stack) {
            build.append(element.toString());
            build.append('\n');
        }
        exception.put("stacktrace", build.toString());
        exception.put("code", c.code());
        obj.put("exception", exception);
    }
%>

<%

%>
