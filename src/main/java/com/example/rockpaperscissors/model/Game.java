package com.example.rockpaperscissors.model;

import com.example.rockpaperscissors.security.Token;
import lombok.Builder;
import lombok.Value;

@Value
//@Builder(toBuilder = true)
public class Game {
    String id;
    String playerName;
    GameStatusCode gameStatusCode;
    String opponentName;
}
