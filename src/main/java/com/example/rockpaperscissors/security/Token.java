package com.example.rockpaperscissors.security;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Value;

import java.util.UUID;

@Data
@AllArgsConstructor
public class Token {
    String id;
    String userId;
    String gameId;
}
