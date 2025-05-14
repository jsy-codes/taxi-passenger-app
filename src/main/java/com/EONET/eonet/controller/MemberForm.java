package com.EONET.eonet.controller;

import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotEmpty;


@Getter @Setter
public class MemberForm {
    private Long id;

    private String userName;
    @NotEmpty(message = "비밀번호는 필수 입니다")
    private String password;

    private String studentId;
    @NotEmpty(message = "동아대 이메일은 필수 입니다")
    private String email;

}