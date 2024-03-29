Feature: 사용자가 축제를 조회하는 기능

  Background:
    Given 현재 시간은 2023년 7월 1일 15시 30분 이다.
    Given 어드민 계정으로 로그인한다.
    Given 지역이 "서울"에 있고, 이름이 "테코대학교"이고, 도메인이 "teco.ac.kr"인 학교를 생성한다.
    Given "테코대학교"에서 시작일이 "2023년 07월 10일", 종료일이 "2023년 07월 12일", 이름이 "축제1"인 축제를 생성한다.
    Given "테코대학교"에서 시작일이 "2023년 07월 06일", 종료일이 "2023년 07월 09일", 이름이 "축제2"인 축제를 생성한다.
    Given "테코대학교"에서 시작일이 "2023년 07월 08일", 종료일이 "2023년 07월 10일", 이름이 "축제3"인 축제를 생성한다.
    Given "테코대학교"에서 시작일이 "2023년 07월 12일", 종료일이 "2023년 07월 13일", 이름이 "축제4"인 축제를 생성한다.
    Given "테코대학교"에서 시작일이 "2023년 07월 11일", 종료일이 "2023년 07월 11일", 이름이 "축제5"인 축제를 생성한다.
    Given "테코대학교"에서 시작일이 "2023년 07월 09일", 종료일이 "2023년 07월 10일", 이름이 "축제6"인 축제를 생성한다.
    Given "테코대학교"에서 시작일이 "2023년 07월 04일", 종료일이 "2023년 07월 06일", 이름이 "축제7"인 축제를 생성한다.
    Given "테코대학교"에서 시작일이 "2023년 07월 08일", 종료일이 "2023년 07월 08일", 이름이 "축제8"인 축제를 생성한다.
    Given "테코대학교"에서 시작일이 "2023년 07월 12일", 종료일이 "2023년 07월 12일", 이름이 "축제9"인 축제를 생성한다.
    Given "테코대학교"에서 시작일이 "2023년 07월 05일", 종료일이 "2023년 07월 07일", 이름이 "축제10"인 축제를 생성한다.

  # TODO 순서가 제대로 나오는지 확인할 것 원래는 축제7, 축제10, 축제2 순서였음
  Scenario: 2023년 7월 6일에 진행중인 축제를 조회한다.
    Given 현재 시간은 2023년 7월 6일 15시 30분 이다.
    Then 상태가 "PROGRESS"인 축제를 조회하면 3개의 축제가 조회된다.
    And 조회된 축제 중에서 1번째 축제의 이름은 "축제2" 이어야 한다.
    And 조회된 축제 중에서 2번째 축제의 이름은 "축제10" 이어야 한다.
    And 조회된 축제 중에서 3번째 축제의 이름은 "축제7" 이어야 한다.

  Scenario: 2023년 7월 9일에 진행 예정인 축제를 조회한다.
    Given 현재 시간은 2023년 7월 9일 15시 30분 이다.
    Then 상태가 "PLANNED"인 축제를 조회하면 4개의 축제가 조회된다.
    And 조회된 축제 중에서 1번째 축제의 이름은 "축제1" 이어야 한다.
    And 조회된 축제 중에서 2번째 축제의 이름은 "축제5" 이어야 한다.
    And 조회된 축제 중에서 3번째 축제의 이름은 "축제4" 이어야 한다.
    And 조회된 축제 중에서 4번째 축제의 이름은 "축제9" 이어야 한다.
