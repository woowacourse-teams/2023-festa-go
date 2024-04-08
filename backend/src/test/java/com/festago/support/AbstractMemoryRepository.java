package com.festago.support;

import jakarta.persistence.Id;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import lombok.SneakyThrows;

public abstract class AbstractMemoryRepository<T> {

    protected final HashMap<Long, T> memory = new HashMap<>();
    private final AtomicLong autoIncrement = new AtomicLong();

    @SneakyThrows
    final public T save(T entity) {
        Field[] fields = entity.getClass()
            .getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Id.class)) {
                field.setAccessible(true);
                long id = autoIncrement.incrementAndGet();
                field.set(entity, id);
                memory.put(id, entity);
                return entity;
            }
        }
        throw new IllegalArgumentException("해당 엔티티에 @Id 어노테이션이 붙은 식별자가 존재하지 않습니다.");
    }

    final public Optional<T> findById(Long id) {
        return Optional.ofNullable(memory.get(id));
    }

    final public boolean existsById(Long id) {
        return memory.containsKey(id);
    }

    final public void deleteById(Long id) {
        memory.remove(id);
    }

    final public long count() {
        return memory.size();
    }
}
