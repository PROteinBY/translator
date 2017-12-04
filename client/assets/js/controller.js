var words = {};
var text = '';

const ERROR = "error";
const OK = "error";
const DOMEN = "localhost";
const URL = "http://" + DOMEN + ":8080/translate";

function translationController(data) {
    if (data.result === ERROR) {
        $("#support-area").show();
        $("#word").val(data.word);
        $("#message").val(data.reason);
    }
    else if (data.result === OK) {
        $("#output").val(data.text)
    }
    else {
        console.log(data)
    }
}

function send() {
    $.ajax({
        type:"POST",
        url: URL,
        contentType: "application/json",
        dataType: "json",
        data: {
            text: text,
            dict: words
        },
        success: translationController
    });
}

$(document).ready(function(){
    $("#translate").click(function() {
        words = {};
        text = $("#input").val();
        send()
    });

    $("send-translation").click(function() {
        words[$("#word").val()] = $("#translation").val();
        send()
    });
});



/*
CLIENT ->
{
    "text": "привет боб это петух",
    "dict": {

    }
}

SERVER ->

{
    "result": "error",
    "reason": "нет перевода слова боб",
    "word": "боб"
}

CLIENT ->

{
    "text": "привет боб это петух галимый",
    "dict": {
        "боб": "bob"
    }
}

SERVER ->

{
    "result": "error",
    "reason": "нет перевода слова петух",
    "word": "петух"
}

CLIENT ->

{
    "text": "привет боб это петух",
    "dict": {
        "боб": "bob",
        "петух": "cock"
    }
}

SERVER ->

{
    "result": "ok",
    "text": "hi bob it's cock"
}
 */