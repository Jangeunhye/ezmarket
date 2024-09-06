package com.ezmarket.member.dto;

import com.ezmarket.member.domain.entity.Member;
import com.ezmarket.member.domain.enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@Getter @Setter
@ToString
@Builder()
@AllArgsConstructor
@NoArgsConstructor
public class MemberDto {

    @NotBlank(message = "이메일을 입력해주세요")
    @Email(message = "이메일 형식이 올바르지 않습니다")
    private String email;

    @NotBlank(message = "이름을 입력해주세요")
    private String name;

    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[!@#$%^&*])(?=.*[0-9]).{8,15}",message = "비밀번호는 8~15자 영문 대소문자, 숫자, 특수문자를 사용하세요")
    private String password;

    @NotBlank(message = "전화번호를 입력해주세요")
    private String phone;

    @NotBlank(message = "주소를 입력해주세요")
    private String address;

    public Member toEntity(PasswordEncoder passwordEncoder){
        return Member.builder()
                .email(this.email)
                .name(this.name)
                .password(passwordEncoder.encode(this.password))
                .address(this.address)
                .phone(this.phone)
                .role(Role.ROLE_ADMIN)
                .build();
    }

    public static MemberDto ofEntity(Member member){
        return MemberDto.builder()
                .email(member.getEmail())
                .name(member.getName())
                .password(member.getPassword())
                .address(member.getAddress())
                .phone(member.getPhone())
                .build();
    }
}
