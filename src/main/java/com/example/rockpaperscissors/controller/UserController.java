package com.example.rockpaperscissors.controller;

import com.example.rockpaperscissors.exceptions.GameNotFoundException;
import com.example.rockpaperscissors.exceptions.TokenNotFoundException;
import com.example.rockpaperscissors.exceptions.UserNotFoundException;
import com.example.rockpaperscissors.model.*;
import com.example.rockpaperscissors.security.Token;
import com.example.rockpaperscissors.services.GameService;
import com.example.rockpaperscissors.services.TokenService;
import com.example.rockpaperscissors.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
@RequestMapping("user")
public class UserController {

    UserService userService;
    TokenService tokenService;
    GameService gameService;

    @GetMapping("/all-users")
    public Stream<UserGame> getAllUsers() {
        return userService.getUsers().map(this::convertToUserGame);
    }

    @GetMapping("/{id}")
    public UserGame getUserById(@PathVariable String id) throws UserNotFoundException {
        return convertToUserGame(userService.getUserById(id));
    }

    @PostMapping("/create-user")
    public void createUser(
            @RequestBody String name,
            @RequestHeader(value = "token") String tokenId) throws TokenNotFoundException {
        Token tokenById = tokenService.getTokenById(tokenId);
        UserEntity user = userService.createUser(name);
        Optional.of(tokenById).ifPresent(token -> token.setUserId(user.getId()));
    }

    public UserGame convertToUserGame(UserEntity userEntity) {
        System.out.println(userEntity);
        return new UserGame(
                userEntity.getId(),
                userEntity.getName(),
                userEntity.getGameEntityList().stream()
                        .map(gameEntity -> new Game(
                                gameEntity.getId(),
                                gameEntity.getPlayerEntity().getName(),
                                gameEntity.getGameStatusCode(),
                                gameEntity.getOpponentEntity().getName()))
                        .collect(Collectors.toList()));
    }
}
