package com.example.rockpaperscissors.model;

import com.example.rockpaperscissors.security.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@Builder(toBuilder = true)
public class GameEntity {
    private final String id;
    private UserEntity playerEntity;
    private GameStatusCode gameStatusCode;
    private UserEntity opponentEntity;

}
