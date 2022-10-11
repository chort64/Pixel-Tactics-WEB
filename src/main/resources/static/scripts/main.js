
const url = 'http://localhost:8080';

let playerLogin;
let stompClient;
let gameID;

let playerType;
let currentData;
let whoMove;

let choosenHeroForMove = [];
let choosenHeroOnField = [];
let choosenCardInHand = -1;
let choosenCellOnField = [];
var my_hand_card = []; 
let digBody = false;

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
            console.log("WHOMOVE" + data.whoMove);
            displayWhoMove(data);
            whoMove = data.whoMove;
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
                gameID = data.gameId;
                currentData = data;
                playerType = 0;
                console.log(playerType);
                playerLogin = login;
                whoMove = data.whoMove;
                connectToSocket(gameID);
                console.log(gameID);
                alert("Your created a game. Game id is: " + data.gameId);
                // waitUser(data.gameId);
            },
            error: function (error) {
                console.log(error);
            }
        })
    }
}   

// ЛЮБЛЮ ТЕБЯ, МОЙ САМЫЙ ЛУЧШИЙ И САМЫЙ ЛЮБИМЫЙ!!!!!!!

function connect_game() {
    let login = document.getElementById("login").value;
    if (login == null || login === '') {
        alert("Please enter login");
    }
    else {
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
                playerLogin = login;
                if (data.player1.login === login) {
                    playerLogin = login + "(1)";
                }
                gameID = gameId;
                console.log("CHECK AGMEID: "+ gameId);
                playerType = 1;
                whoMove = data.whoMove;
                displayWhoMove(data);
                console.log(playerType);
                displayEnemyLogin(data);
                displayMyLogin(data);
                displayMyHand(data);
                displayEnemyHand(data);
                displayCountOfEnemyDeck(data);
                displayCountOfMyDeck(data);
                my_hand_card = document.querySelectorAll(".my-hand-card"); 
                connectToSocket(gameId);
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

    if (text > 0 && playerType == whoMove) {
        //http запрос
        $.ajax({
            url: url + "/game/makeMove",
            type: 'POST',
            dataType: "JSON",
            contentType: "application/json",
            data: JSON.stringify({
                "gameId": gameID,
                "login": playerLogin,
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
    console.log(whoMove);
    if (playerType === whoMove) {
    $("#" + id).click(function () {
        let text = document.getElementById(id).innerText;
        if (text === "") {}
        else {
            choosenCardInHand = id.split("_")[1];
            displayPossibleCells(); 
        }
    });
    }
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

function reset() {
    console.log("RESET");
    for (let i = 0; i < 3; ++i) {
        for (let j = 0; j < 6; ++j) {
            let el = document.getElementById(i + "_" + j);
            console.log(el);
            el.style.backgroundColor = 'transparent';
        }
    }
    choosenHeroForMove = [];
    choosenHeroOnField = [];
    console.log("HERO ON FILED : " + choosenHeroOnField);
    choosenCardInHand = -1;
    console.log("CARD IN HAND: " + choosenCardInHand);
    digBody = false;
}

$(".my-card").click(function () {
    var id = $(this).attr("id");
    if (whoMove != playerType) {}
    else if (choosenCardInHand > 0) {
        var id = $(this).attr("id").split("_");
        putCard(choosenCardInHand - 0, id[0] - 0, id[1] - 3);
    } else if (document.getElementById(id).innerText === "DEAD" && digBody) {
        digDead(id.split("_")[0], id.split("_")[1] - 3);
        reset();
    }
    else if (choosenHeroOnField != [] && document.getElementById($(this).attr("id")).innerText == "") {
        var id = $(this).attr("id").split("_");
        displayPossibleCells();
        moveHero(choosenHeroOnField[0], choosenHeroOnField[1], id[0] - 0, id[1] - 3);
    }
    else if (document.getElementById($(this).attr("id")).innerText != "") {
        var id = $(this).attr("id");
        document.getElementById(id).style.backgroundColor = 'green';
        choosenHeroOnField = [id.split("_")[0] - 0, id.split("_")[1] - 3];
        console.log(choosenCellOnField);
    }
});


//выбор карты противника
$(".e-card").click(function () {
    var id = $(this).attr("id");
    if (choosenHeroOnField != [] && document.getElementById(id).innerText != "" && playerType == whoMove) {
        heroAttack(choosenHeroOnField[0], choosenHeroOnField[1], 2 - id.split("_")[0], 2 - id.split("_")[1]);
    }
})


function putCard(i, j, k) {
    console.log("wtf");
    console.log(i, j, k);
    $.ajax({
        url: url + "/game/makeMove",
        type: 'POST',
        dataType: "JSON",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": gameID,
            "login": playerLogin,
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
            console.log("WAAAAAAAAAAT: " + gameID);
            console.log("WAAAAAAAAAAT: " + playerLogin);
            console.log()
            console.log(error);
        }
    })
}

function heroAttack(x1, y1, x2, y2) {
    console.log(x1, y1, x2, y2);
    $.ajax({
        url: url + "/game/makeMove",
        type: 'POST',
        dataType: "JSON",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": gameID,
            "login": playerLogin,
            "typeOfMove": "ATTACK",
            "x1" : x1,
            "y1" : y1,
            "x2" : x2,
            "y2" : y2
          }),
        success: function (data) {
            document.getElementById(x1 + "_" + y1).style.backgroundColor = 'transparent';
            displayMyHand(data);
            displayMyField(data);
            displayEnemyField(data);
            choosenHeroOnField = [];
        },
        error: function (error) {
            console.log()
            console.log(error);
        }
    })
}

function moveHero(x1, y1, x2, y2) {
    console.log(x1, y1, x2, y2);
    $.ajax({
        url: url + "/game/makeMove",
        type: 'POST',
        dataType: "JSON",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": gameId,
            "login": playerLogin,
            "typeOfMove": "MOVE",
            "x1" : x1,
            "y1" : y1,
            "x2" : x2,
            "y2" : y2
          }),
        success: function (data) {
            displayMyField(data);
            displayEnemyField(data);
            choosenHeroOnField = [];
        },
        error: function (error) {
            console.log()
            console.log(error);
        }
    })
}



function dig() {
    for (let i = 0; i < 3; ++i) {
        for (let j = 3; j < 6; ++j) {
            let id = i + "_" + j;
            let card = document.getElementById(id);
            if (card.innerText == "DEAD") card.style.backgroundColor = "green"; 
        }
    }
    digBody = true;
}

function digDead(x1, y1) {
    $.ajax({
        url: url + "/game/makeMove",
        type: 'POST',
        dataType: "JSON",
        contentType: "application/json",
        data: JSON.stringify({
            "gameId": gameId,
            "typeOfMove": "DIG",
            "x1" : x1,
            "y1" : y1
          }),
        success: function (data) {
            displayMyField(data);
            displayEnemyField(data);
            digBody = false;
        },
        error: function (error) {
            console.log()
            console.log(error);
        }
    })
}