package com.festago.school.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class School extends BaseTimeEntity {

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
        checkNotNull(domain);
        checkNotNull(name);
        checkLength(domain, 50);
        checkLength(name, 255);
    }

    private void checkNotNull(String input) {
        if (input == null) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    private void checkLength(String input, int maxLength) {
        if (input.length() > maxLength) {
            throw new InternalServerException(ErrorCode.INTERNAL_SERVER_ERROR);
        }
    }

    public void changeDomain(String domain) {
        checkNotNull(domain);
        checkLength(domain, 50);
        this.domain = domain;
    }

    public void changeName(String name) {
        checkNotNull(name);
        checkLength(name, 255);
        this.name = name;
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
