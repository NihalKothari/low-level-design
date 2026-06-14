package com.lld.common.repository;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * Thread-safe generic in-memory repository base.
 */
public class InMemoryRepository<T> {

    private final Map<String, T> store = new ConcurrentHashMap<>();
    private final Function<T, String> idExtractor;

    public InMemoryRepository(Function<T, String> idExtractor) {
        this.idExtractor = idExtractor;
    }

    public void save(T entity) {
        store.put(idExtractor.apply(entity), entity);
    }

    public Optional<T> findById(String id) {
        return Optional.ofNullable(store.get(id));
    }

    public Collection<T> findAll() {
        return store.values();
    }

    public boolean deleteById(String id) {
        return store.remove(id) != null;
    }

    public void clear() {
        store.clear();
    }
}
