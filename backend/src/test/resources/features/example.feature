Feature: 축제 생성

  Scenario: 성공
    Given 로그인을 한 상태에서
    Given "테코대학교"를 생성하고
    Given 축제를 생성하고
    When 축제를 검색하면
    Then 축제가 있다

  Scenario: 의존성 테스트
    Then 전 시나리오에서 생성된 데이터는 없어진다

