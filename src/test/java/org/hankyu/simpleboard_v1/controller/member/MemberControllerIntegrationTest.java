package org.hankyu.simpleboard_v1.controller.member;

import org.hankyu.simpleboard_v1.entity.member.Member;
import org.hankyu.simpleboard_v1.exception.MemberNotFoundException;
import org.hankyu.simpleboard_v1.init.TestInitDB;
import org.hankyu.simpleboard_v1.repository.member.MemberRepository;
import org.hankyu.simpleboard_v1.security.WithMockCustomUser;
import org.hankyu.simpleboard_v1.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles(value = "test")
@Transactional
@AutoConfigureMockMvc
public class MemberControllerIntegrationTest {

    @Autowired WebApplicationContext context;
    MockMvc mockMvc;
    @Autowired TestInitDB initDB;
    @Autowired MemberRepository memberRepository;
    @Autowired MemberService memberService;

    Member member1, member2, admin;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.webAppContextSetup(context).apply(springSecurity()).build();
        initDB.initDB();
        member1 = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);
        member2 = memberRepository.findByEmail(initDB.getMember2Email()).orElseThrow(MemberNotFoundException::new);
        admin = memberRepository.findByEmail(initDB.getAdminEmail()).orElseThrow(MemberNotFoundException::new);
    }

    @Test
    void readTest() throws Exception {
        //given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);

        //when, then
        mockMvc.perform(
                get("/member/{id}", member.getId()))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockCustomUser(id = 2, email = "member1@member.com", role = "NORMAL")
    void deleteTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);

        // when, then
        mockMvc.perform(
                        delete("/member/{id}", member.getId())
                )
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void deleteAccessDeniedByAnonymousUserTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);

        // when, then
        mockMvc.perform(
                        delete("/member/{id}", member.getId())
                )
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockCustomUser
    void deleteByAdminTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);

        // when, then
        mockMvc.perform(
                        delete("/member/{id}", member.getId())
                )
                .andExpect(status().isOk());
    }

   /* @Test
    void deleteByAdminTest() throws Exception {
        // given
        Member member = memberRepository.findByEmail(initDB.getMember1Email()).orElseThrow(MemberNotFoundException::new);

        // when, then
        mockMvc.perform(
                        delete("/members/{id}", member.getId()).header("Authorization", adminSignInRes.getAccessToken()))
                .andExpect(status().isOk());
    }*/


}
