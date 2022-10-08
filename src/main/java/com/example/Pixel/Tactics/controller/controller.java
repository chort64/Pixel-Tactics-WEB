package com.example.Pixel.Tactics.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.Pixel.Tactics.controller.dto.Check;
import com.example.Pixel.Tactics.controller.dto.ConnectRequest;
import com.example.Pixel.Tactics.controller.dto.MoveRequest;
import com.example.Pixel.Tactics.exception.CardNotFoundException;
import com.example.Pixel.Tactics.exception.GameIsFullException;
import com.example.Pixel.Tactics.exception.GameNotFound;
import com.example.Pixel.Tactics.exception.HeroIsNotDeadException;
import com.example.Pixel.Tactics.exception.InvalidMove;
import com.example.Pixel.Tactics.exception.MaxCardsInHandException;
import com.example.Pixel.Tactics.exception.OccupiedPlaceException;
import com.example.Pixel.Tactics.service.GameService;
import com.example.Pixel.Tactics.storage.GameStorage;

import lombok.AllArgsConstructor;
import model.Game;
import model.Gameplay;
import model.Player;
import model.User;

@RestController
@AllArgsConstructor
@RequestMapping("/game")
public class controller {
    
    private final GameService gameService = new GameService();
    private SimpMessagingTemplate simpMessagingTemplate; //WTF?

    @PostMapping("/createGame")
    public ResponseEntity<Gameplay> createNewGame(@RequestBody User user1) {
        Game game = gameService.createGame(user1);
        return ResponseEntity.ok(game.gameToGameplay()); 
    }

    @PostMapping("/checkGame")
    public ResponseEntity<Boolean> check(@RequestBody Check check) throws GameNotFound {
        return ResponseEntity.ok(GameStorage.getInstance().getGame(check.getGameId()).getUser2() == null);
    }

    @PostMapping("/getGame")
    public ResponseEntity<Gameplay> get(@RequestBody Check check) throws GameNotFound {
        return ResponseEntity.ok(GameStorage.getInstance().getGame(check.getGameId()).gameToGameplay());
    }

    @PostMapping("/connectToGame")
    public ResponseEntity<Gameplay> ConnectToGame(@RequestBody ConnectRequest request) throws GameIsFullException, GameNotFound {
        Gameplay gameplay = gameService.connectToGame(request.getUser(), request.getGameId()).gameToGameplay();
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(), gameplay); //что
        return ResponseEntity.ok(gameplay);
    }

    //TESTING
    // @PostMapping("/startGame")
    // public ResponseEntity<Gameplay> startGame() throws GameNotFound, GameIsFullException {
    //     User user = new User("chort");
    //     User user2 = new User("CHORT");
    //     Game game = gameService.createGame(user);
    //     gameService.connectToGame(user2, "1");
    //     return ResponseEntity.ok( gameService.startGame("1")
    //                               .gameToGameplay()
    //                              );
    // }

    // Лишнее
    // @PostMapping("/startGame")
    // public ResponseEntity<Gameplay> startGame(@RequestBody String gameId) throws GameNotFound {
    //      return ResponseEntity.ok( gameService.startGame(gameId)
    //                               .gameToGameplay()
    //                              );
    // }

    // @PostMapping("/ChooseLeader")

    @PostMapping("/makeMove")
    public ResponseEntity<Gameplay> makeMove(@RequestBody MoveRequest request) throws GameNotFound, MaxCardsInHandException, CardNotFoundException, OccupiedPlaceException, InvalidMove, HeroIsNotDeadException {
        Gameplay gameplay = gameService.MakeMove( request.getGameId(), request.getTypeOfMove()
                                                , request.getX1(), request.getY1()
                                                , request.getX2(), request.getY2() 
                                                );
                                                
        simpMessagingTemplate.convertAndSend("/topic/game-progress/" + request.getGameId(), gameplay); //что
        return ResponseEntity.ok(gameplay);
    }
}
