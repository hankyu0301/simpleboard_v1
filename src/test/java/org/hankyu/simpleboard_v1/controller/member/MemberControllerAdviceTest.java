package org.hankyu.simpleboard_v1.controller.member;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.hankyu.simpleboard_v1.advice.ExceptionAdvice;
import org.hankyu.simpleboard_v1.dto.sign.SignUpRequest;
import org.hankyu.simpleboard_v1.exception.MemberEmailAlreadyExistsException;
import org.hankyu.simpleboard_v1.exception.MemberNotFoundException;
import org.hankyu.simpleboard_v1.exception.RoleNotFoundException;
import org.hankyu.simpleboard_v1.handler.ResponseHandler;
import org.hankyu.simpleboard_v1.service.member.MemberService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.validation.BindException;

import static org.hankyu.simpleboard_v1.factory.dto.SignUpRequestFactory.createSignUpRequest;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.forwardedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class MemberControllerAdviceTest {

    @InjectMocks MemberController memberController;
    @Mock MemberService memberService;
    @Mock ResponseHandler responseHandler;
    MockMvc mockMvc;
    ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void beforeEach() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n/exception");
        mockMvc = MockMvcBuilders.standaloneSetup(memberController).setControllerAdvice(new ExceptionAdvice(responseHandler)).build();
    }

    @Test
    void readMemberNotFoundExceptionTest() throws Exception {
        //given
        given(memberService.read(anyLong())).willThrow(MemberNotFoundException.class);

        //when, then
        mockMvc.perform(
                get("/member/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteMemberNotFoundExceptionTest() throws Exception {
        //given
        doThrow(MemberNotFoundException.class).when(memberService).delete(anyLong());

        //when, then
        mockMvc.perform(
                delete("/member/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void signUpMemberMemberEmailAlreadyExistsExceptionTest() throws Exception {
        //given
        SignUpRequest req = createSignUpRequest("email@email.com", "123456a!", "username", "nickname");
        doThrow(MemberEmailAlreadyExistsException.class).when(memberService).signUp(any());

        //when, then
        mockMvc.perform(
                post("/signUp")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isConflict());
    }

    @Test
    void signUpRoleNotFoundExceptionTest() throws Exception {
        // given
        SignUpRequest req = createSignUpRequest("email@email.com", "123456a!", "username", "nickname");
        doThrow(RoleNotFoundException.class).when(memberService).signUp(any());

        // when, then
        mockMvc.perform(
                        post("/signUp")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isNotFound());
    }

    @Test
    void signUpMethodArgumentNotValidExceptionTest() throws Exception {
        // given
        SignUpRequest req = createSignUpRequest("", "", "", "");

        // when, then
        mockMvc.perform(
                        post("/signUp")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(req)))
                .andExpect(forwardedUrl("/member/signUp"));
    }

}
