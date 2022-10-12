package com.example.Pixel.Tactics.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.Pixel.Tactics.controller.dto.ConnectRequest;
import com.example.Pixel.Tactics.controller.dto.MoveRequest;
import com.example.Pixel.Tactics.exception.CardNotFoundException;
import com.example.Pixel.Tactics.exception.GameIsFullException;
import com.example.Pixel.Tactics.exception.GameNotFound;
import com.example.Pixel.Tactics.exception.HeroIsNotDeadException;
import com.example.Pixel.Tactics.exception.InvalidMove;
import com.example.Pixel.Tactics.exception.LoginIsBusy;
import com.example.Pixel.Tactics.exception.MaxCardsInHandException;
import com.example.Pixel.Tactics.exception.NotYourMove;
import com.example.Pixel.Tactics.exception.OccupiedPlaceException;
import com.example.Pixel.Tactics.service.GameService;
import com.example.Pixel.Tactics.service.MoveService;

import lombok.AllArgsConstructor;
import model.Game;
import model.Gameplay;
import model.User;

@RestController
@AllArgsConstructor
@RequestMapping("/game")
public class controller {
    
    private final GameService gameService = new GameService();
    private final MoveService moveService = new MoveService();
    private SimpMessagingTemplate simpMessagingTemplate; //WTF?

    @PostMapping("/createGame")
    public ResponseEntity<Game> createNewGame(@RequestBody User user1) {
        Game game = gameService.createGame(user1);
        return ResponseEntity.ok(game); 
    }

    @PostMapping("/connectToGame")
    public ResponseEntity<Gameplay> ConnectToGame(@RequestBody ConnectRequest request) throws GameIsFullException, GameNotFound, LoginIsBusy {
        Gameplay gameplay = gameService.connectToGame(request.getUser(), request.getGameId());
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(), gameplay); //что
        return ResponseEntity.ok(gameplay);
    }

    @PostMapping("/makeMove")
    public ResponseEntity<Gameplay> makeMove(@RequestBody MoveRequest request) throws GameNotFound, MaxCardsInHandException, CardNotFoundException, OccupiedPlaceException, InvalidMove, HeroIsNotDeadException, NotYourMove {
        Gameplay gameplay = moveService.makeMove( request.getGameId(), request.getLogin()
                                                , request.getTypeOfMove()
                                                , request.getX1(), request.getY1()
                                                , request.getX2(), request.getY2() 
                                                );
                                                
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(), gameplay); //что
        return ResponseEntity.ok(gameplay);
    }
}
