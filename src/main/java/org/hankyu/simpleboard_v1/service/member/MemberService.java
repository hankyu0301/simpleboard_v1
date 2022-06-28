package org.hankyu.simpleboard_v1.service.member;

import lombok.RequiredArgsConstructor;
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
import org.hankyu.simpleboard_v1.util.MailUtil;
import org.hankyu.simpleboard_v1.util.RedisUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final MailUtil mailUtil;
    private final RedisUtil redisUtil;

    public MemberDto read(Long id) {
        return MemberDto.toDto(memberRepository.findById(id).orElseThrow(MemberNotFoundException::new));
    }

    @Transactional
    @PreAuthorize("@memberGuard.check(#id)")
    public void delete(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        memberRepository.delete(member);
    }

    @Transactional
    public void sendSignUpMail(SignUpRequest req) {
        
    }

    @Transactional
    public void signUp(SignUpRequest req) {
        validateSignUpRequest(req);
        String encodedPassword = encoder.encode(req.getPassword());
        List<Role> roles = List.of(roleRepository.findByRoleType(RoleType.ROLE_NORMAL).orElseThrow(RoleNotFoundException::new));
        memberRepository.save(new Member(req.getEmail(), encodedPassword, req.getUsername(), req.getNickname(), roles));
    }

    private void validateSignUpRequest(SignUpRequest req) {
        if(memberRepository.existsByEmail(req.getEmail())) {
            throw new MemberEmailAlreadyExistsException(req.getEmail());
        }
        if(memberRepository.existsByNickname(req.getNickname())) {
            throw new MemberNicknameAlreadyExistsException(req.getNickname());
        }
    }
}
