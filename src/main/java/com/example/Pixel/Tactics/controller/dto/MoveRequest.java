package com.example.Pixel.Tactics.controller.dto;

import com.example.Pixel.Tactics.exception.GameNotFound;
import com.example.Pixel.Tactics.service.GameService;
import com.example.Pixel.Tactics.storage.GameStorage;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import model.Game;
import model.Gameplay;
import model.TypeOfMove;

@Data
public class MoveRequest {
    private String gameId;
    private String typeOfMove;
    private Integer x1 = -1;
    private Integer y1 = -1;
    private Integer x2 = -1;
    private Integer y2 = -1;
}
