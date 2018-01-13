var stompClient = null;

window.onload = function() {
    connect();
};

window.onclose = function(){
    disconnect();
}

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#logfile").html("");
}

function connect() {
    var socket = new SockJS('/log');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/topic/log', function (logEntry) {
            showLogEntry(logEntry.body);
        });
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    stompClient.send("/app/hello", {}, JSON.stringify({'name': $("#name").val()}));
}

function showLogEntry(message) {
    $("#logfile").append("<tr><td>" + message + "</td></tr>");
    window.scrollTo(0,document.body.scrollHeight);
}

