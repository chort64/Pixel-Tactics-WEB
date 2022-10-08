package com.example.Pixel.Tactics.storage;

import java.util.HashMap;
import java.util.Map;

import com.example.Pixel.Tactics.exception.GameNotFound;

import model.Game;

public class GameStorage {
    static private Map<String, Game> gameStorage;
    static private GameStorage instance = null;


    //Для тестов
    public Map<String, Game> getGameStorage() {
        return gameStorage;
    }

    public GameStorage() {
        gameStorage = new HashMap<>();
    }

    static public synchronized GameStorage getInstance() {
        if (instance == null) {
            instance = new GameStorage();
        }
        return instance;
    }

    public Game getGame(String login) throws GameNotFound {
        if (!gameStorage.containsKey(login)) {
            throw new GameNotFound("Game not found.");
        }
        return gameStorage.get(login);
    }

    static public void setGame(Game game) {
        gameStorage.put(game.getGameId(), game);
    }
}
