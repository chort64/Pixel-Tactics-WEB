package com.example.Pixel.Tactics.service;

import model.Game;
import model.GameStatus;
import model.Gameplay;
import model.Player;
import model.User;

import java.security.KeyStore.Entry;
import java.util.UUID;
import java.util.Vector;

import org.springframework.stereotype.Service;

import com.example.Pixel.Tactics.exception.CardNotFoundException;
import com.example.Pixel.Tactics.exception.GameIsFullException;
import com.example.Pixel.Tactics.exception.GameNotFound;
import com.example.Pixel.Tactics.exception.HeroIsNotDeadException;
import com.example.Pixel.Tactics.exception.InvalidMove;
import com.example.Pixel.Tactics.exception.MaxCardsInHandException;
import com.example.Pixel.Tactics.exception.OccupiedPlaceException;
import com.example.Pixel.Tactics.storage.GameStorage;

import model.Card;

@Service
public class GameService {

    public Game createGame(User user1) {
        Game game = new Game();
        // game.setGameId(UUID.randomUUID().toString());
        game.setGameId("1");
        game.setGameStatus(GameStatus.NEW);
        game.setUser1(user1);
        game.setTurn(0);
        // game.setPlayer1(new Player(user1));
        GameStorage.getInstance().setGame(game);

        return game;
    }


    public Game connectToGame(User user2, String gameId) throws GameIsFullException, GameNotFound {

        System.out.println("GAMEID: " + gameId);
        Game game = GameStorage.getInstance().getGame(gameId);
        if (game.getUser2() != null) {
            throw new GameIsFullException("Game if full.");
        } 
        game.setUser2(user2);
        GameStorage.getInstance().setGame(game);
        return startGame(gameId);
    }


    //ПРоработать лучше начало игры
    public Game startGame(String gameId) throws GameNotFound {
        Game game = GameStorage.getInstance().getGame(gameId);


        //Рандомно назначаем игроков
        if ((int) (Math.random() * 10) > 4) {
            game.setPlayer1(new Player(game.getUser1()));
            game.setPlayer2(new Player(game.getUser2()));
        } else {
            System.out.println("WOWOWOOW");
            game.setPlayer1(new Player(game.getUser2()));
            game.setPlayer2(new Player(game.getUser1()));
        }

        //Создание полей для каждого игрока
        game.getPlayer1().setField(new Card[3][3]); //Почему я решил сделать отдельно поле для каждого?
        game.getPlayer2().setField(new Card[3][3]);
        
        game.setCurrentWave(1);      //Текущая волна - первая
        game.setTurn(0); //Кто ходит первый
        game.setRound(0);
        game.setMoves(2);

        game.setGameStatus(GameStatus.IN_PROCESS);

        GameStorage.getInstance().setGame(game);
        return game;
    }


    //МОжет быть куча ошибок неправильного ввода. ДОБАВИТЬ ОШИБКИ!!
    public Gameplay MakeMove(String gameId, String move, int... args) throws GameNotFound, MaxCardsInHandException, CardNotFoundException, OccupiedPlaceException, InvalidMove, HeroIsNotDeadException {
        Game game = GameStorage.getInstance().getGame(gameId);
        switch (move) {
            case ("TAKE_CARD"):
                this.takeCard(gameId);
                break;
            case ("PUT_CARD"):
                System.out.println("ARGS    " + args[0] + " " + args[1] + " " + args[2]);
                this.putCard(gameId, args[0], args[1], args[2]);
                break;
            case ("MOVE"): 
                this.moveCard(gameId, args[0], args[1], args[2], args[3]);
                break;
            case ("ATTACK"):
                this.attackHero(gameId, args[0], args[1], args[2], args[3]);
                break;
            case ("DIG"):
                this.deleteBody(gameId, args[0], args[1]);
                break;
            default:
                throw new InvalidMove("Invalid move");
        }


        //Возможно добавить метод инкремента для волны,очереди хода, раунда
        game.setMoves(game.getMoves() - 1);
        if (game.getMoves() == 0) { 
            game.setTurn((game.getTurn() + 1) % 2);
            if (game.getTurn() == 0) {
                game.setCurrentWave(game.getCurrentWave() + 1);
                if (game.getCurrentWave() == 4) {
                    game.setCurrentWave(1);
                    game.setRound(game.getRound() + 1);

                    //очень плохо)
                    // Player player = game.getCurrentPlayer();
                    // game.setCurrentPlayer(game.getCurrentEnemy());
                    // game.setCurrentEnemy(player);

                }
            }
            game.setMoves(2);
        };
        
        GameStorage.getInstance().setGame(game);
        return game.gameToGameplay();
    }
    

    public Game takeCard(String gameId) throws MaxCardsInHandException, GameNotFound {
        Game game = GameStorage.getInstance().getGame(gameId);
        Player player = game.getCurrentPlayer();
        if (player.getHand().size() == 5) {
            throw new MaxCardsInHandException("You have max count of cards in hand.");
        } 
        player.takeCardFromDeck();
        // game.setCurrentPlayer(player);
        GameStorage.setGame(game);
        return game;
    }
        

    public Game putCard(String gameId, Integer numberOfCard, Integer xCoord, Integer yCoord) throws CardNotFoundException, OccupiedPlaceException, GameNotFound {
        Game game = GameStorage.getInstance().getGame(gameId);

        Player player = game.getCurrentPlayer();
        Vector<Card> hand = player.getHand();       //Поменять тип данных с вектора на список?

        if (hand.size() < numberOfCard - 1) {
            throw new CardNotFoundException("You don't have card with this number");
        }
        
        Card card = player.takeCardFromHand(numberOfCard - 1); //Нужно ли минус один? вроде да
        Card field[][] = player.getField();

        if (field[xCoord][yCoord] != null) {
            throw new OccupiedPlaceException("This place if occupied");
        }

        field[xCoord][yCoord] = card;
        player.setField(field);

        GameStorage.setGame(game);
        return game;
    }


    /*
     *  Идея - хранить всех героев в Map<Hero, HeroStatus>, где имя героя ключ, а его значение - его место в игре. 
     *  Например, если Воин лежит в руке, то map.get(Warrior) = IN_HAND
     *  Или если он стоит в клетке (1, 2), то map.get(Warrior) = ON_FIELD(1, 2)
     *  Зачем? Чтобы например при методах moveCard работать не тупо с координатами, из разряда взял из этой клетки, положил в эту,
     *  а будем именно брать персонажа и класть в какую-то клетку
     * 
     *  идея говно, ибо чтобы узнать, что клетка занята - надо будет пройтись по всей Мапе
     */

    //Возможно стоит обрабатывать, что ввелись координаты вне поля. Т.е. доп. ошибка
    public Game moveCard(String gameId, Integer xCoord1, Integer yCoord1, Integer xCoord2, Integer yCoord2) throws CardNotFoundException, OccupiedPlaceException, GameNotFound {

        Game game = GameStorage.getInstance().getGame(gameId);
        Player player = game.getCurrentPlayer();
        Card field[][] = player.getField();

        if (field[xCoord1][yCoord1] == null) {
            throw new CardNotFoundException("Card not found on field"); //Добавить доп. ошибку на то, что карты нет на поле
                                                                                //и изменить ошибку, что нет карты на руке
        } else if (field[xCoord2][yCoord2] != null) {
            throw new OccupiedPlaceException("This place is occupied");
        }

        Card card = field[xCoord1][yCoord1];
        field[xCoord1][yCoord1] = null;
        field[xCoord2][yCoord2] = card;
        player.setField(field);
        // game.setCurrentPlayer(player);

        GameStorage.setGame(game);
        return game;
    }

    //Метод для удаления тела с поля.
    public Game deleteBody(String gameId, Integer x, Integer y) throws HeroIsNotDeadException, GameNotFound {
        Game game = GameStorage.getInstance().getGame(gameId);
        Player player = game.getCurrentPlayer();
        Card field[][] = player.getField();

        //хранить в карте сразу и героя, и лидера?
        if (field[x][y].getAlive()) {
            throw new HeroIsNotDeadException("This Hero is not dead");
        }

        field[x][y] = null;
        player.setField(field);
        // game.setCurrentPlayer(player);

        GameStorage.setGame(game);
        return game;
    }

    public Game attackHero(String gameId, Integer x1, Integer y1, Integer x2, Integer y2) throws CardNotFoundException, GameNotFound {
        Game game = GameStorage.getInstance().getGame(gameId);
        Player player = game.getCurrentPlayer();
        Player enemy  = game.getCurrentEnemy();
        Card playerField[][] = player.getField();
        Card enemyField[][] = enemy.getField();
        //Весь интерфейс получения героя с поля нужно реализовать в классе Player
        if (playerField[x1][y1] == null) {
            throw new CardNotFoundException("Card not found");
        }
        if (enemyField[x2][y2] == null) {
            throw new CardNotFoundException("Card not found");
        }

        Card playerCard = playerField[x1][y1];
        Card enemyCard = enemyField[x2][y2];

        enemyCard.setHealth(enemyCard.getHealth() - playerCard.getDamage());
        if (enemyCard.getHealth() <= 0) enemyCard.setAlive(false);
        
        enemy.setField(enemyField);
        // game.setCurrentEnemy(enemy);

        GameStorage.setGame(game);
        return game;
    }
}
