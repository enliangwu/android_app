<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="leow.project4task2.InfoStore" %>
<%@ page import="leow.project4task2.ActivityLog" %>
<%@ page import="java.util.List" %>
<html>
<head>
    <title>Operation Logs</title>
    <style>
        table, th, td { border: solid 1px #333; }
    </style>
</head>
<body>
    <% InfoStore store = new InfoStore(); %>
    <% List<ActivityLog> logs = store.getActivityLogs(); %>
    <table>
        <tr>
            <th>Action Name</th>
            <th>User</th>
            <th>Action Time</th>
            <th>Activity ID</th>
            <th>Client</th>
            <th>Client Request</th>
            <th>Client Reply</th>
            <th>3rd Request</th>
            <th>3rd Reply</th>
        </tr>
    <% for (ActivityLog log : logs) { %>
        <tr>
            <td><%=log.getActionName()%></td>
            <td><%=log.getUserName()%></td>
            <td><%=log.getActionTime()%></td>
            <td><%=log.getActivityId()%></td>
            <td><%=log.getClientInfo()%></td>
            <td><%=log.getClientRequestInfo()%></td>
            <td><%=log.getClientReplyInfo()%></td>
            <td><%=log.getApiRequestInfo()%></td>
            <td><%=log.getApiReplyInfo()%></td>
        </tr>
    <% } %>
    </table>
</body>
</html>
