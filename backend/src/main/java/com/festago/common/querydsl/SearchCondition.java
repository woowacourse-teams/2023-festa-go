package com.festago.common.querydsl;

import com.festago.common.util.Validator;
import org.springframework.data.domain.Pageable;

public record SearchCondition(
    String searchFilter,
    String searchKeyword,
    Pageable pageable
) {

    public SearchCondition {
        Validator.notNull(pageable, "pageable");
    }

    @Override
    public String searchFilter() {
        return searchFilter != null ? searchFilter : "";
    }

    @Override
    public String searchKeyword() {
        return searchKeyword != null ? searchKeyword : "";
    }
}
