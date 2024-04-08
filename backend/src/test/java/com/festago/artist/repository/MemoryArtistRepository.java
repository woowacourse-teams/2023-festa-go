package com.festago.artist.repository;

import com.festago.artist.domain.Artist;
import com.festago.support.AbstractMemoryRepository;
import java.util.Collection;
import java.util.List;

public class MemoryArtistRepository extends AbstractMemoryRepository<Artist> implements ArtistRepository {

    @Override
    public void deleteById(Long artistId) {
        memory.remove(artistId);
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
