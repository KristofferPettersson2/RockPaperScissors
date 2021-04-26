package com.example.rockpaperscissors.services;

import com.example.rockpaperscissors.exceptions.UserNotFoundException;
import com.example.rockpaperscissors.model.GameEntity;
import com.example.rockpaperscissors.model.Move;
import com.example.rockpaperscissors.model.UserEntity;
import com.example.rockpaperscissors.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.UUID;
import java.util.stream.Stream;

@Service
public class UserService {

    @Autowired
    UsersRepository usersRepository;

    public Stream<UserEntity> getUsers() {
        return usersRepository.all();
    }

    public UserEntity createUser(String name) {
        UserEntity user = new UserEntity(UUID.randomUUID().toString(), name, Move.NONE, new ArrayList<>());
        usersRepository.save(user.getId(), user);
        return user;
    }

    public UsersRepository getUsersRepository() {
        return usersRepository;
    }

    public UserEntity getUserById(String id) throws UserNotFoundException {
        return usersRepository.getEntityById(id).orElseThrow(UserNotFoundException::new);
    }

    public boolean addGameToUserList(String id, GameEntity gameEntity) throws UserNotFoundException {
        switch (gameEntity.getGameStatusCode()) {
            case NONE, ACTIVE, OPEN -> { return false; }
            case WIN, LOSE, DRAW -> {
                UserEntity userById = getUserById(id);
                userById.addGameEntity(gameEntity);
                //usersRepository.save(userById.getId(), userById);
                return true;
            }
        }
        return false;
    }
}
