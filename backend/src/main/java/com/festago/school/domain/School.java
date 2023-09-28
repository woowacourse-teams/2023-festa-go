package com.festago.school.domain;

import com.festago.common.domain.BaseTimeEntity;
import com.festago.common.exception.BadRequestException;
import com.festago.common.exception.ErrorCode;
import com.festago.common.exception.InternalServerException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.regex.Pattern;

@Entity
public class School extends BaseTimeEntity {

    private static final Pattern DOMAIN_REGEX = Pattern.compile("^[^.]+(\\.[^.]+)+$");
    private static final char DOMAIN_DELIMITER = '.';

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
        checkNotNull(domain, name);
        checkLength(domain, name);
        validateDomain(domain);
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

    private void validateDomain(String domain) {
        if (!DOMAIN_REGEX.matcher(domain).matches()) {
            throw new BadRequestException(ErrorCode.INVALID_SCHOOL_DOMAIN);
        }
    }
    
    public String findAbbreviation() {
        int dotIndex = domain.indexOf(DOMAIN_DELIMITER);
        return domain.substring(0, dotIndex);
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
