package com.example.account.repository;

import com.example.account.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
// 첫번째 인자는 여기서 조회할 테이블에 연결된 entity, 두번째 인자는 pk의 타입
public interface AccountUserRepository extends JpaRepository<AccountUser, Long> {

}
