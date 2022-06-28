package org.hankyu.simpleboard_v1.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hankyu.simpleboard_v1.dto.sign.SignUpRequest;
import org.hankyu.simpleboard_v1.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.hankyu.simpleboard_v1.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class MemberControllerTest {

    @InjectMocks MemberController memberController;
    @Mock MemberService memberService;

    ObjectMapper objectMapper = new ObjectMapper();
    MockMvc mockMvc;

    @BeforeEach
    void beforeEach() {
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).build();
    }

    @Test
    void readTest() throws Exception {
        //given
        Long id = 1L;

        //when,then
        mockMvc.perform(
                     get("/member/{id}", id))
                .andExpect(status().isOk())
                .andExpect(view().name("/member/read"));
        verify(memberService).read(id);
    }

    @Test
    void deleteTest() throws Exception {
        // given
        Long id = 1L;

        // when, then
        mockMvc.perform(
                    delete("/member/{id}", id))
                .andExpect(status().isOk())
                .andExpect(redirectedUrl("/"));
        verify(memberService).delete(id);
    }

    @Test
    void signUpTest() throws Exception {
        // given
        SignUpRequest req = createSignUpRequest("email@email.com", "123456a!", "username", "nickname");

        // when, then
        //json으로 넘겨 주고 있음,,
        mockMvc.perform(
                post("/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated());

        verify(memberService).signUp(req);
    }
}
