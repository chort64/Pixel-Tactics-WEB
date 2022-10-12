# Pixel-Tactics-WEB
Transferring the "Pixel tactics" board game to the browser

# DOTO List.

## Make a prototype:
  - [x] Make core game (like put cards, take cards from deck and etc)
  - [x] Write main HTTP methods with Spring MVC
  - [x] Make a 'socket' connection with STOMP
  - [x] Make front with Bootstrap and jQuery
    
 ## Make next updates:
  - [x] Make normal user view for every player (now it's looks like you play versus yourself)
  - [x] Rewrite Card class (he have to contain Hero object and Leader object)
  - [x] Add "Leader choose" round
  - [x] Add winner check  
  - [x] Add walking on the current wave
  - [x] Rewrite player's moves changes (one hero can't move twice for wave)
  - [ ] Add all main heroes from original game
  - [ ] Add spells for every hero
  - [ ] Add spells for every leader
  - [ ] Update visual
    - [ ] Make good background
    - [ ] Add cards images
    - [ ] Make some animation (possibly)
    
## Refactoring:
- [ ] Rewrite Player Class

## Big changes for V2.0 :
  - [ ] Rewrite all Backend part (with patterns and new logic of code)
  - [ ] Rewrite all Frontend pard (control the status of game through updated data from socket (not it's work with text in div's kek))    
    - [ ] MayBe use js framework like react, vue and etc.
  - [ ] Add portal page
  - [ ] Add DataBases for gameStorage, userStorage
  - [ ] Add Spring Security
  - [ ] Maybe change STOMP on WebSocket
  - [ ] Add chat to game page
