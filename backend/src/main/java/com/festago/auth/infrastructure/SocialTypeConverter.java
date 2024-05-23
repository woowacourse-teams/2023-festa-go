package com.festago.auth.infrastructure;

import com.festago.auth.domain.SocialType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SocialTypeConverter implements Converter<String, SocialType> {

    @Override
    public SocialType convert(String socialType) {
        return SocialType.valueOf(socialType.toUpperCase());
    }
}
