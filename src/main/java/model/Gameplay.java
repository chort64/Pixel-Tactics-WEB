package model;

import lombok.Getter;
import lombok.Setter;

/*
 * Продумать, можно ли здесь оставить только поля (и конструктор) а всю остальную часть
 * Вынести как gameplayService
 */

@Getter
@Setter
public class Gameplay {
    
    private Integer turn;      
    private Integer whoMove;  
    private Integer wave;
    private Integer round;
    private Integer numberOfSteps;
    private Player player1;
    private Player player2;
    private String winner;

    public Gameplay(Game game) {
        turn = (int) (Math.random() * 10) % 2;
        whoMove = turn;
        wave = 1;
        round = -1;
        numberOfSteps = 1;
        player1 = new Player(game.getUser1());
        player2 = new Player(game.getUser2());
    }

    public void decreaseNumberOfStepsByOne(){
        numberOfSteps = numberOfSteps - 1;
    }

    public void updateAllGameplayValues() {
        if (round < 0) {
            chooseLeaderRound();
        }

        if (numberOfSteps == 0) {
            player1.updateCardsStatus();  // Вынести бы как нибудь
            player2.updateCardsStatus();  //
            numberOfSteps = 2;
            whoMove = (whoMove + 1) % 2;
            if (whoMove == turn) {
                wave = wave + 1;
            }
        }

        if (wave > 3) {
            wave = 1;
            turn = (turn + 1) % 2;
            whoMove = turn;
            round = round + 1;
        }
    }

    public void chooseLeaderRound() {
        whoMove = (whoMove + 1) % 2;
        if (turn == whoMove) {
            numberOfSteps = 2;
            round = 1;
        } else {
            numberOfSteps = 1;
        }
    }

    public void checkWinner() {
        if (!player1.isAliveLeader() && round > 0) {
            winner = player2.getLogin();
        }
        if (!player2.isAliveLeader() && round > 0) {
            winner = player1.getLogin();
        }
    }

    // Method for getting your player
    public Player getMe(String login) {
        if (login.equals(player1.getLogin())) return player1;
        else                                  return player2;
    }

    // Method for getting your enemy player
    public Player getEnemy(String login) {
        if (login.equals(player1.getLogin())) return player2;
        else                                  return player1;
    }

}

