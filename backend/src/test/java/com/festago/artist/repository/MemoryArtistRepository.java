package com.festago.artist.repository;

import com.festago.artist.domain.Artist;
import java.lang.reflect.Field;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public class MemoryArtistRepository implements ArtistRepository {

    private final ConcurrentHashMap<Long, Artist> memory = new ConcurrentHashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    public void clear() {
        memory.clear();
    }

    @Override
    @SneakyThrows
    public Artist save(Artist artist) {
        Field idField = artist.getClass()
            .getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(artist, autoIncrement.incrementAndGet());
        memory.put(artist.getId(), artist);
        return artist;
    }

    @Override
    public void deleteById(Long artistId) {
        memory.remove(artistId);
    }

    @Override
    public Optional<Artist> findById(Long id) {
        return Optional.ofNullable(memory.get(id));
    }

    @Override
    public long countByIdIn(List<Long> artistIds) {
        return memory.values().stream()
            .filter(artist -> artistIds.contains(artist.getId()))
            .count();
    }

    @Override
    public List<Artist> findByIdIn(Collection<Long> artistIds) {
        return memory.values().stream()
            .filter(artist -> artistIds.contains(artist.getId()))
            .toList();
    }

    @Override
    public boolean existsById(Long id) {
        return memory.containsKey(id);
    }
}
