package org.hankyu.simpleboard_v1.repository.member;

import org.hankyu.simpleboard_v1.config.QuerydslConfig;
import org.hankyu.simpleboard_v1.entity.member.Member;
import org.hankyu.simpleboard_v1.entity.member.MemberRole;
import org.hankyu.simpleboard_v1.entity.member.Role;
import org.hankyu.simpleboard_v1.entity.member.RoleType;
import org.hankyu.simpleboard_v1.exception.MemberNotFoundException;
import org.hankyu.simpleboard_v1.repository.role.RoleRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.DataIntegrityViolationException;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.hankyu.simpleboard_v1.factory.entity.MemberFactory.createMember;
import static org.hankyu.simpleboard_v1.factory.entity.MemberFactory.createMemberWithRoles;

@DataJpaTest
@Import(QuerydslConfig.class)
class MemberRepositoryTest {

    @Autowired MemberRepository memberRepository;
    @Autowired RoleRepository roleRepository;
    @PersistenceContext EntityManager em;

    /*작성해야 할 테스트 목록
    * 1. CRUD가 정상적으로 되는지 확인하는 메서드
    * 2. Entity 생성 할 때 Unique Column으로 등록한 Nickname과 Email이 실제로 unique인지 확인하는 메서드
    * 3. repository에 작성한 메서드 들이 정상적으로 작동하는지 확인하는 메서드
    * 4. cascade 설정한 memberRole이 정상적으로 작동하는지 확인하는 메서드
    * */


    @Test
    void createAndReadTest() {
        //given
        Member member = createMember();

        //when
        memberRepository.save(member);

        //then
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        assertThat(foundMember.getId()).isEqualTo(member.getId());
    }

    @Test
    void memberDateTest() {
        //given
        Member member = createMember();

        //when
        memberRepository.save(member);
        clear();

        //then
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        assertThat(foundMember.getCreatedAt()).isNotNull();
        assertThat(foundMember.getModifiedAt()).isNotNull();
        assertThat(foundMember.getCreatedAt()).isEqualTo(foundMember.getModifiedAt());
    }

    @Test
    void updateTest() {
        //given
        String updatedNickname = "updated";
        Member member = memberRepository.save(createMember());
        clear();

        //when
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
    }

    @Test
    void deleteTest() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when
        memberRepository.delete(member);
        clear();
        assertThatThrownBy(()->memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new))
                .isInstanceOf(MemberNotFoundException.class);
    }

    @Test
    void findByEmailTest() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when
        Member foundMember = memberRepository.findByEmail(member.getEmail()).orElseThrow(MemberNotFoundException::new);

        //then
        assertThat(foundMember.getEmail()).isEqualTo(member.getEmail());
    }

    @Test
    void findByNicknameTest() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when
        Member foundMember = memberRepository.findByNickname(member.getNickname()).orElseThrow(MemberNotFoundException::new);

        //then
        assertThat(foundMember.getNickname()).isEqualTo(member.getNickname());
    }

    @Test
    void uniqueEmailTest() {
        //given
        Member member = memberRepository.save(createMember("email1", "password1", "username1", "nickname1"));
        clear();

        //when, then
        assertThatThrownBy(()->memberRepository.save(createMember(member.getEmail(), "password2", "username2", "nickname2")))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void uniqueNicknameTest() {
        //given
        Member member = memberRepository.save(createMember("email1", "password1", "username1", "nickname1"));
        clear();

        //when, then
        assertThatThrownBy(()->memberRepository.save(createMember("email1", "password2", "username2", member.getNickname())))
                .isInstanceOf(DataIntegrityViolationException.class);
    }

    @Test
    void existsByEmailTest() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when, then
        assertThat(memberRepository.existsByEmail(member.getEmail())).isTrue();
        assertThat(memberRepository.existsByEmail(member.getEmail() + "test")).isFalse();
    }

    @Test
    void existsByNicknameTest() {
        //given
        Member member = memberRepository.save(createMember());
        clear();

        //when, then
        assertThat(memberRepository.existsByNickname(member.getNickname())).isTrue();
        assertThat(memberRepository.existsByNickname(member.getNickname() + "test")).isFalse();
    }

    @Test
    void memberRoleCascadePersistTest() {
        //given
        List<RoleType> roleTypes = List.of(RoleType.ROLE_NORMAL, RoleType.ROLE_ADMIN);
        List<Role> roles = roleTypes.stream().map(roleType -> new Role(roleType)).collect(Collectors.toList());
        roleRepository.saveAll(roles);
        clear();

        Member member = memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
        clear();

        //when
        Member foundMember = memberRepository.findById(member.getId()).orElseThrow(MemberNotFoundException::new);
        Set<MemberRole> memberRoleSet = foundMember.getRoles();

        //then
        assertThat(memberRoleSet.size()).isEqualTo(roles.size());
    }

    @Test
    void memberRoleCascadeDeleteTest() {
        // given
        List<RoleType> roleTypes = List.of(RoleType.ROLE_NORMAL, RoleType.ROLE_ADMIN);
        List<Role> roles = roleTypes.stream().map(roleType -> new Role(roleType)).collect(toList());
        roleRepository.saveAll(roles);
        clear();

        Member member = memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
        clear();

        // when
        memberRepository.deleteById(member.getId());
        clear();

        // then
        List<MemberRole> result = em.createQuery("select mr from MemberRole mr", MemberRole.class).getResultList();
        assertThat(result.size()).isZero();
    }

    @Test
    void findWithRolesByEmailTest() {
        // given
        List<RoleType> roleTypes = List.of(RoleType.ROLE_NORMAL,RoleType.ROLE_ADMIN);
        List<Role> roles = roleTypes.stream().map(roleType -> new Role(roleType)).collect(toList());
        roleRepository.saveAll(roles);
        Member member = memberRepository.save(createMemberWithRoles(roleRepository.findAll()));
        clear();

        // when
        Member foundMember = memberRepository.findWithRolesByEmail(member.getEmail()).orElseThrow(MemberNotFoundException::new);

        // then
        List<RoleType> result = foundMember.getRoles().stream().map(memberRole -> memberRole.getRole().getRoleType()).collect(toList());
        assertThat(result.size()).isEqualTo(roleTypes.size());
        assertThat(result).contains(roleTypes.get(0), roleTypes.get(1));
    }


    private void clear() {
        em.flush();
        em.clear();
    }
}
