- [x] QR 정보를 받아온다.

```Gherkin
    GIVEN 티켓 ID 를 가지고 있다.
     WHEN QR 정보를 요청한다.
     THEN QR 정보를 받는다.
``` 

- [x] QR을 생성한다.
  - 유효 시간이 얼마나 남았는지 보여준다.

```Gherkin
    GIVEN QR 정보를 받아온 상태이다.
     WHEN QR 을 생성한다.
     THEN 생성된 QR 이 화면에 노출된다.
``` 
