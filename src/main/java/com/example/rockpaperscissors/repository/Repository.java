package com.example.rockpaperscissors.repository;

import com.example.rockpaperscissors.exceptions.TokenNotFoundException;
import com.example.rockpaperscissors.security.Token;

import java.util.Optional;
import java.util.stream.Stream;

public interface Repository<E> {
    Stream<E> all();

    Optional<E> getEntityById(String e) throws TokenNotFoundException;

    E save(String id, E e);

    String delete(String e);
}
