package com.example.account.domain;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder(buildMethodName = "doseNotUseThisBuild")
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Id
    @GeneratedValue
    // 자동으로 값 생성
    private Long id; // 각 테이블만의 pk를 공통으로 갖고가면 편함(모든 테이블 공통)

    @CreatedDate
    private LocalDateTime createdAt; // 테이블이 갖고있으면 좋음(모든 테이블 공통)
    @LastModifiedDate
    private LocalDateTime updatedAt; // 테이블이 갖고있으면 좋음(모든 테이블 공통)
}
