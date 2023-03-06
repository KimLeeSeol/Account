package com.example.account.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
// Account의 정보만 따로 뽑아서 사용자에게 응답을 주는,
// Client와 Controller(어플리케이션)간 주고받는 응답
public class AccountInfo {
    private String accountNumber;
    private Long balance;

}
