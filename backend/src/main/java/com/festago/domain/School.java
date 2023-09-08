package com.festago.domain;

import com.festago.exception.ErrorCode;
import com.festago.exception.InternalServerException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class School {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    private String domain;

    @NotNull
    @Size(max = 255)
    private String name;

    protected School() {
    }

    public School(String domain, String name) {
        this(null, domain, name);
    }

    public School(Long id, String domain, String name) {
        validate(domain, name);
        this.id = id;
        this.domain = domain;
        this.name = name;
    }

    private void validate(String domain, String name) {
        checkNotNull(domain, name);
        checkLength(domain, name);
    }

    private void checkNotNull(String domain, String name) {
        if (domain == null ||
            name == null) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void checkLength(String domain, String name) {
        if (overLength(domain, 50) ||
            overLength(name, 255)) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private boolean overLength(String target, int maxLength) {
        if (target == null) {
            return false;
        }
        return target.length() > maxLength;
    }

    public Long getId() {
        return id;
    }

    public String getDomain() {
        return domain;
    }

    public String getName() {
        return name;
    }
}
