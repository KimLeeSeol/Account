package com.example.account.repository;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    // 맨 첫번째 꺼를 가져올건데 그런데 순서로 정렬해서 가져올건데
    // 정렬순은 Id로 하고 역순으로 가져올 것임

    // 값이 없을 수 있음(계좌가 하나도 없음) 그래서 Optional<Account> 방식으로 가져옴
    Optional<Account> findFirstByOrderByIdDesc();

    Integer countByAccountUser(AccountUser accountUser);

    Optional<Account> findByAccountNumber(String AccountNumber);

    List<Account> findByAccountUser(AccountUser accountUser);
}
