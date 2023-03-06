package com.example.account.dto;

import com.example.account.domain.Account;
import lombok.*;

import java.time.LocalDateTime;

// entity 클래스가 있으면 이 entity 클래스랑 비슷한데 조금 단순화된 버전으로
// 꼭 필요한 것만 넣어둠!

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
// Controller와 Service간 데이터를 주고받는데 최적화된 Dto
public class AccountDto {
    private Long userId;
    private String accountNumber;
    private Long balance;

    private LocalDateTime registeredAt;
    private LocalDateTime unRegisteredAt;

    // 특정 entity에서 특정 dto로 변환해줌
    // entity를 통해서 dto가 많이 만들어짐

    public static AccountDto fromEntity(Account account) {
        // 생성자를 쓰지않고 static 메서드 생성자를 통해서 생성해주면 깔끔한 생성 가능
        return AccountDto.builder()
                .userId(account.getAccountUser().getId())
                .accountNumber(account.getAccountNumber())
                .balance(account.getBalance())
                .registeredAt(account.getRegisteredAt())
                .unRegisteredAt(account.getUnregisteredAt())
                .build();
    }
}
