<%@ page import="com.alibaba.fastjson.JSON" %>
<%@ page import="io.github.hylexus.yassos.core.session.YassosSession" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>protected-resource</title>
</head>
<body>
<h2>Protected Page</h2>
<%
    YassosSession yassosCurrentSession = (YassosSession) request.getSession().getAttribute("yassos_current_session");
%>
<h3>current yassos session info</h3>
<pre>
<%=JSON.toJSONString(yassosCurrentSession, true)%>
</pre>
<a href="../index.jsp">to index.jsp</a>
</body>
</html>
