# Setp 준비!

- 요구사항을 명확히 이해하고, 구현하기 위해 발제영상을 다시보고 요구사항을 명확히 작성

## Database Docker Container 생성 Test Container

### DataBase

- mysql:8.0
- database: test_db
- TZ: Asia/Seoul
- file path: [docker-compose.yaml](src/test/resources/docker/docker-compose.yaml)

### 실행

```bash
docker-compose -f docker/docker-compose.yaml up -d
```

### 종료

```bash
docker-compose -f docker/docker-compose.yaml down
```

### 진행하며 겪은 문제

- com.mysql.cj.jdbc.Driver Error
- 해결 방법
    - dependencies에 `runtimeOnly 'com.mysql:mysql-connector-j'` 로 DataBase Driver 추가

## 아키텍처 준수를 위한 애플리케이션 패키지 설계

- Clean + Layerd Architecture 로 설계하기!
- 특정 아키텍쳐가 중요한게 아니라 어떤걸 준수하고 이걸 왜 사용하고 있는지에 대한 이해가 중요하다.

### OCP 원칙

```
- 확장에 대해서는 열려있고, 수정에 대해서는 닫혀있다.
- 하위계층에 변경이 일어날 때 상위계층 변경이 일어나면 안된다.
```

### DIP 원칙

```
- 추상화에 의존해야지, 구체화에 의존하면 안된다.
- 상위 계층은 하위 계층의 구현이 아닌 인터페이스에 의존해야 한다.
```  

### Layerd Architecture

- 상위 계층 -> 하위 계층으로 호출의 단방향 흐름
- 상위 계층의 필요한 로직을 하위 계층의 구현으로 전달
- OCP(X), DIP(O)
- 직접 하위 계층을 의존하기 때문에 OCP를 준수하지 못한다.

### Hexagonal Architecture

- 모든 의존성이 내부(도메인 영영)을 향한다.
- 외부에 연결되는 항목이 독립적이다.
    - 관리포인트가 늘어나지만, 각 연결고리 별로 독립적이기 때문에 책임소재가 명확하게 유지가능하다.
- DIP, OCP를 준수한다.

### Clean Architecture

- Hexagonal Architecture를 확장한 개념
- 추상적으로 표현되어있고 어떻게 짜라는 부분은 없다. ( 모호하다... )

### Clean + Layerd Architecture

#### 흐름

- Contoller -> Service
- Service  <- Repository(interface) <- RepositoryImpl -> ORM Repository(interface)

#### 각 Layer 정의

- Presentation Layer
    - Controller
    - IService(interface)
- Business Layer
    - ServiceImpl (구현)
    - IRepository(interface)
- Datasource Layer
    - RepositoryImpl
    - ORM Repository(interface)

#### 각 역활에대한 책임 정의

- Controller
    - `Request`, `Response`를 처리
    - `Reuqset`를 검증
    - `IService`를 의존
- IService
    - `Service`에 대한 추상화 정의

-----------------

- Service
    - IService(interface)을 구현
        - 비즈니스 로직을 처리
        - `IRepository`를 의존
- IRepository
    - `Repository`에 대한 추상화 정의

-----------------

- RepositoryImpl
    - `IRepository`를 구현
        - `ORM Repository`를 의존
- ORM Repository (Spring Data JPA 사용)
    - `ORM`을 사용하여 `Repository`를 구현

## 도메인 설계

### 특강(lecture)

- id
- 강연 이름
- 강연자 이름
- 강연 날짜

### 신청자(student)

- id
- 신청자 이름

### 특강 신청 이력(lecture_history)

- id
- 특강 id
- 신청자 id
- 신청일자

## 기본 기능 구현

### 필수 조건

- 각 기능에 대한 제약 사항에 대해 단위 테스트 하나 이상 작성
- 다수의 인스턴스로 어플리케이션이 동작 하더라도 문제가 없어야함.
- 동시성 이슈를 고려

### 특강 신청 API

```
- 선착순 신청
- 특강의 정원은 30명 
   - 30명 이상 신청시 실패
- 동일한 사용자는 동일한 강의에 대해서 한번의 수강신청만 성공 가능
- 특강에 신청하기 전 목록 조회

--------- Input ---------
[POST] /lectures
{
  "studentId": "1",
  "lectureId": "1"
}
--------- Output ---------
{
    ...LectureHistory
} 
```

#### 제약조건 검증 항목

- Input Fild 검증
    - studentId, lectureId가 유효하지않다면
        - BadRequestException
- studentId가 없다면 오류 발생
    - ResourceNotFoundException
- 신청 전 특강 정보가 없다면 오류 발생
    - ResourceNotFoundException
- 30명 이상 신청시 31번 째 부터 정원 초과 오류 발생
    - ConflictException
- 동일한 사용자가 동일한 강의에 대해서 두번째 신청시 오류 발생
    - DataBase Entity Unique Key 설정
    - AlreadyExistsException

### 특강 신청 가능 목록 API

```
- 날짜별로 현재 신청이 가능한 특강 목록을 조회 

--------- Input ---------

[GET] /lectures/apply?date=2024-12-22&studentId=1

--------- Output ---------
[
  {
    "id": "1",
    "lectureName": "Spring Boot",
    "lecturer": "Tom",
    "lectureDate": "2024-12-22"
  },
  {
    "id": "2",
    "lectureName": "Spring Cloud",
    "lecturer": "Tom",
    "lectureDate": "2024-12-22"
  }
]
```

#### 제약 조건 검증 항목

- Input Fild 검증
    - date가 날짜 형식이 아니라면 오류 발생
        - BadRequestException
    - studentId가 유효하지 않다면
        - BadRequestException
- user가 존재하지 않으면 오류 발생
    - ResourceNotFoundException
- 신청가능한 특강이 없으면 빈배열

### 특강 신청 완료 목록 조회 API

```
- studentId를 입력받아 신청한 특강 목록을 조회

--------- Input ---------

[GET] /lectures/history?studentId=1

--------- Output ---------
[
  {
    "id": "1",
    "lectureName": "Spring Boot",
    "lecturer": "Tom",
    "lectureDate": "2024-12-22"
  },
  {
    "id": "2",
    "lectureName": "Spring Cloud",
    "lecturer": "Tom",
    "lectureDate": "2024-12-22"
  }
]
```

#### 제약 조건 검증 항목

- studentId가 없다면 오류 발생
    - ResourceNotFoundException
- 신청한 특강이 없으면 빈배열

## Lock

- 위의 필수조건을 만족하면서 동시성 이슈를 고려하여 Lock을 사용하여 해결
- DataBase의 Lock 사용
    - READ : 읽기 작업을 수행할 때 사용
    - WRITE : 쓰기 작업을 수행할 때 사용
    - OPTIMISTIC : 낙관적 락을 사용할 때 사용
    - OPTIMISTIC_FORCE_INCREMENT : 낙관적 락을 사용할 때 사용하며, 엔티티의 버전을 강제로 증가시킬 때 사용
    - PESSIMISTIC_READ : 비관적 락을 사용할 때 사용하며, 읽기 작업을 수행할 때 사용
    - PESSIMISTIC_WRITE : 비관적 락을 사용할 때 사용하며, 쓰기 작업을 수행할 때 사용
    - PESSIMISTIC_FORCE_INCREMENT : 비관적 락을 사용할 때 사용하며, 엔티티의 버전을 강제로 증가시킬 때 사용
    - NONE : 락을 사용하지 않을 때 사용 ( default )

### 비관적락? 낙관적락?

#### 비관적락(Pessimistic Locking)

- DataBase의 Transaction이 시작될 때 Shared Lock, Exclusive Lock을 사용하여 다른 Transaction이 접근하지 못하게 하는 방식
- Transaction이 끝날 때까지 Lock을 유지 다른 작업들은 Lock이 해제될 때까지 대기
- 데이터의 일관성을 보장 ( 데이터를 읽는 동안 다른 트랜잭션이 해당 데이터 변경 할 수 없음 )
- 성능이 낮고 Deadlock 발생 가능성이 높다

#### 낙관적락 (Optimistic Locking)

- 데이터를 읽을 때 락을 걸지 않고 데이터를 업데이트 할 때 이전 데이터와 현재 데이터를 비교하여 충돌 여부를 판단
- 주요 장점으로 읽는 동안 다른 Transaction이 데이터를 변경할 수 있으며, 데이터 충돌이 발생하면 Rollback을 통해 해결
- 성능이 좋고 Dedalock 발생 가능성이 낮다

## 기능에 대한 단위 테스트 작성

### 궁금증1 참고

```Java

@Override
public LectureHistory save(Lecture lecture, Student student) {
    // lectureHistory 생성
    LectureHistory lectureHistory = LectureHistory.createLectureHistory(lecture, student);
    return lectureHistoryJpaRepository.save(lectureHistory);
}
```

에서 DataBase에 저장 하기 위해 lectureHistory 객체를 만들어줘야하는데
lecture, student 객체를 이용해서 객체를 만들게 되면

```Java
LectureHistory lectureHistory = LectureHistory.createLectureHistory(lecture, student);

when(lectureHistoryJpaRepository.save(lectureHistory)).

thenReturn(lectureHistory);
```

테스트 코드에서 `lectureHistoryJpaRepository.save(lectureHistory)`와 코드의 `lectureHistory` 객체가
서로 달라 테스트를 어떻게 진행해야 할지 고민하다 통합 테스트에서 테스트 하는것으로 결론을 내렸는데
이부분에 대한 해결방법을 알고 싶습니다.