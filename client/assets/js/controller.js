var words = {};
var text = '';

const ERROR = "error";
const OK = "error";
const DOMEN = "localhost";
const URL = "http://" + DOMEN + ":8080/translate";

function translationController(data) {
    console.log("Error controller: " + JSON.stringify(data));
    $("#support-area").hide();
    $("#output").show();
    $("#output").val(data.text)
}

function errorController(data) {
    console.log("Error controller: " + data.responseText);
    var word = data.responseJSON.word;

    $("#support-area").show();
    $("#word").val(word);
    $("#translation").val('');
}

function send() {
    $.ajax({
        type:"POST",
        url: URL,
        contentType: "application/json",
        data: JSON.stringify({
            text: text,
            dict: words
        }),
        success: translationController,
        error:errorController
    });
}

$(document).ready(function(){
    $("#support-area").hide();
    $("#output").hide();


    $("#translate").click(function() {
        words = {};
        text = $("#input").val();
        console.log("Text: " + text);
        send()
    });

    $("#send-translation").click(function() {
        words[$("#word").val()] = $("#translation").val();
        send()
    });
});