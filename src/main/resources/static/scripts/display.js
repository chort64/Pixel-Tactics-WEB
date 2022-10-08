function displayMyLogin(data) {
    let myLogin = data.currentPlayer.login;
    $("#" + "my-login").text("Login:" + myLogin);
}

function displayEnemyLogin(data) {
    let enemyLogin = data.currentEnemy.login;
    $("#" + "enemy-login").text("Login: " + enemyLogin);
}

function displayEnemyHand(data) {
    let hand = data.currentEnemy.hand;

    for( let i = 0; i < hand.length; ++i) {
        if (hand[i] === null) {
            hand[i] = "";
        }
        let id = "e_" + (i + 1);
        $("#" + id).text(hand[i].name + "(" + hand[i].health + ", " + hand[i].damage + ")");
    }
}

function displayMyHand(data) {
    let hand = data.currentPlayer.hand;

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
    let deck = data.currentEnemy.deck.deck;
    $('#' + "e_deck").text("COUNT:" + deck.length);
}

function displayCountOfMyDeck(data) {
    let deck = data.currentPlayer.deck.deck;
    let count = 0;
    for (let i = 0; i < deck.length; ++i) {
        if (deck[i] === null) {}
        else ++count;
    }
    document.getElementById("m_deck").innerHTML = ("COUNT: " + count);
    // console.log(document.getElementById("m_deck").innerText);
}

function displayEnemyField(data) {
    let enemyField = data.currentEnemy.field;
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
    let myField = data.currentPlayer.field;
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