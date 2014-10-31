<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
    <title>登录页面</title>
</head>
<body>
<div>
    <div>
        <form action="/login" method="post">
            用户名:<input name="name" type="text"/><input type="submit" value="提交"/>
        </form>
    </div>
    <div>
        <a href="/index.jsp">直接进入聊天室</a> <a href="/monitor.jsp">进入监控页面</a>
    </div>
</div>
</body>
</html>
