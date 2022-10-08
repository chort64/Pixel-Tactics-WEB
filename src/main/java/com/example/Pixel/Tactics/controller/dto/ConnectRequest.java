package com.example.Pixel.Tactics.controller.dto;

import lombok.Data;
import model.User;

@Data
public class ConnectRequest {
    User user;
    String gameId;
}
