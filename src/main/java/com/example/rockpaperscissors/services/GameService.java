package com.example.rockpaperscissors.services;

import com.example.rockpaperscissors.exceptions.GameNotAllowedException;
import com.example.rockpaperscissors.exceptions.GameNotFoundException;
import com.example.rockpaperscissors.exceptions.UserNotFoundException;
import com.example.rockpaperscissors.model.*;
import com.example.rockpaperscissors.repository.GamesRepository;
import com.example.rockpaperscissors.repository.UsersRepository;
import com.example.rockpaperscissors.security.Token;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.stream.Stream;

@Service
public class GameService {

    @Autowired
    GamesRepository gameRepository;

    @Autowired
    UsersRepository usersRepository;

    public GameEntity startGame(Token token) throws UserNotFoundException {
        UserEntity user = usersRepository.getEntityById(token.getUserId())
                .orElseThrow(UserNotFoundException::new);
        GameEntity newGame = createNewGame(user);
        gameRepository.save(newGame.getId(), newGame);
        return newGame;
    }

    private GameEntity createNewGame(UserEntity user) {
        return GameEntity.builder()
                .id(UUID.randomUUID().toString())
                .playerEntity(user)
                .gameStatusCode(GameStatusCode.OPEN)
                .opponentEntity(null)
                .build();
    }

    public GameEntity joinGame(UserEntity userEntity, String gameId) throws GameNotFoundException, GameNotAllowedException {
        GameEntity game = gameRepository.getEntityById(gameId)
                .orElseThrow(GameNotFoundException::new);
        if (game.getGameStatusCode() == GameStatusCode.OPEN) {
            game.setOpponentEntity(userEntity);
            game.setGameStatusCode(GameStatusCode.ACTIVE);
            return game;
        } else {
            throw new GameNotAllowedException("GAME_ALREADY_STARTED");
        }
    }

    public Stream<GameEntity> getAllAvailableGames() {
        return gameRepository.all()
                .filter(gameEntity -> gameEntity.getGameStatusCode() == GameStatusCode.OPEN);
    }

    public Stream<GameEntity> getAllGames() {
        return gameRepository.all();
    }

    public User getGameInfo(UserEntity userEntity) {
        return new User(userEntity.getId(), userEntity.getName(), userEntity.getMove());
    }

    public GameEntity makeMove(Move sign, UserEntity userById, Token token) throws GameNotFoundException {
        GameEntity gameById = getGameById(token.getGameId());
        changeMove(sign, userById, token, gameById);
        return updateGameStatusCode(gameById);
    }

    private GameEntity updateGameStatusCode(GameEntity gameById) {
        UserEntity playerEntity = gameById.getPlayerEntity();
        UserEntity opponentEntity = gameById.getOpponentEntity();

        switch (playerEntity.getMove()) { //Rules
            case ROCK -> {
                switch (opponentEntity.getMove()) {
                    case ROCK -> gameById.setGameStatusCode(GameStatusCode.DRAW);
                    case SCISSORS -> gameById.setGameStatusCode(GameStatusCode.WIN);
                    case PAPER -> gameById.setGameStatusCode(GameStatusCode.LOSE);
                }
            }
            case PAPER -> {
                switch (opponentEntity.getMove()) {
                    case PAPER -> gameById.setGameStatusCode(GameStatusCode.DRAW);
                    case ROCK -> gameById.setGameStatusCode(GameStatusCode.WIN);
                    case SCISSORS -> gameById.setGameStatusCode(GameStatusCode.LOSE);
                }
            }
            case SCISSORS -> {
                switch (opponentEntity.getMove()) {
                    case SCISSORS -> gameById.setGameStatusCode(GameStatusCode.DRAW);
                    case PAPER -> gameById.setGameStatusCode(GameStatusCode.WIN);
                    case ROCK -> gameById.setGameStatusCode(GameStatusCode.LOSE);
                }
            }
            default -> gameById.setGameStatusCode(GameStatusCode.ACTIVE);
        }
        return gameById;
    }

    private void changeMove(Move sign, UserEntity userById, Token token, GameEntity gameById) {
        userById.setMove(sign);
        if (gameById.getPlayerEntity().getId().equals(token.getUserId())) {
            gameById.setPlayerEntity(userById);
        } else if (gameById.getOpponentEntity().getId().equals(token.getUserId())) {
            gameById.setOpponentEntity(userById);
        }
    }

    public GameEntity getGameById(String id) throws GameNotFoundException {
        return gameRepository.getEntityById(id)
                .orElseThrow(GameNotFoundException::new);
    }
}