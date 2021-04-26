package com.example.rockpaperscissors.controller;

import com.example.rockpaperscissors.security.Token;
import com.example.rockpaperscissors.services.TokenService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Stream;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class TokenController {

    TokenService tokenService;

    @GetMapping("/new-token")
    public String createToken() {
        Token token = tokenService.createToken();
        return token.getId();
    }

    @GetMapping("/all-tokens")
    public Stream<Token> getAllTokens(){
        return tokenService.getAllTokens();
    }
}
