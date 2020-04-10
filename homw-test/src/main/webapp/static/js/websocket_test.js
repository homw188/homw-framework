var websocket;
function createSocket() {
    var ip = window.location.hostname;
    var port = window.location.port;
    var pathName = window.location.pathname;

    var projectName = pathName.substring(0, pathName.substr(1).indexOf('/') + 1);

    //同http协议使用'http://'开头一样，WebSocket协议的URL使用'ws://'开头，另外安全的WebSocket协议使用'wss://'开头
    //判断当前浏览器是否支持WebSocket
    if ('WebSocket' in window) {
        websocket = new WebSocket("ws://" + ip + ":" + port + projectName + "/ws");
    } else if ('MozWebSocket' in window) {
        websocket = new MozWebSocket("ws://" + ip + ":" + port + projectName + "/ws");
    } else {
        websocket = new SockJS("http://" + ip + ":" + port + projectName + "/sockjs");
    }
    
    var bizId = 1001;// 业务id

    //连接成功建立的回调方法
    websocket.onopen = function (evnt) {
        console.log("websocket连接成功");
        sendMessageBySocket(JSON.stringify({bizId: bizId}));

        $("#socketLayer").html("连接状态：正常");
    };

    //接收到消息的回调方法
    websocket.onmessage = function (event) {
        var data = event.data;
        console.log("收到推送消息>>>" + data);
        if (data) {
            data = JSON.parse(data);
            if (data.code != 0) {
                console.log("收到推送异常消息>>>" + data.msg);
                return;
            }

            if (data.refreshType == "stat") {
            	console.log("刷新统计");
            } else if (data.refreshType == "status") {
            	console.log("刷新状态");
            } else if (data.refreshType == "alarm") {
            	console.log("刷新告警");
            } else if (data.refreshType == "config") {
            	console.log("刷新配置");
                //window.location = "websocket_test.html?bizId=" + bizId;
            }
        }
    };

    //连接发生错误的回调方法
    websocket.onerror = function (evnt) {
        console.log("websocket连接错误");
        $("#socketLayer").html("连接状态：异常");
    };

    //连接关闭的回调方法
    websocket.onclose = function (evnt) {
        console.log("websocket连接断开");
        $("#socketLayer").html("连接状态：断开");

        //IE下会很快自动关闭连接，这种情况下就只能在关闭的时候重新去加载请求连接 (此处如果连接数大的话，重新请求会不会影响性能有待考究)
        //会去操作后台重连。
        if (websocket.readyState != websocket.OPEN) {
            console.log("websocket稍后重连...");
            setTimeout(function () {
                websocket.close();
                createSocket();
            }, 5000);
        }
    };

    //监听窗口关闭事件，当窗口关闭时，主动去关闭websocket连接，防止连接还没断开就关闭窗口，server端会抛异常。
    window.onbeforeunload = function () {
        websocket.close();
        console.log("窗口关闭");
    }
}

function sendMessageBySocket(message) {
    if (message && message != '') {
        if (websocket != null) {
            if (websocket.readyState == websocket.OPEN) {
                // 调用后台handleTextMessage方法
                websocket.send(message);
            } else {
                console.log("消息发送失败，websocket连接未打开");
                websocket.close();
                createSocket();
                setTimeout(function () {
                    sendMessageBySocket(message);
                }, 250);
            }
        } else {
            alert("消息发送失败，websocket未初始化");
        }
    } else {
        console.log("消息发送失败，内容不能为空");
    }
}

createSocket();
