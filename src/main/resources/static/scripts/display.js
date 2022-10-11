function getMe(data, login) {
    if (data.player1.login === login) return data.player1;
    else return data.player2;
}

function getEnemy(data, login) {
    if (data.player1.login === login) return data.player2;
    else return data.player1;
}

function displayWhoMove (data) {
    if (data.whoMove == 0) {
        $("#whoMove").text("Move: " + data.player1.login);
    } else {
        $("#whoMove").text("Move: " + data.player2.login);
    }
}

function displayMyLogin(data) {
    let myLogin = getMe(data, playerLogin).login;
    $("#" + "my-login").text("Login:" + myLogin);
}

function displayEnemyLogin(data) {
    let enemyLogin = getEnemy(data, playerLogin).login;
    $("#" + "enemy-login").text("Login: " + enemyLogin);
}

function displayEnemyHand(data) {
    
    let hand = getEnemy(data, playerLogin).hand; // data.currentEnemy.hand;

    for( let i = 0; i < hand.length; ++i) {
        if (hand[i] === null) {
            hand[i] = "";
        }
        let id = "e_" + (i + 1);
        $("#" + id).text(hand[i].name + "(" + hand[i].health + ", " + hand[i].damage + ")");
    }
}

function displayMyHand(data) {

    let hand = getMe(data, playerLogin).hand;
    // let hand = data.currentPlayer.hand;

    for( let i = 0; i < hand.length; ++i) {
        if (hand[i] === null) {
            hand[i] = "";
        }
        let id = "m_" + (i + 1);
        $("#" + id).text(hand[i].name + "(" + hand[i].health + ", " + hand[i].damage + ")");
    }

    for (let i = hand.length; i < 5; ++i) {
        let id = "m_" + (i + 1);
        $("#" + id).text("");
    }
}

function displayCountOfEnemyDeck(data) {
    // let deck = data.currentEnemy.deck.deck;
    let deck = getEnemy(data, playerLogin).deck.deck;

    $('#' + "e_deck").text("COUNT:" + deck.length);
}

function displayCountOfMyDeck(data) {
    // let deck = data.currentPlayer.deck.deck;
    let deck = getMe(data, playerLogin).deck.deck;

    let count = 0;
    for (let i = 0; i < deck.length; ++i) {
        if (deck[i] === null) {}
        else ++count;
    }
    document.getElementById("m_deck").innerHTML = ("COUNT: " + count);
    // console.log(document.getElementById("m_deck").innerText);
}

function displayEnemyField(data) {
    // let enemyField = data.currentEnemy.field;
    let enemyField = getEnemy(data, playerLogin).field;

    for (let i = 0; i < enemyField.length; ++i) {
        for (let j = 0; j < enemyField[i].length; ++j) {
            let id = (2 - i) + "_" + (2 - j);
            if (enemyField[i][j] === null) {
                $("#" + (2 - i) + "_" + (2 - j)).text("");
            } else if (enemyField[i][j].alive === false) {
                $("#" + id).text("DEAD");
            } 
            else {
                $("#" + id).text(enemyField[i][j].name + "( " + enemyField[i][j].health + ", " + enemyField[i][j].damage + ")");
            }
        }
    }
}


function displayMyField(data) {
    // let myField = data.currentPlayer.field;
    let myField = getMe(data, playerLogin).field;

    console.log(myField);
    for (let i = 0; i < myField.length; ++i) {
        for (let j = 0; j < myField[i].length; ++j) {
            let id = i + "_" + (j + 3);
            if (myField[i][j] === null) {
                $("#" + id).text("");
            } else if (myField[i][j].alive === false) {
                $("#" + id).text("DEAD");
            } else {
                $("#" + id).text(myField[i][j].name + "( " + myField[i][j].health + ", " + myField[i][j].damage + ")");
            }
            document.getElementById(id).style.backgroundColor = 'transparent';

        }
    }
}