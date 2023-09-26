package com.festago.school.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import jakarta.persistence.Column;
import com.festago.common.util.Validator;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.util.Assert;

@Entity
public class School extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(unique = true)
    private String domain;

    @NotNull
    @Size(max = 255)
    @Column(unique = true)
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
        validateDomain(domain);
        validateName(name);
    }

    private void validateDomain(String domain) {
        Assert.notNull(domain, "domain은 null 값이 될 수 없습니다.");
        Validator.maxLength(domain, 50, "domain은 50글자를 넘을 수 없습니다.");
    }

    private void validateName(String name) {
        Assert.notNull(name, "name은 null 값이 될 수 없습니다.");
        Validator.maxLength(name, 255, "name은 255글자를 넘을 수 없습니다.");
    }

    public void changeDomain(String domain) {
        validateDomain(domain);
        this.domain = domain;
    }

    public void changeName(String name) {
        validateName(name);
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
