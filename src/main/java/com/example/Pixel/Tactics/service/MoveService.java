package com.example.Pixel.Tactics.service;

import com.example.Pixel.Tactics.exception.CardNotFoundException;
import com.example.Pixel.Tactics.exception.GameNotFound;
import com.example.Pixel.Tactics.exception.HeroIsNotDeadException;
import com.example.Pixel.Tactics.exception.InvalidMove;
import com.example.Pixel.Tactics.exception.MaxCardsInHandException;
import com.example.Pixel.Tactics.exception.NotYourMove;
import com.example.Pixel.Tactics.exception.OccupiedPlaceException;
import com.example.Pixel.Tactics.storage.GameStorage;

import model.Card;
import model.Game;
import model.Gameplay;
import model.Player;

import java.util.ArrayList;

public class MoveService {
    //МОжет быть куча ошибок неправильного ввода. ДОБАВИТЬ ОШИБКИ!!
    public Gameplay makeMove(String gameId, String login, String move, int... args) throws GameNotFound, MaxCardsInHandException, CardNotFoundException, OccupiedPlaceException, InvalidMove, HeroIsNotDeadException, NotYourMove {
        Game game = GameStorage.getInstance().getGame(gameId);
        Gameplay gameplay = game.getGameplay();

        //TODO: закинуть в отдельную функцию
        if ((gameplay.getWhoMove() == 0 && !gameplay.getPlayer1().getLogin().equals(login)) 
        || (gameplay.getWhoMove() == 1 && !gameplay.getPlayer2().getLogin().equals(login))) {
            throw new NotYourMove("It's not your move");
        }

        switch (move) {
            case ("TAKE_CARD"):
                takeCard(gameId, login);
                break;
            case ("PUT_CARD"):
                 putCard(gameId, login, args[0], args[1], args[2]);
                break;
            case ("MOVE"): 
                moveCard(gameId, login, args[0], args[1], args[2], args[3]);
                break;
            case ("ATTACK"):
                attackHero(gameId, login, args[0], args[1], args[2], args[3]);
                break;
            case ("DIG"):
                deleteBody(gameId,login, args[0], args[1]);
                break;
            default:
                throw new InvalidMove("Invalid move");
        }
        
        gameplay.decreaseNumberOfStepsByOne();
        gameplay.updateAllGameplayValues();
        gameplay.checkWinner();

        GameStorage.getInstance().setGame(game);
        return gameplay;
    }

    public Game takeCard(String gameId, String login) throws MaxCardsInHandException, GameNotFound {
        Game game = GameStorage.getInstance().getGame(gameId);
        Gameplay gameplay = game.getGameplay();
        Player player = gameplay.getMe(login);
        player.takeCardFromDeck();
        GameStorage.setGame(game);
        return game;
    }
        

    public Game putCard(String gameId, String login, Integer numberOfCard, int xCoord, int yCoord) throws CardNotFoundException, OccupiedPlaceException, GameNotFound, MaxCardsInHandException {
        Game game = GameStorage.getInstance().getGame(gameId);
        Gameplay gameplay = game.getGameplay();
        Player player = gameplay.getMe(login);

        ArrayList<Card> hand = player.getHand();       //Поменять тип данных с вектора на список?
        Integer wave = gameplay.getWave();

        if (hand.size() < numberOfCard - 1) {
            throw new CardNotFoundException("You don't have card with this number");
        } else if (gameplay.getRound() < 0 && (xCoord != 1 || yCoord != 1)) {
            throw new CardNotFoundException("Choose leader place"); //Поменять ошибку
        } else if (yCoord != wave - 1 && gameplay.getRound() >= 0) {
            throw new CardNotFoundException("You can't move on this wave"); //ПОменять ошибку
        }
        
        Card card = player.takeCardFromHand(numberOfCard - 1); //Нужно ли минус один? вроде да
        Card field[][] = player.getField();

        if (field[xCoord][yCoord] != null) {
            throw new OccupiedPlaceException("This place if occupied");
        }

        if (xCoord == 1 && yCoord == 1) {
            card.newLeader(); 
        } else {
            card.newHero();
        }

        card.setReadyToMove(false);
        field[xCoord][yCoord] = card;
        player.setField(field);

        GameStorage.setGame(game);
        return game;
    }

    //Возможно стоит обрабатывать, что ввелись координаты вне поля. Т.е. доп. ошибка
    public Game moveCard(String gameId, String login, Integer xCoord1, Integer yCoord1, Integer xCoord2, Integer yCoord2) throws CardNotFoundException, OccupiedPlaceException, GameNotFound {

        Game game = GameStorage.getInstance().getGame(gameId);
        Gameplay gameplay = game.getGameplay();
        Player player = gameplay.getMe(login);
        Card field[][] = player.getField();

        if (field[xCoord1][yCoord1] == null) {
            throw new CardNotFoundException("Card not found on field"); //Добавить доп. ошибку на то, что карты нет на поле
                                                                                //и изменить ошибку, что нет карты на руке
        } else if (field[xCoord2][yCoord2] != null) {
            throw new OccupiedPlaceException("This place is occupied");
        } else if (xCoord1 == 1 && yCoord1 == 1) {
            throw new OccupiedPlaceException("You can move Leader"); //Исправить на другую ошибку
        }

        Card card = field[xCoord1][yCoord1];

        if (!card.getReadyToMove()) {
            throw new OccupiedPlaceException("This hero can't move more in this wave"); //Поменять ошибку
        }

        field[xCoord1][yCoord1] = null;
        field[xCoord2][yCoord2] = card;
        card.setReadyToMove(false);
        player.setField(field);
        // game.setCurrentPlayer(player);

        GameStorage.setGame(game);
        return game;
    }

    //Метод для удаления тела с поля.
    public Game deleteBody(String gameId, String login, Integer x, Integer y) throws HeroIsNotDeadException, GameNotFound {
        Game game = GameStorage.getInstance().getGame(gameId);
        Gameplay gameplay = game.getGameplay();
        Player player = gameplay.getMe(login);
        Card field[][] = player.getField();

        //хранить в карте сразу и героя, и лидера?
        if (field[x][y].getAlive()) {
            throw new HeroIsNotDeadException("This Hero is not dead");
        }

        field[x][y] = null;
        player.setField(field);

        GameStorage.setGame(game);
        return game;
    }

    public Game attackHero(String gameId, String login, Integer x1, Integer y1, Integer x2, Integer y2) throws CardNotFoundException, GameNotFound {
        Game game = GameStorage.getInstance().getGame(gameId);
        Gameplay gameplay = game.getGameplay();
        Player player = gameplay.getMe(login);
        Player enemy  = gameplay.getEnemy(login);
        Card playerField[][] = player.getField();
        Card enemyField[][] = enemy.getField();
        //Весь интерфейс получения героя с поля нужно реализовать в классе Player
        if (playerField[x1][y1] == null) {
            throw new CardNotFoundException("Card not found");
        }
        if (enemyField[x2][y2] == null) {
            throw new CardNotFoundException("Card not found");
        }

        Integer wave = gameplay.getWave();
        if (y1 != wave - 1 && gameplay.getRound() >= 0) {
            throw new CardNotFoundException("You can't move on this wave"); //ПОменять ошибку
        }

        Card playerCard = playerField[x1][y1];

        if (!playerCard.getReadyToMove()) {
            throw new CardNotFoundException("You cant't attack with this hero this wave");
        }

        Card enemyCard = enemyField[x2][y2];


        enemyCard.setHealth(enemyCard.getHealth() - playerCard.getDamage());
        if (enemyCard.getHealth() <= 0) enemyCard.setAlive(false);
       
        playerCard.setReadyToMove(false);
        enemy.setField(enemyField);
        // game.setCurrentEnemy(enemy);

        GameStorage.setGame(game);
        return game;
    }
 
}
