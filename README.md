# EZmarket

## 1. 프로젝트 소개 
간단한 쇼핑몰 프로젝트로 장바구니, 세션 로그인, 장바구니 기능을 구현하였습니다. <br>
백엔드 개발의 기본적인 흐름을 학습하고 실제로 적용해 볼 수 있는 기회였습니다.

- [서비스 바로가기](https://ezmarket.shop)


## 2. 기술스택
- Java 17
- Spring Boot
- JPA
- Thymeleaf
- PostgreSQL
- AWS EC2, RDS, S3
- Spring Security

## 3. 서비스 화면
계정의 권한을 admin과 user로 구분하여 접근 제한을 설정하였습니다.

admin
- 상품 등록
- 모든 주문 접근
<br>

user
- 상품 구매
- 장바구니 담기
- 해당 user 주문 목록만 접근
<br>

![image](https://github.com/user-attachments/assets/5260d87c-ed09-4c77-a8a2-e79d49b4127d)


## 3. 테스트 계정

ADMIN 계정
- 아이디: admin@test.com
- 비밀번호: 1234

USER 계정
- 아이디: user@test.com
- 비밀번호: 1234
