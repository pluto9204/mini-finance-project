# 🏦 Mini Finance Project

> Kafka, RabbitMQ, Redis, Docker, Spring Boot 기반의 금융 마이크로서비스 프로젝트  
> 인증 → 대출 신청 → 심사 처리 → 알림 발송 전 과정을 비동기 메시징 기반으로 처리

## 📌 프로젝트 구조

```
mini-finance-project/
│
├── auth-service              # 사용자 인증 서비스 (JWT + Redis 블랙리스트)
├── loan-service              # 대출 신청 서비스 (Kafka로 메시지 발행)
├── loan-processor            # 대출 심사 + 결과 저장 (Kafka 소비, RabbitMQ 발행)
├── notification-service      # 심사 결과 알림 발송 (RabbitMQ 소비)
├── common                    # 공통 DTO 및 예외 처리 모듈
└── docker-compose.yml        # 전체 환경 구성
```

## 🚀 서비스 흐름 요약

1. **사용자 인증 (auth-service)**
   - JWT 발급, Redis로 블랙리스트 처리
   - React에서 로그인 요청 시 `Authorization: Bearer <token>` 방식 사용

2. **대출 신청 (loan-service)**
   - 신청 정보 저장 후 Kafka(`loan-application-topic`)로 메시지 발행

3. **대출 심사 (loan-processor)**
   - Kafka 메시지 소비
   - 임의 심사 로직 수행 후 결과 저장
   - RabbitMQ(`loan-decision-queue`)로 결과 발송

4. **알림 발송 (notification-service)**
   - RabbitMQ 메시지 소비
   - 문자 또는 알림 처리 전담 (DB 저장은 하지 않음)

## 🧨 주요 트러블슈팅 정리

### 1. ✅ Kafka 역직렬화 실패

- **문제:** Kafka Consumer에서 메시지 역직렬화 실패 (`Could not deserialize value of type`)
- **원인:** 메시지 클래스 구조 불일치 (예: 필드 이름, 타입 다름)
- **해결:** DTO 구조 통일, `@JsonProperty` 명시, `ObjectMapper` 설정 보강

### 2. ✅ Kafka + DB 트랜잭션 분리 이슈

- **문제:** Kafka 발행 성공했지만 DB 저장 실패 또는 반대 상황 발생
- **해결:** DB 저장은 loan-service에서 하고, Kafka 소비 + 결과 저장은 **loan-processor로 분리**  
- **결과:** 트랜잭션은 loan-processor 단에서만 관리하며 원자성을 보장함

### 3. ✅ Docker Compose 간 통신 문제

- **문제:** `auth-service`에서 `loan-service` 등 다른 서비스로의 통신이 실패
- **원인:** `localhost`가 아닌 Docker Compose 내 서비스명을 사용해야 함
- **해결:** `http://loan-service:8080` 등으로 통신 주소 변경

### 4. ✅ JWT 인증 오류

- **문제:** React에서 로그인 후 API 호출 시 인증 실패
- **원인:** JWT를 `localStorage`에 저장 후 헤더에 포함하지 않음
- **해결:** Axios 요청에 `Authorization: Bearer <token>` 추가

### 5. ✅ 프론트엔드 수정 시 Docker 반영 안됨

- **문제:** `.tsx` 파일 수정 후에도 화면 반영되지 않음
- **원인:** `vite` 핫리로딩 설정 부족 or `docker volume` 미지정
- **해결:** `docker-compose.override.yml`로 볼륨 마운트, `npm run dev` 사용

### 6. ✅ 대출신청 결과 저장 위치 재조정

- **문제:** 알림 서비스(`notification-service`)가 결과 저장도 수행함 → 역할 모호
- **해결:** 대출 결과 저장은 `loan-processor`로 이관  
  → `notification-service`는 단순히 메시지 소비 후 문자 발송만 담당

## 🛠️ 기술 스택

- **Backend:** Java 17, Spring Boot, Spring Security
- **Message Queue:** Apache Kafka, RabbitMQ
- **Database:** PostgreSQL
- **Infra:** Docker, Docker Compose, Redis
- **Frontend:** React, TypeScript, Vite

## 📦 향후 개선 계획

- [ ] Outbox/Event Log 패턴 적용
- [ ] Kafka 메시지 전송 결과 Callback 적용
- [ ] 대출 재심사 기능 추가
- [ ] 클라우드 환경(EKS, S3 등)으로 이전