package com.festago.common.querydsl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.NullExpression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import org.springframework.data.domain.Sort;

public class OrderSpecifierUtils {

    public static final OrderSpecifier<?> NULL = new OrderSpecifier(Order.ASC, NullExpression.DEFAULT,
        OrderSpecifier.NullHandling.Default);

    private OrderSpecifierUtils() {
    }

    public static OrderSpecifier of(Sort.Direction direction, Expression<?> target) {
        return new OrderSpecifier(direction.isAscending() ? Order.ASC : Order.DESC, target);
    }
}
