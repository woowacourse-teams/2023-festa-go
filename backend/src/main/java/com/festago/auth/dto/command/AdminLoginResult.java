package com.festago.auth.dto.command;

import com.festago.auth.domain.AuthType;

// TODO Command에서 반환하는 객체의 이름을 어떻게 하면 좋을까
// 버저닝을 사용하지 않고 AdminLoginResponse라고 한 뒤 버저닝 된 컨트롤러에서 AdminLoginV1Response 객체로 변환하여 사용?
// 혹은 지금과 같이 AdminLoginResult와 같이 Response라는 이름을 빼고 반환할지..?
// Controller에서 필요한 응답은 username과 authType임.
// 그리고 accessToken은 쿠키로 반환하기 때문에 다음과 같이 accessToken이 필드로 있게 되면 필요하지 않은 응답이 나감
// 클라이언트에서 필요하지 않은 필드는 무시하지만, accessToken과 같은 보안에 관련된 값일때 필요하지 않은 값은
// 필요가 없다면 보내지 않는게 좋지 않을까?
public record AdminLoginResult(
    String username,
    AuthType authType,
    String accessToken
) {

}
