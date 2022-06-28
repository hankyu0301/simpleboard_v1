package org.hankyu.simpleboard_v1.config.auth;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hankyu.simpleboard_v1.entity.member.Member;
import org.hankyu.simpleboard_v1.exception.LoginFailureException;
import org.hankyu.simpleboard_v1.repository.member.MemberRepository;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    private final MemberRepository memberRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findWithRolesByEmail(email).orElseThrow(LoginFailureException::new);
        Set<SimpleGrantedAuthority> authorities = member.getRoles().stream()
                .map(memberRole -> memberRole.getRole())
                .map(role -> role.getRoleType())
                .map(roleType -> roleType.toString())
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toSet());

        log.info("member login - id : {}, member-email : {}, authorities : {}", member.getId(),member.getEmail(), member.getRoles());
        return new CustomUserDetails(member.getId(), member.getEmail(), member.getPassword(), authorities);
    }
}
