package com.example.Pixel.Tactics.service;

import com.example.Pixel.Tactics.exception.CardNotFoundException;
import com.example.Pixel.Tactics.exception.HeroIsNotDeadException;
import com.example.Pixel.Tactics.exception.NotYourMove;
import com.example.Pixel.Tactics.exception.OccupiedPlaceException;

import model.Card;
import model.Gameplay;
import model.Player;

public class ExceptionService {
    static public void canPlayerMoveInThisWave(Gameplay gameplay, String login) throws NotYourMove {
        //TODO: сделать неублюдочный вид
        if ( ( gameplay.getWhoMove() == 0 
               && !gameplay.getPlayer1().getLogin().equals(login)
             ) 
             || 
             ( gameplay.getWhoMove() == 1 
               && !gameplay.getPlayer2().getLogin().equals(login)
             )
           ) 
        {
            throw new NotYourMove("It's not your move");
        }
    }

    static public void canPlayerPutCard(Gameplay gameplay, Integer xCoord, Integer yCoord) throws CardNotFoundException {
        if (gameplay.getRound() < 0 && (xCoord != 1 || yCoord != 1)) {
            throw new CardNotFoundException("Choose leader place"); 
        } else if (yCoord != gameplay.getWave() - 1 && gameplay.getRound() >= 0) {
            throw new CardNotFoundException("You can't move on this wave"); 
        }
    }

    static public void leaderCantMoveCheck(Integer x, Integer y) throws OccupiedPlaceException {
        if (x == 1 && y == 1) {
            throw new OccupiedPlaceException("You can move Leader"); //Исправить на другую ошибку
        }
    }

    static public void isDeadHeroWithThisCoordinates(Player player, Integer x, Integer y) throws HeroIsNotDeadException, CardNotFoundException {
        Card card = player.getCardWithCoordinates(x, y);
        if (card.getAlive()) {
            throw new HeroIsNotDeadException("This Hero is not dead");
        }
    }

    static public void isReadyToMoveThisHero(Player player, Integer x, Integer y) throws OccupiedPlaceException, CardNotFoundException {
        Card card = player.getCardWithCoordinates(x, y);
        if (!card.getReadyToMove()) {
            //TODO: поменять ошибку
            throw new OccupiedPlaceException("This hero can't move more in this wave"); 
        }
    }
}
