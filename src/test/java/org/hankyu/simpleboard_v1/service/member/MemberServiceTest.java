package org.hankyu.simpleboard_v1.service.member;

import org.hankyu.simpleboard_v1.dto.member.MemberDto;
import org.hankyu.simpleboard_v1.dto.sign.SignUpRequest;
import org.hankyu.simpleboard_v1.entity.member.Member;
import org.hankyu.simpleboard_v1.entity.member.Role;
import org.hankyu.simpleboard_v1.entity.member.RoleType;
import org.hankyu.simpleboard_v1.exception.MemberEmailAlreadyExistsException;
import org.hankyu.simpleboard_v1.exception.MemberNicknameAlreadyExistsException;
import org.hankyu.simpleboard_v1.exception.MemberNotFoundException;
import org.hankyu.simpleboard_v1.exception.RoleNotFoundException;
import org.hankyu.simpleboard_v1.repository.member.MemberRepository;
import org.hankyu.simpleboard_v1.repository.role.RoleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hankyu.simpleboard_v1.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static org.hankyu.simpleboard_v1.factory.entity.MemberFactory.createMember;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks MemberService memberService;
    @Mock MemberRepository memberRepository;
    @Mock RoleRepository roleRepository;
    @Mock PasswordEncoder encoder;

    @Test
    void readTest() {
        //given
        Member member = createMember();
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(member));

        //when
        MemberDto result = memberService.read(1L);

        //then
        assertThat(result.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void readExceptionByMemberNotFoundTest() {
        // given
        given(memberRepository.findById(any())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.read(1L)).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void deleteTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.of(createMember()));

        // when
        memberService.delete(1L);

        // then
        verify(memberRepository).delete(any());
    }

    @Test
    void deleteExceptionByMemberNotFoundTest() {
        // given
        given(memberRepository.findById(anyLong())).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.delete(1L)).isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void signUpTest() {
        // given
        SignUpRequest req = createSignUpRequest();
        given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)).willReturn(Optional.of(new Role(RoleType.ROLE_NORMAL)));

        // when
        memberService.signUp(req);

        // then
        verify(encoder).encode(req.getPassword());
        verify(memberRepository).save(any());
    }

    @Test
    void validateSignUpByDuplicateEmailTest() {
        //given
        given(memberRepository.existsByEmail(anyString())).willReturn(true);

        //when, then
        assertThatThrownBy(()->memberService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberEmailAlreadyExistsException.class);
    }

    @Test
    void validateSignUpByDuplicateNickTest() {
        //given
        given(memberRepository.existsByNickname(anyString())).willReturn(true);

        //when, then
        assertThatThrownBy(()->memberService.signUp(createSignUpRequest()))
                .isInstanceOf(MemberNicknameAlreadyExistsException.class);
    }

    @Test
    void signUpRoleNotFoundTest() {
        // given
        given(roleRepository.findByRoleType(RoleType.ROLE_NORMAL)).willReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> memberService.signUp(createSignUpRequest()))
                .isInstanceOf(RoleNotFoundException.class);
    }
}
