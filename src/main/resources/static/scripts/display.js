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
        $("#" + id).text(hand[i]);
    }
}

function displayMyHand(data) {
    let hand = data.currentPlayer.hand;

    for( let i = 0; i < hand.length; ++i) {
        if (hand[i] === null) {
            hand[i] = "";
        }
        let id = "m_" + (i + 1);
        $("#" + id).text(hand[i]);
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
    // console.log(enemyField);
    for (let i = 0; i < enemyField.length; ++i) {
        for (let j = 0; j < enemyField[i].length; ++j) {
            if (enemyField[i][j] === null) {
                enemyField[i][j] = "";
            }
            let id = i + "_" + j;
            $("#" + id).text(enemyField[i][j]);
        }
    }
}


function displayMyField(data) {
    let myField = data.currentPlayer.field;
    for (let i = 0; i < myField.length; ++i) {
        for (let j = 0; j < myField[i].length; ++j) {
            if (myField === null) {
                myField = "";
            }
            let id = i + "_" + (j + 3);
            // console.log(id + " " + myField[i][j]);
            document.getElementById(id).style.backgroundColor = 'transparent';
            $("#" + id).text(myField[i][j]);
        }
    }
}