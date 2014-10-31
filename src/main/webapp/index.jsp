<%@ page language="java" import="java.util.*" pageEncoding="utf8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=utf8" />
    <title>聊天室</title>
    <script src="jquery/jquery-1.4.3.min.js"></script>
    <script type="text/javascript">
        var me = "${name}";
        var index = 0;
        var index_1 = 0;
        $(function(){
            $('#msg').bind('keypress',function(event){
                if(event.keyCode == "13")
                {
                    var msg = $('#msg').val();
                    send(msg);
                    $('#msg').val("");
                }
            });
            $('#msg_1').bind('keypress',function(event){
                if(event.keyCode == "13")
                {
                    var msg = $('#msg_1').val();
                    send_1(msg);
                    $('#msg_1').val("");
                }
            });
            window.setInterval("read();",1000);
            window.setInterval("read_1();",1000);
            window.setInterval("refreshleft()",2000);
        });
        function send(_msg){
            $.ajax({
                url: "/chatroom",
                type: "post",
                data: "text="+_msg,
                async: false
            });
        }
        function send_1(_msg){
            $.ajax({
                url: "/chat?name="+$("#who").html(),
                type: "post",
                data: "text="+_msg,
                async: false
            });
        }
        function read(){
            $.ajax({
                url: "/chatroom",
                dataType: "json",
                type: "get",
                data: "index="+index,
                async: false ,
                success: function (msges){
                    var len=msges.length;
                    if(len>0){
                        index = index+len;
                    }
                    for(var i=0;i<len;i++){
                        var msg = msges[i]["content"];
                        var time = msges[i]["time"];
                        var author = msges[i]["author"];
                        var div = '<div>'+author+'于'+getTimeStr(time)+'说：'+msg+'</div>';
                        $(".chatroom").append(div);
                    }
                }
            });
        }
        function read_1(){
            $.ajax({
                url: "/chat?name="+$("#who").html(),
                dataType: "json",
                type: "get",
                data: "index="+index_1,
                async: false ,
                success: function (msges){
                    var len=msges.length;
                    if(len>0){
                        index_1 = index_1+len;
                    }
                    for(var i=0;i<len;i++){
                        var msg = msges[i]["content"];
                        var time = msges[i]["time"];
                        var author = msges[i]["author"];
                        var div = '<div>'+author+'于'+getTimeStr(time)+'说：'+msg+'</div>';
                        $(".chatuser").append(div);
                    }
                }
            });
        }
        function refreshleft(){
            $.ajax({
                url: "/user",
                dataType: "json",
                type: "get",
                data: "index="+index,
                async: false ,
                success: function (users){
                    var len=users.length;
                    $(".center_left").html("");
                    for(var i=0;i<len;i++){
                        var name = users[i];
                        var div = '<div class="user" onclick="showname(this);">'+name+'</div>';
                        $(".center_left").append(div);
                    }
                }
            });
        }
        function showname(_this){
            var name = $(_this).html();
            if(me!=name){
                $("#who").html(name);
            }
        }
        function getTimeStr(_time){
            var dateTime = new Date(_time);
            var timestr = dateTime.getHours()+':'+dateTime.getMinutes()+':'+dateTime.getSeconds();
            return timestr;
        }
    </script>
    <style type="text/css">
        .top{
            width: 1000px;height: 100px;
        }
        .center{
            width: 1000px;height: 500px;
        }
        .center_left{
            float: left;
            background-color: #aadddd;
            width: 200px;
            height: 500px;
        }
        .center_right{
            float: right;
            background-color: #dddddd;
            width: 800px;
            height: 500px;
        }
        .user{
            background-color:#09aafa;
            border: 1px solid #CC9933;
        }
        .chatroom{
            color: #ffffff;
            overflow: auto;
            width: 800px;
            height: 260px;
            background-color:#000000;
        }
        .chatuser{
            color: #ffffff;
            overflow: auto;
            width: 800px;
            height: 160px;
            background-color:#000000;
        }
        .paneltitle{
            width: 800px;
            height: 20px;
            background-color:#CC9933;
        }
        .inputpanel{
            color: #ffffff;
            width: 800px;
            height: 20px;
            background-color:#000000;
        }
    </style>
</head>
<body>
<div class="top">
    <div style="float: left;"><h1>聊天室页面</h1></div>
    <div style="float: right;">[用户<span style="color: #aa0000">${name}</span>][<a href="login">退出</a>]</div>
</div>
<div class="center">
    <div class="center_left"></div>
    <div class="center_right">
        <div class="paneltitle">公聊</div>
        <div class="chatroom"></div>
        <div class="inputpanel" style="text-align: right">
            <input id="msg" type="text" style="width: 250px"/>
        </div>
        <div class="paneltitle">私聊-[您正在和<span style="color: #aa0000" id="who"></span>聊天]</div>
        <div class="chatuser"></div>
        <div class="inputpanel" style="text-align: right">
            <input id="msg_1" type="text" style="width: 250px"/>
        </div>
    </div>
</div>
</body>
</html>