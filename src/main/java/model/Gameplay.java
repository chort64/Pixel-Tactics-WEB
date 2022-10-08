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
    private Integer turn; //кто ходит
    private Integer wave;
    private Integer round;
    private Integer moves;
    // Что лучше: передавать просто игроков или currentPlayer. currentEnemy.

    private Player currentPlayer;
    private Player currentEnemy;


    public Gameplay(Game game) {
        this.gameId = game.getGameId();
        this.turn = game.getTurn();
        this.wave = game.getCurrentWave();
        this.round = game.getRound();
        this.moves = game.getMoves();
        this.currentPlayer = game.getCurrentPlayer();
        this.currentEnemy = game.getCurrentEnemy();
    }
}

