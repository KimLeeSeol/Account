package com.example.account.service;

import com.example.account.domain.Account;
import com.example.account.domain.AccountUser;
import com.example.account.dto.AccountDto;
import com.example.account.exception.AccountException;
import com.example.account.repository.AccountRepository;
import com.example.account.repository.AccountUserRepository;
import com.example.account.type.AccountStatus;
import com.example.account.type.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.example.account.type.AccountStatus.UNREGISTERED;
import static com.example.account.type.ErrorCode.*;

@Service
@RequiredArgsConstructor
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountUserRepository accountUserRepository;

    /**
     * 사용자가 있는지 조회
     * 계좌 번호 생성하고
     * 계좌를 저장하고, 그 정보를 넘김
     */

    @Transactional
    public AccountDto createAccount(Long userId, Long initialBalance) {
        // findById(조회) 해서 나오는 기본적인 타입이 optional임
        // 이 optional이 데이터가 없을 때 orElseThrow를 통해서 사용자가 없으면 Exception날림
        // 만약 데이터가 있을 때 값을 줌

        AccountUser accountUser = getAccountUser(userId);

        validateCreateAccount(accountUser);

        // 제일 마지막에 생성된 계좌를 가져와서 그 계좌번호보다 하나 더 큰 숫자를 넣어줌
        String newAccountNumber = accountRepository.findFirstByOrderByIdDesc()
                .map(account -> (Integer.parseInt(account.getAccountNumber())) + 1 + "")
                .orElse("1000000000");

        // 신규 계좌 저장
        // 위에서 찾아왔던 유저
        return AccountDto.fromEntity(
                accountRepository.save(
                Account.builder()
                        .accountUser(accountUser)
                        .accountStatus(AccountStatus.IN_USE)
                        .accountNumber(newAccountNumber)
                        .balance(initialBalance)
                        .registeredAt(LocalDateTime.now())
                        .build())
        );
    }

    //이 사람이 소유하고 있는 계좌의 수가 10개면 예외 발생
    private void validateCreateAccount(AccountUser accountUser) {
        if(accountRepository.countByAccountUser(accountUser) >= 10) {
            throw new AccountException(ErrorCode.MAX_ACCOUNT_PER_USER_10);
        }
    }

    /**
     * 유저(accountNumber)를 조회해서 없으면 에러
     * 사용자 아이디와 계좌 소유주가 다른 경우 에러
     * 계좌가 이미 해지 상태인 경우
     * 잔액이 있는 경우 실패 응답
     */
    @Transactional
    public Account getAccount(Long id) {
        if(id < 0){
            throw new RuntimeException("Minus");
        }
        return accountRepository.findById(id).get();
    }

    @Transactional
    public AccountDto deleteAccount(Long userId, String accountNumber) {
        AccountUser accountUser = getAccountUser(userId);
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new AccountException(ErrorCode.ACCOUNT_NOT_FOUND));
        validateDeleteAccount(accountUser, account);

        account.setAccountStatus(UNREGISTERED);
        account.setUnregisteredAt(LocalDateTime.now());

        accountRepository.save(account); // account에 상태값이 들어갔는지 확인

        return AccountDto.fromEntity(account);
    }

    private void validateDeleteAccount(AccountUser accountUser, Account account) throws AccountException {
        if(!Objects.equals(accountUser.getId(), account.getAccountUser().getId())) {
            throw new AccountException(USER_ACCOUNT_UN_MATCH);
        }
        if(account.getAccountStatus() == UNREGISTERED) {
            throw new AccountException(ACCOUNT_ALREDY_UNREGISTERED);
        }
        if(account.getBalance() > 0) {
            throw new AccountException(BANLANCE_NOT_EMPTY);
        }

    }

    // userId를 받아서 이것에 해당되는 유저들을 조회할 것 임
    // userId로 조회해서 리스트로 가져왔던 accounts를 AccountDto 변환해서 응답을 주고
    @Transactional
    public List<AccountDto> getAccountsByUserId(Long userId) {
        AccountUser accountUser = getAccountUser(userId);

        List<Account> accounts = accountRepository
                .findByAccountUser(accountUser);

        return accounts.stream()
                .map(AccountDto::fromEntity)
                .collect(Collectors.toList());
    }

    private AccountUser getAccountUser(Long userId) {
        return accountUserRepository.findById(userId)
                .orElseThrow(() -> new AccountException(USER_NOT_FOUND));
    }
}
