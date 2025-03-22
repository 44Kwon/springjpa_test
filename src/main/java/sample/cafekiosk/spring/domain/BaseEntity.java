package sample.cafekiosk.spring.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * @MappedSuperclass : 공통 필드를 가지는 부모 클래스를 정의할, 해당 클래스를 엔티티로 사용하지 않음(테이블 생성X)
 * 이 클래스를 상속하는 엔티티들은 부모 클래스의 필드 상속받아 자신의 테이블에 포함
 *
 * @EntityListeners(AuditingEntityListener.class)
 * JPA 엔티티의 변경 이벤트(생성, 수정 등)를 감지하는 리스너(Entity Listener)를 등록하는 어노테이션
 * AuditingEntityListener.class를 사용하면, Spring Data JPA의 Auditing 기능을 활용하여 자동으로 생성 및 수정 날짜를 관리할 수 있음
 * @EnableJpaAuditing 활성화 필수
 */
@Getter

@MappedSuperclass

@EntityListeners(AuditingEntityListener.class)

public abstract class BaseEntity {

    @CreatedDate
    private LocalDateTime createdDateTime;

    @LastModifiedDate
    private LocalDateTime modifiedDateTime;
}
