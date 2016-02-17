<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.practiceit.Mystery" %>
<%@ page import="java.util.*" %>

<%
    @SuppressWarnings("unchecked")
    List<Map<String, String>> list = (List<Map<String, String>>) request.getAttribute("list");

    if(list == null) {
        // There was an error, serverside 500
        request.getRequestDispatcher("/problem/500.jsp").forward(request, response);
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>

    <meta charset="UTF-8">
    <title>Practice-It 2.0</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/style.css"/>
</head>
<body>
<section class="main">
    <h1 class="title">Welcome to Practice-It 2.0</h1>
    <table class="problemList">
        <tr>
            <th>ID</th>
            <th>Title</th>
            <th>Type</th>
        </tr>
    <% if(list != null) {
        for(Map<String, String> problem : list) { %>
            <tr>
                <td class="id"><%= problem.get("id")%></td>
                <td><a href="${pageContext.request.contextPath}/problem?id=<%= problem.get("id")%>"><%= problem.get("title")%></a></td>
                <td><%= problem.get("type")%></td>
            </tr>
    <%  } %>
    </table>
    <% } %>
</section>
</body>
</html>