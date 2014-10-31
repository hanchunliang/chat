<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>聊天监控页面</title>
    <script src="jquery/jquery-1.4.3.min.js"></script>
    <script src="jquery/My97DatePicker/WdatePicker.js" type="text/javascript"></script>
    <script type="text/javascript">
        var index = 0;
        var dialogId = '';
        function search(){
            var from = $("#from").val();
            var to = $("#to").val();
            var start = $("#start").val();
            var end = $("#end").val();
            $.ajax({
                url: "/monitor",
                dataType: "json",
                type: "get",
                data: "from="+from+"&to="+to+"&start="+start+"&end="+end,
                async: false ,
                success: function (dialogs){

                    var len=dialogs.length;
                    var htm = '<tr class="trhead"><th>对话组</th><th>对话Id</th><th>开始时间</th><th>发启人</th><th>接收人</th><th colspan="2">操作</th></tr>';
                    for(var i=0;i<len;i++){
                        var group = dialogs[i]["group"];
                        var id = dialogs[i]["id"];
                        var from = dialogs[i]["from"];
                        var to = dialogs[i]["to"];
                        var time = dialogs[i]["time"];
                        var htm = htm + '<tr><td>'+group+'</td><td>'+id+'</td><td>'+time+'</td><td>'+from+'</td><td>'+to+'</td><td onclick="showDetail(\''+id+'\');">监听</td><td onclick="del(\''+id+'\');">终止</td></tr>';

                    }
                    $("#table").html("");
                    $("#table").append(htm);
                }
            });
        }
        function showDetail(_id){
            dialogId = _id;
            $(".chatuser").html("");
        }
        function read_1(){
            $.ajax({
                url: "/monitor",
                dataType: "json",
                type: "get",
                data: "type=1&did="+dialogId+"&index=0",
                async: false ,
                success: function (msges){
                    var len=msges.length;
                    if(len>0){
                        index = index+len;
                    }
                    var div = '';
                    for(var i=0;i<len;i++){
                        var msg = msges[i]["content"];
                        var time = msges[i]["time"];
                        var author = msges[i]["author"];
                        div = div + '<div>'+author+'于'+getTimeStr(time)+'说：'+msg+'</div>';
                    }
                    $(".chatuser").html("");
                    $(".chatuser").append(div);
                }
            });
        }
        window.setInterval("read_1();",1000);
        function del(_id){
            $.ajax({
                url: "/monitor",
                dataType: "json",
                type: "post",
                data: "did="+_id,
                async: false
            });
        }
        function send_1(_msg){
            $.ajax({
                url: "/monitor?type=1",
                type: "post",
                data: "text="+_msg+"&did="+dialogId,
                async: false
            });
        }
        $(function(){
            $('#msg_1').bind('keypress',function(event){
                if(event.keyCode == "13")
                {
                    var msg = $('#msg_1').val();
                    send_1(msg);
                    $('#msg_1').val("");
                }
            });
        });
        function getTimeStr(_time){
            var dateTime = new Date(_time);
            var timestr = dateTime.getHours()+':'+dateTime.getMinutes()+':'+dateTime.getSeconds();
            return timestr;
        }
    </script>
    <style type="text/css">
        .top{
            width: 1000px;
            height: 300px;
            background-color:#dddddd;
        }
        .trhead{
            background-color:#CC9933;
        }
        tr{
            background-color:#ffffff;
        }
        .chatuser{
            color: #ffffff;
            overflow: auto;
            width: 1000px;
            height: 300px;
            background-color:#000000;
        }
        .paneltitle{
            width: 1000px;
            height: 20px;
            text-align: center;
            background-color:#aadddd;
        }
        .inputpanel{
            color: #ffffff;
            width: 1000px;
            height: 20px;
            background-color:#000000;
        }
    </style>
</head>
<body>
<div>
    <h1>聊天监控页面</h1>
</div>
<div>查询条件：时间<input onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" id="start" type="text">-<input onfocus="WdatePicker({dateFmt:'yyyy-MM-dd HH:mm:ss'})" type="text" id="end"> 发起人<input type="text" id="from"> 接收人<input type="text" id="to"> <input type="button" value="查询" onclick="search();"></div>
<div class="top">
<table id="table" width="100%"></table>
</div>
<div class="paneltitle">聊天详细内容</div>
<div class="chatuser"></div>
<div class="inputpanel" style="text-align: right">
    <input id="msg_1" type="text" style="width: 250px"/>
</div>
</body>
</html>
