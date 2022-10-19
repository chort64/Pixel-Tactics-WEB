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

public class MoveService {
    public Gameplay makeMove(String gameId, String login, String move, int... args) throws GameNotFound, MaxCardsInHandException, CardNotFoundException, OccupiedPlaceException, InvalidMove, HeroIsNotDeadException, NotYourMove {
        Game game = GameStorage.getInstance().getGame(gameId);
        Gameplay gameplay = game.getGameplay();

        ExceptionService.canPlayerMoveInThisWave(gameplay, login);

        //ToDo: 
        //  - закинуть сюда ошибку на готовность ходить героя в эту волну
        //  - Подумать, как проработать изменения состояние readyToMove
        switch (move) {
            case ("TAKE_CARD"):
                takeCard(gameId, login);
                break;
            case ("PUT_CARD"):
                ExceptionService.canPlayerPutCard(gameplay, args[1], args[2]);
                putCard(gameId, login, args[0], args[1], args[2]);
                break;
            case ("MOVE"): 
                ExceptionService.leaderCantMoveCheck(args[0], args[1]);
                ExceptionService.isReadyToMoveThisHero(gameplay.getMe(login), args[0], args[1]);
                moveCard(gameId, login, args[0], args[1], args[2], args[3]);
                break;
            case ("ATTACK"):
                ExceptionService.isReadyToMoveThisHero(gameplay.getMe(login), args[0], args[1]);
                attackHero(gameId, login, args[0], args[1], args[2], args[3]);
                break;
            case ("DIG"):
                ExceptionService.isDeadHeroWithThisCoordinates(gameplay.getMe(login), args[0], args[1]); 
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
        Card card = player.takeCardFromHand(numberOfCard - 1);

        if (xCoord == 1 && yCoord == 1) {
            //Player put leader
            player.setCardWithCoordinates(xCoord, yCoord, card.newLeader()); 
        } else {
            //Player put hero
            player.setCardWithCoordinates(xCoord, yCoord, card.newHero()); 
        }

        GameStorage.setGame(game);
        return game;
    }

    public Game moveCard(String gameId, String login, Integer xCoord1, Integer yCoord1, Integer xCoord2, Integer yCoord2) throws CardNotFoundException, OccupiedPlaceException, GameNotFound {

        Game game = GameStorage.getInstance().getGame(gameId);
        Gameplay gameplay = game.getGameplay();
        Player player = gameplay.getMe(login);

        Card card = player.getCardWithCoordinates(xCoord1, yCoord1);

        player.setCardWithCoordinates(xCoord1, yCoord1, null);
        player.setCardWithCoordinates(xCoord2, yCoord2, card);
        card.setReadyToMove(false);

        GameStorage.setGame(game);
        return game;
    }

    public Game deleteBody(String gameId, String login, Integer x, Integer y) throws HeroIsNotDeadException, GameNotFound, CardNotFoundException, OccupiedPlaceException {
        Game game = GameStorage.getInstance().getGame(gameId);
        Gameplay gameplay = game.getGameplay();
        Player player = gameplay.getMe(login);
        
        player.setCardWithCoordinates(x, y, null);

        GameStorage.setGame(game);
        return game;
    }

    public Game attackHero(String gameId, String login, Integer x1, Integer y1, Integer x2, Integer y2) throws CardNotFoundException, GameNotFound, OccupiedPlaceException {
        Game game = GameStorage.getInstance().getGame(gameId);
        Gameplay gameplay = game.getGameplay();
        Player player = gameplay.getMe(login);
        Player enemy  = gameplay.getEnemy(login);
        Card playerCard = player.getCardWithCoordinates(x1, y1);
        Card enemyCard = enemy.getCardWithCoordinates(x2, y2);

        Integer wave = gameplay.getWave();
        
        //ToDo: перекинуть в ExceptionService как отдельный чек
        if (y1 != wave - 1 && gameplay.getRound() >= 0) {
            throw new CardNotFoundException("You can't move on this wave"); //ПОменять ошибку
        }

        ExceptionService.isReadyToMoveThisHero(player, x1, y1);

        //ToDO: Добавить метод decreaseHealth
        enemyCard.setHealth(enemyCard.getHealth() - playerCard.getDamage());

        //ToDO: Добавить метод на проверку 'живности' героя
        if (enemyCard.getHealth() <= 0) enemyCard.setAlive(false);
       
        playerCard.setReadyToMove(false);

        GameStorage.setGame(game);
        return game;
    }
}
