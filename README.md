# Pixel-Tactics-WEB
Transferring the "Pixel tactics" board game to the browser

TODO List:

  - [x] Make a prototype
    - [x] Make core game (like put cards, take cards from deck and etc)
    - [x] Write main HTTP methods with Spring MVC
    - [x] Make a 'socket' connection with STOMP
    - [x] Make front with Bootstrap and jQuery
    
  - [ ] Make next updates
    - [ ] Make normal user view for every player (now it's looks like you play versus yourself)
    - [ ] Rewrite Card class (he have to contain Hero object and Leader object)
    - [ ] Add "Leader choose" round
    - [ ] Add walking on the current wave
    - [ ] Rewrite player's moves changes
    - [ ] Add all main heroes from original game
    - [ ] Add spells for every hero
    - [ ] Add spells for every leader
    - [ ] Update visual
      - [ ] Make good background
      - [ ] Add cards images
      - [ ] Make some animation (possibly)
      
  # Hard changes for V2.0 :
  - [ ] Rewrite all Backend part (with patterns and new logic of code)
  - [ ] Rewrite all Frontend pard (control the status of game through updated data from socket (not it's work with text in div's kek))    
 