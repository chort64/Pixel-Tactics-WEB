package model;

import lombok.Data;


@Data
public class Game {
    
    private String gameId;
    private GameStatus gameStatus; 
    private User user1;
    private User user2;

    private Player player1;     //Поменять сеттер/геттер может быть
    private Player player2;
    private Player winner;
    private Integer currentWave;
    private Integer turn;
    private Integer round;
    private Integer moves;

    // В зависимости от того, чей щас ход, будем получать игрока, который ходит
    public Player getCurrentPlayer() {
        if (this.turn == 0) {
            return player1;
        }
        return player2;
    }

    public void setCurrentPlayer(Player player) {
        if (this.turn == 0) {
            this.setPlayer1(player);
        }
        this.setPlayer2(player);
    }

    public Player getCurrentEnemy() {
        if (this.turn == 0) {
            return player2;
        }
        return player1;
    }

    public void setCurrentEnemy(Player player) {
        if (this.turn == 0) {
            this.setPlayer2(player);
        }
        this.setPlayer1(player);
    }

    public Gameplay gameToGameplay() {
        return new Gameplay(this);
    }
}
