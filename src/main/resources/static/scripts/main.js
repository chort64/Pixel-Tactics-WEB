
const url = 'http://localhost:8080';
let stompClient;
let gameId;
let playerType;

// let enemyField = [["", "", ""], ["", "", ""], ["", "", ""]];
// let myField = [["", "", ""], ["", "", ""], ["", "", ""]];

let choosenCardInHand = -1;
let choosenCellOnField = [];
var my_hand_card = []; 

function connectToSocket(gameId) {  

    console.log("connecting to the game");
    let socket = new SockJS(url + "/gameplay"); //Нихуя не понятно, почему ссылка на /gameplay
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        console.log("connected to the frame: " + frame);
        stompClient.subscribe("/topic/game-progress/" + gameId, function (response) {
            console.log("CHECK SOCKET");
            let data = JSON.parse(response.body);
            console.log(data);
            displayEnemyLogin(data);
            displayMyLogin(data);
            displayMyHand(data);
            displayEnemyHand(data);
            displayMyField(data);
            displayEnemyField(data);
            displayCountOfMyDeck(data);
        })
    })
}   

function create_game() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Please enter login");
    } else {
        $.ajax({
            url: url + "/game/createGame",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "login": login
            }),
            success: function (data) {
                gameId = data.gameId;
                connectToSocket(gameId);
                console.log(gameId);
                alert("Your created a game. Game id is: " + data.gameId);
                // waitUser(data.gameId);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}   

// function waitUser(gameId) {
//     console.log("waiting");
//     console.log("GAMEID : " + gameId);
//     $.ajax({
//         url: url + "/game/checkGame",
//         type: 'POST',
//         dataType: "json",
//         contentType: "application/json",
//         data: JSON.stringify({
//             "gameId": gameId
//         }),
//         success: function (data) {
//             console.log(data);
//             if (data) {
//                 setTimeout(() => {
//                     console.log("Delayed for 1 second.");
//                     waitUser(gameId);
//                   }, "2000");
//             }
//             else {
//                 $.ajax({
//                     url: url + "/game/getGame",
//                     type: 'POST',
//                     dataType: "json",
//                     contentType: "application/json",
//                     data: JSON.stringify({
//                         "gameId": gameId
//                     }),
//                     success: function (data) {
//                         alert("Your opponent is " + data.currentEnemy.login);
//                         displayEnemyLogin(data);
//                         displayMyHand(data);
//                         displayEnemyHand(data);
//                         displayCountOfEnemyDeck(data);
//                         displayCountOfMyDeck(data);
//                     } 
//                 });
//             }
//         },
//     });
// }

// ЛЮБЛЮ ТЕБЯ, МОЙ САМЫЙ ЛУЧШИЙ И САМЫЙ ЛЮБИМЫЙ!!!!!!!

function connect_game() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Please enter login");
    } else {
        let gameId = document.getElementById("game_id").value;
        if (gameId == null || gameId === '') {
            alert("Please enter game id");
        }
        $.ajax({
            url: url + "/game/connectToGame",
            type: 'POST',
            dataType: "json",
            contentType: "application/json",
            data: JSON.stringify({
                "user": {
                    "login": login
                },
                "gameId": gameId
            }),
            success: function (data) {
                gameId = data.gameId;
                displayEnemyLogin(data);
                displayMyLogin(data);
                displayMyHand(data);
                displayEnemyHand(data);
                displayCountOfEnemyDeck(data);
                displayCountOfMyDeck(data);
                my_hand_card = document.querySelectorAll(".my-hand-card"); 
                // playerType = 'O';
                // reset();
                connectToSocket(gameId);
                // alert("Congrats you're playing with: " + data.user.login);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}

// function makeMove(args) {

// }

$("#m_deck").click(function (){
    var deck = document.getElementById("m_deck");
    var text = deck.innerHTML.split(":")[1];  //число оставшихся карт

    if (text > 0) {
        //http запрос
        $.ajax({
            url: url + "/game/makeMove",
            type: 'POST',
            dataType: "JSON",
            contentType: "application/json",
            data: JSON.stringify({
                "gameId": gameId,
                "typeOfMove": "TAKE_CARD"
            }),
            success: function (data) {
                // console.log(data);
                displayCountOfMyDeck(data);
                displayMyHand(data);
                displayCountOfMyDeck(data);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
    else alert("Соре, кончились карты братишка");
});
// 
for(var i = 0; i < 6; ++i) {
    let id = "m_" + i;
    $("#" + id).click(function () {
        let text = document.getElementById(id).innerText;
        if (text === "") {}
        else {
            choosenCardInHand = id.split("_")[1];
            displayPossibleCells(choosenCardInHand); 
        }
    });
}

function displayPossibleCells(i)  {
    for (var j = 0; j < 3; ++j) {
        for (var k = 3; k < 6; ++k) {
            var id = j + "_" + k;
            var element = document.getElementById(id);
            if (element.innerText === "") {
                element.style.backgroundColor = 'green';
            }
        }
    }
}

$(".my-card").click(function () {
    if (choosenCardInHand > 0) {
        var id = $(this).attr("id").split("_");
        putCard(choosenCardInHand - 0, id[0] - 0, id[1] - 3);
    }
});


// for (var j = 0; j < 3; ++j) {
//     for (var k = 3; k < 6; ++k) { 
//         var id = j + "_" + k;
//         $("#" + id).click(function() {
//             console.log("newcheck" + id);
//             if (choosenCardInHand > 0 && document.getElementById(id).innerText === "") {
//                 var coord = id.split("_");
//                 console.log("CHECK CHECK" + id);
//                 putCard(choosenCardInHand - 0, coord[0] - 0, coord[1] - 3);
//             }
//         });
//     }
// }


function putCard(i, j, k) {
    console.log("wtf");
    console.log(i, j, k);
    $.ajax({
        url: url + "/game/makeMove",
        type: 'POST',
        dataType: "JSON",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": gameId,
            "typeOfMove": "PUT_CARD",
            "x1" : i,
            "y1" : j,
            "x2" : k
          }),
        success: function (data) {
            displayMyHand(data);
            displayMyField(data);
            displayEnemyField(data);
            choosenCardInHand = -1;
        },
        error: function (error) {
            console.log()
            console.log(error);
        }
    })
}
