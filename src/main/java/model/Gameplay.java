package model;

import lombok.Getter;
import lombok.Setter;

/*
    класс для получения данных при запросах
*/

@Getter
@Setter
public class Gameplay {
    
    private String gameId;

    /* 
     * Как я представляю это:
     *  -turn - показывает кто ходит первым в раунде, т.е. например если turn = 1, то ходит Player1 первым в раунде
     *  -whoMove - показывает кто ходит в данный ход
     * 
     * turn не меняется по истечению трех раундов, лишь когда закончится третий раунд - тогда turn = (turn + 1) % 2;
     * 
     * Раунд начинается, смотрим значение turn, если единица, то whoMove = player1, иначе player2;
     * У игрока, который ходил, заканчиваются ходы, соотв. whoMove = player2. 
     *  
    */
    private Integer turn;      
    private Integer whoMove;  

    private Integer wave;
    private Integer round;
    private Integer moves;
    // Что лучше: передавать просто игроков или currentPlayer. currentEnemy.

    private Player player1;
    private Player player2;
    private String winner;

    // private Player currentPlayer;
    // private Player currentEnemy;


    public Gameplay(Game game) {
        this.gameId = game.getGameId();
        this.turn = game.getTurn();
        this.whoMove = game.getWhoMove();
        this.wave = game.getCurrentWave();
        this.round = game.getRound();
        this.moves = game.getMoves();
        this.player1 = game.getPlayer1();
        this.player2 = game.getPlayer2();
        this.winner = game.getWinner();
        // this.currentPlayer = game.getCurrentPlayer();
        // this.currentEnemy = game.getCurrentEnemy();
    }
}

