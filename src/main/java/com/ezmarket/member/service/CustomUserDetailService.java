package com.ezmarket.member.service;

import com.ezmarket.member.domain.entity.Member;
import com.ezmarket.member.dto.CustomUserDetails;
import com.ezmarket.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Member member = memberRepository.findByEmail(username).orElseThrow(()->{
            return new UsernameNotFoundException("해당 이메일을 찾을 수 없습니다.");
        });

      return new CustomUserDetails(member);
    }



}
