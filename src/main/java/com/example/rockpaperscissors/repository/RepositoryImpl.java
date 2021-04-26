package com.example.rockpaperscissors.repository;

import com.example.rockpaperscissors.security.Token;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public class RepositoryImpl<E> implements Repository<E> {
    Map<String, E> entities = new HashMap<>();

    @Override
    public Stream<E> all() {
        return entities.values().stream();
    }

    @Override
    public Optional<E> getEntityById(String id) {
        return Optional.ofNullable(entities.get(id));
    }

    @Override
    public E save(String id, E e) {
        return entities.put(id, e);
    }

    @Override
    public String delete(String id) {
        entities.remove(id);
        return id;
    }
}

