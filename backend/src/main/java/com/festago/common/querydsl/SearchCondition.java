package com.festago.common.querydsl;

import com.festago.common.util.Validator;
import jakarta.annotation.Nonnull;
import org.springframework.data.domain.Pageable;

public record SearchCondition(
    String searchFilter,
    String searchKeyword,
    @Nonnull Pageable pageable
) {

    public SearchCondition {
        Validator.notNull(pageable, "pageable");
    }

    @Nonnull
    @Override
    public String searchFilter() {
        return searchFilter != null ? searchFilter : "";
    }

    @Nonnull
    @Override
    public String searchKeyword() {
        return searchKeyword != null ? searchKeyword : "";
    }
}
