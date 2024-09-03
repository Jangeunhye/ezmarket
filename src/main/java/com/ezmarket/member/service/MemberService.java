package com.ezmarket.member.service;

import com.ezmarket.member.domain.entity.Member;
import com.ezmarket.member.dto.MemberDto;

import com.ezmarket.member.repository.MemberRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

@Slf4j
@Service
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional
    public MemberDto signUp(MemberDto memberDto){

        Member member = memberDto.toEntity(passwordEncoder);

        Member savedMember = memberRepository.save(member);

        return MemberDto.ofEntity(savedMember);
    }

    @Transactional(readOnly = true)
    public void checkDuplicateEmail(String email, BindingResult bindingResult){
        boolean isDuplicated = memberRepository.existsByEmail(email);
        if (isDuplicated){
            bindingResult.addError(new FieldError("memberDto","email", "이미 가입된 이메일입니다"));
        }
    }
}
