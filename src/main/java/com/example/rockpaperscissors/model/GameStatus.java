package com.example.rockpaperscissors.model;

import com.example.rockpaperscissors.security.Token;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Value;

@Value
@Builder(toBuilder = true)
public class GameStatus {
    String id;
    String playerName;
    Move playerMove;
    GameStatusCode gameStatusCode;
    String opponentName;
    Move opponentMove;
}
