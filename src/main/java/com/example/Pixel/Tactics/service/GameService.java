package com.example.Pixel.Tactics.service;

import model.Game;
import model.GameStatus;
import model.Gameplay;
import model.Player;
import model.User;

import java.util.UUID;
import java.util.Vector;

import org.springframework.stereotype.Service;

import com.example.Pixel.Tactics.exception.CardNotFoundException;
import com.example.Pixel.Tactics.exception.GameIsFullException;
import com.example.Pixel.Tactics.exception.GameNotFound;
import com.example.Pixel.Tactics.exception.HeroIsNotDeadException;
import com.example.Pixel.Tactics.exception.InvalidMove;
import com.example.Pixel.Tactics.exception.LoginIsBusy;
import com.example.Pixel.Tactics.exception.MaxCardsInHandException;
import com.example.Pixel.Tactics.exception.NotYourMove;
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
        game.setTurn((int) (Math.random() * 10) % 2);
        game.setWhoMove(game.getTurn());
        game.setPlayer1(new Player(user1));
        GameStorage.getInstance().setGame(game);

        return game;
    }


    public Game connectToGame(User user2, String gameId) throws GameIsFullException, GameNotFound, LoginIsBusy {

        System.out.println("GAMEID: " + gameId);
        Game game = GameStorage.getInstance().getGame(gameId);
        if (game.getUser2() != null) {
            throw new GameIsFullException("Game if full.");
        } else if (game.getUser1().getLogin().equals(user2.getLogin())) {
            System.out.println("changeLogin");
            user2.setLogin(user2.getLogin() + "(1)");
        } 
        game.setUser2(user2);
        GameStorage.getInstance().setGame(game);
        return startGame(gameId);
    }

    //ПРоработать лучше начало игры
    public Game startGame(String gameId) throws GameNotFound {
        Game game = GameStorage.getInstance().getGame(gameId);

        //Рандомно назначаем игроков
        // game.setPlayer1(new Player(game.getUser1()));
        game.setPlayer2(new Player(game.getUser2()));

        //Создание полей для каждого игрока
        game.getPlayer1().setField(new Card[3][3]); //Почему я решил сделать отдельно поле для каждого?
        game.getPlayer2().setField(new Card[3][3]);

        game.setCurrentWave(1);      //Текущая волна - первая
        // game.setWhoIsMove((int) (Math.random() * 10) % 2);
        game.setRound(0);
        game.setMoves(2);

        game.setGameStatus(GameStatus.IN_PROCESS);

        GameStorage.getInstance().setGame(game);
        return game;
    }


    //МОжет быть куча ошибок неправильного ввода. ДОБАВИТЬ ОШИБКИ!!
    public Gameplay MakeMove(String gameId, String login, String move, int... args) throws GameNotFound, MaxCardsInHandException, CardNotFoundException, OccupiedPlaceException, InvalidMove, HeroIsNotDeadException, NotYourMove {
        Game game = GameStorage.getInstance().getGame(gameId);
        if (game.getWhoMove() == 0 && !game.getPlayer1().getLogin().equals(login) 
        || (game.getWhoMove() == 1 && !game.getPlayer2().getLogin().equals(login))) {
            throw new NotYourMove("It's not your move");
        }

        switch (move) {
            case ("TAKE_CARD"):
                this.takeCard(gameId, login);
                break;
            case ("PUT_CARD"):
                 this.putCard(gameId, login, args[0], args[1], args[2]);
                break;
            case ("MOVE"): 
                this.moveCard(gameId, login, args[0], args[1], args[2], args[3]);
                break;
            case ("ATTACK"):
                this.attackHero(gameId, login, args[0], args[1], args[2], args[3]);
                break;
            case ("DIG"):
                this.deleteBody(gameId,login, args[0], args[1]);
                break;
            default:
                throw new InvalidMove("Invalid move");
        }

        //turn = 0; whoMove = 0;
        //whoMove = (whoMove + 1) % 2;

        //Возможно добавить метод инкремента для волны,очереди хода, раунда
        game.setMoves(game.getMoves() - 1);
        if (game.getMoves() == 0) { 
            game.setWhoMove((game.getWhoMove() + 1) % 2);
            System.out.println(game.getWhoMove());
            if (game.getWhoMove() == game.getTurn()) {
                game.setCurrentWave(game.getCurrentWave() + 1);
                if (game.getCurrentWave() == 4) {
                    game.setCurrentWave(1);
                    game.setRound(game.getRound() + 1);
                    game.setTurn((game.getTurn() + 1) % 2);
                    game.setWhoMove(game.getTurn());
                }
            }
            game.setMoves(2);
        };
        
        GameStorage.getInstance().setGame(game);
        return game.gameToGameplay();
    }
    

    public Game takeCard(String gameId, String login) throws MaxCardsInHandException, GameNotFound {
        Game game = GameStorage.getInstance().getGame(gameId);
        Player player = game.getMe(login);
        if (player.getHand().size() == 5) {
            throw new MaxCardsInHandException("You have max count of cards in hand.");
        } 
        player.takeCardFromDeck();
        GameStorage.setGame(game);
        return game;
    }
        

    public Game putCard(String gameId, String login, Integer numberOfCard, Integer xCoord, Integer yCoord) throws CardNotFoundException, OccupiedPlaceException, GameNotFound {
        Game game = GameStorage.getInstance().getGame(gameId);

        Player player = game.getMe(login);

        System.out.println("TEST !!!!!!!!!!!!!!");
        System.out.println(player);

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
    public Game moveCard(String gameId, String login, Integer xCoord1, Integer yCoord1, Integer xCoord2, Integer yCoord2) throws CardNotFoundException, OccupiedPlaceException, GameNotFound {

        Game game = GameStorage.getInstance().getGame(gameId);
        Player player = game.getMe(login);
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
    public Game deleteBody(String gameId, String login, Integer x, Integer y) throws HeroIsNotDeadException, GameNotFound {
        Game game = GameStorage.getInstance().getGame(gameId);
        Player player = game.getMe(login);
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

    public Game attackHero(String gameId, String login, Integer x1, Integer y1, Integer x2, Integer y2) throws CardNotFoundException, GameNotFound {
        System.out.println("WARNING ATTACK:" + x2 + " " + y2);
        Game game = GameStorage.getInstance().getGame(gameId);
        Player player = game.getMe(login);
        Player enemy  = game.getEnemy(login);
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
