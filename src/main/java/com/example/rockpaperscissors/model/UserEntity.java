package com.example.rockpaperscissors.model;

import com.example.rockpaperscissors.repository.Repository;
import com.example.rockpaperscissors.security.Token;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class UserEntity {
    private final String id;
    private String name;
    private Move move;

    private List<GameEntity> gameEntityList;

    public void addGameEntity(GameEntity gameEntity) {
        gameEntityList.add(gameEntity);
    }
}
