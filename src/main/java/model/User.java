package model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
// @AllArgsConstructor
public class User {
   private String login; 


   //Будем получать контроль игрока засчёт соотв. логинов
   public Player getMe(Game game) {
      Player player1 = game.getPlayer1();
      Player player2 = game.getPlayer2();
      if (player1.getLogin() == this.getLogin()) return player1;
      else return player2;
  }

  public Player getEnemy(Game game) {
      Player player1 = game.getPlayer1();
      Player player2 = game.getPlayer2();
      if (player1.getLogin() == this.getLogin()) return player2;
      else return player1;
  }
}
