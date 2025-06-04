package com.EONET.eonet.controller;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;


@Getter @Setter
public class MemberForm {
    @NotEmpty(message = "id은 필수 입니다")
    private String id;
    @NotEmpty(message = "비밀번호는 필수 입니다")
    private String password;

    private String studentId;
    @NotEmpty(message = "동아대 이메일은 필수 입니다")
    private String email;

    @NotEmpty(message = "카드 번호는 필수 입니다")
    private String cardNumber;
}