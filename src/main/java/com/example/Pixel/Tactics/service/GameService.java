package com.example.Pixel.Tactics.service;

import model.Game;
import model.Gameplay;
import model.Player;
import model.User;

import java.util.ArrayList;
import java.util.UUID;

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
        game.setUser1(user1);
        GameStorage.getInstance().setGame(game);

        return game;
    }

    public Gameplay connectToGame(User user2, String gameId) throws GameIsFullException, GameNotFound, LoginIsBusy {
        Game game = GameStorage.getInstance().getGame(gameId);

        if (game.getUser2() != null) {
            throw new GameIsFullException("Game if full.");
        } else if (game.getUser1().getLogin().equals(user2.getLogin())) {
            user2.setLogin(user2.getLogin() + "(1)");
        } 

        game.setUser2(user2);
        Gameplay gameplay = new Gameplay(game);
        game.setGameplay(gameplay);
        return gameplay;
    }
}
