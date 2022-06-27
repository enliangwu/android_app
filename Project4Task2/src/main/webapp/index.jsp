<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="leow.project4task2.InfoStore" %>
<%@ page import="java.util.*" %>
<% InfoStore store = new InfoStore(); %>
<%
    store.getAnalysis();
    int limit = 10;
%>

<!DOCTYPE html>
<html>
<head>
    <title>Dashboard</title>
    <style>
        table, th, td { border: solid 1px #333; }
    </style>
</head>
<body>
    <a href="logs.jsp">View All logs</a>
    <section>
        <h1>High frequency Activity Types TOP 10</h1>
        <table>
            <tr>
                <th>
                    Rank
                </th>
                <th>
                    Activity Type
                </th>
                <th>
                    Get Times
                </th>
            </tr>
            <% limit = 0; %>
            <% for (Map.Entry<String, Integer> item : store.getSortedHighFrequencyActivityTypes().entrySet()) { %>
            <% if (limit >= 10) break; %>
            <tr>
                <td>
                    <%=limit + 1%>
                </td>
                <td>
                    <%=item.getKey()%>
                </td>
                <td>
                    <%=item.getValue()%>
                </td>
            </tr>
            <% limit++; %>
            <% } %>
        </table>
    </section>
    <section>
        <h1>Just DO IT Activities TOP 10</h1>
        <table>
            <tr>
                <th>
                    Rank
                </th>
                <th>
                    Activity Name
                </th>
                <th>
                    Times
                </th>
            </tr>
            <% limit = 0; %>
            <% for (Map.Entry<String, Integer> item : store.getSortedDoItActivities().entrySet()) { %>
            <% if (limit >= 10) break; %>
            <tr>
                <td>
                    <%=limit + 1%>
                </td>
                <td>
                    <%=item.getKey()%>
                </td>
                <td>
                    <%=item.getValue()%>
                </td>
            </tr>
            <% limit++; %>
            <% } %>
        </table>
    </section>
    <section>
        <h1>DISLIKE Activities TOP 10</h1>
        <table>
            <tr>
                <th>Rank</th>
                <th>Activity Name</th>
                <th>Times</th>
            </tr>
            <% limit = 0; %>
            <% for (Map.Entry<String, Integer> item : store.getSortedDislikeActivities().entrySet()) { %>
            <% if (limit >= 10) break; %>
            <tr>
                <td>
                    <%=limit + 1%>
                </td>
                <td>
                    <%=item.getKey()%>
                </td>
                <td>
                    <%=item.getValue()%>
                </td>
            </tr>
            <% limit++; %>
            <% } %>
        </table>
    </section>
</body>
</html>