<%@ page import="com.alibaba.fastjson.JSON" %>
<%@ page import="io.github.hylexus.yassos.core.session.YassosSession" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
            + path + "/";
%>
<html>
<head>
    <base href="<%=basePath%>">
    <meta charset="UTF-8">
    <title>protected-resource</title>
    <link href="https://cdn.bootcss.com/twitter-bootstrap/3.4.1/css/bootstrap.min.css" rel="stylesheet">
    <%
        YassosSession yassosCurrentSession = (YassosSession) request.getSession().getAttribute("yassos_current_session");
    %>
</head>
<body class="container">
<div class="col-sm-offset-2">
    <h2>Protected Page</h2>
    <hr/>
    <div>
        <h4>current yassos session info</h4>
        <pre>
        <%=JSON.toJSONString(yassosCurrentSession, true)%>
        </pre>
    </div>

    <div>
        <%
            Object obj = request.getSession().getAttribute("yassos_current_session");
            if (obj != null && ((YassosSession) obj).isValid()) {
        %>
        <a class="btn btn-danger" href="logout">Sign-out</a>
        <%
            }
        %>
        <button onclick="loadCookie('x-yassos-token')" class="btn btn-info">getCookie</button>
        <a class="btn btn-success" href="index.jsp">to index.jsp</a>
        <br/>
        <pre id="cookie-text" style="margin-top: 5px;"></pre>
    </div>

</div>
</body>
<script>
    function loadCookie(key) {
        var cookieValue = getCookie(key);
        document.getElementById("cookie-text").textContent = "cookie : [ " + key + " = " + cookieValue + " ]";
    }

    function getCookie(name) {
        var index = document.cookie.indexOf(name);
        if (index === -1) {
            return "";
        }
        index += name.length + 1;
        var end = document.cookie.indexOf(";", index);
        if (end === -1) {
            end = document.cookie.length;
        }
        return unescape(document.cookie.substring(index, end));
    }
</script>
</html>
