package com.example.Pixel.Tactics.controller.dto;

import lombok.Data;

@Data
public class MoveRequest {
    private String gameId;
    private String login;
    private String typeOfMove;
    private Integer x1 = -1;
    private Integer y1 = -1;
    private Integer x2 = -1;
    private Integer y2 = -1;
}
