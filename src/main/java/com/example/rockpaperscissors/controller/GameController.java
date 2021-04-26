package com.example.rockpaperscissors.controller;

import com.example.rockpaperscissors.exceptions.GameNotAllowedException;
import com.example.rockpaperscissors.exceptions.GameNotFoundException;
import com.example.rockpaperscissors.exceptions.TokenNotFoundException;
import com.example.rockpaperscissors.exceptions.UserNotFoundException;
import com.example.rockpaperscissors.model.*;
import com.example.rockpaperscissors.security.Token;
import com.example.rockpaperscissors.services.GameService;
import com.example.rockpaperscissors.services.TokenService;
import com.example.rockpaperscissors.services.UserService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RestController
@AllArgsConstructor
@RequestMapping("/games")
public class GameController {
    TokenService tokenService;
    GameService gameService;
    UserService userService;

    @GetMapping("/start")
    public GameStatus startGame(@RequestHeader(value = "token") String tokenId)
            throws UserNotFoundException, TokenNotFoundException {
        Token tokenById = tokenService.getTokenById(tokenId);
        GameEntity gameEntity = gameService.startGame(tokenById);
        Optional.of(tokenById).ifPresent(token -> token.setGameId(gameEntity.getId()));
        return convertToGameStatus(gameEntity);
    }

    @GetMapping("/join/{gameId}")
    public GameStatus joinGame(
            @RequestHeader(value = "token") String tokenId,
            @PathVariable String gameId)
            throws TokenNotFoundException, GameNotFoundException, UserNotFoundException, GameNotAllowedException {
        Token tokenById = tokenService.getTokenById(tokenId);
        UserEntity opponentUserEntity = userService.getUsersRepository().getEntityById(tokenById.getUserId())
                .orElseThrow(UserNotFoundException::new);
        GameEntity gameEntity = gameService.joinGame(opponentUserEntity, gameId);
        tokenById.setGameId(gameEntity.getId());

        return convertToGameStatus(gameEntity);
    }

    @GetMapping("/status")
    public GameStatus gameStatus(@RequestHeader(value = "token") String tokenId) throws GameNotFoundException, TokenNotFoundException {
        Token tokenById = tokenService.getTokenById(tokenId);
        GameEntity gameById = gameService.getGameById(tokenById.getGameId());
        return convertToGameStatus(gameById);
    }

    @GetMapping
    public List<GameStatus> gameList(@RequestHeader(value = "token") String tokenId) throws TokenNotFoundException {
        tokenService.getTokenById(tokenId);
        return gameService.getAllAvailableGames()
                .map(this::convertToGameStatus)
                .collect(Collectors.toList());
    }

    @GetMapping("{id}")
    public User gameInfo(
            @RequestHeader(value = "token") String tokenId,
            @PathVariable String id) throws GameNotFoundException, UserNotFoundException, TokenNotFoundException {
            tokenService.getTokenById(tokenId);
        UserEntity userEntity = userService.getUsersRepository().getEntityById(id)
                .orElseThrow(UserNotFoundException::new);
        return gameService.getGameInfo(userEntity);
    }

    @GetMapping("/move/{sign}")
    public GameStatus makeMove(
            @RequestHeader(value = "token") String tokenId,
            @PathVariable Move sign) throws TokenNotFoundException, UserNotFoundException, GameNotFoundException {
        Token tokenById = tokenService.getTokenById(tokenId);
        UserEntity userById = userService.getUserById(tokenById.getUserId());
        GameEntity gameEntity = gameService.makeMove(sign, userById, tokenById);
        userService.addGameToUserList(tokenById.getUserId(), gameEntity);
        return convertToGameStatus(gameEntity);
    }

    @GetMapping("/all-games")
    public Stream<GameStatus> getAllGames() {
        return gameService.getAllGames().map(this::convertToGameStatus);
    }

    private GameStatus convertToGameStatus(GameEntity gameEntity) {
        return GameStatus.builder()
                .id(gameEntity.getId())
                .playerName(gameEntity.getPlayerEntity() != null ?
                        gameEntity.getPlayerEntity().getName() : "Player-Unknown")
                .playerMove(gameEntity.getPlayerEntity().getMove())
                .gameStatusCode(gameEntity.getGameStatusCode())
                .opponentName(gameEntity.getOpponentEntity() != null ?
                        gameEntity.getOpponentEntity().getName() : "Opponent-Unknown")
                .opponentMove(gameEntity.getOpponentEntity() != null ?
                        gameEntity.getOpponentEntity().getMove() : Move.NONE)
                .build();
    }
}




