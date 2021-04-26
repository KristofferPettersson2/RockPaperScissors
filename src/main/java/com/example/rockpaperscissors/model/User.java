package com.example.rockpaperscissors.model;

import com.example.rockpaperscissors.security.Token;
import lombok.Value;

import java.util.UUID;

@Value
public class User {
    String id;
    String name;
    Move move;
}
