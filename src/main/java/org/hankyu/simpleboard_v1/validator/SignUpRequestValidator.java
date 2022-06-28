package org.hankyu.simpleboard_v1.validator;

import lombok.RequiredArgsConstructor;
import org.hankyu.simpleboard_v1.dto.sign.SignUpRequest;
import org.hankyu.simpleboard_v1.repository.member.MemberRepository;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;


@RequiredArgsConstructor
@Component
public class SignUpRequestValidator implements Validator {

    private final MemberRepository memberRepository;

    @Override
    public boolean supports(Class<?> clazz) {
        return SignUpRequest.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        SignUpRequest req = (SignUpRequest) target;

        /*ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nickname", "required", "필수 정보 입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "required", "필수 정보 입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "required", "필수 정보 입니다.");
        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "nickname", "required", "필수 정보 입니다.");
        ValidationUtils.rejectIfEmpty(errors, "password", "required", "필수 정보 입니다.");
        ValidationUtils.rejectIfEmpty(errors, "confirmPassword", "required", "필수 정보 입니다.");*/

     /*   if(!req.getPassword().isEmpty()) {
            if(!req.getPassword().equals(req.getConfirmPassword())) {
                errors.rejectValue("confirmPassword", "nomatch", "비밀번호가 일치하지 않습니다.");
            }
        }*/

        if(memberRepository.existsByEmail(req.getEmail())) {
            errors.rejectValue("email", "duplicatedEmail","중복된 이메일 입니다.");
        }
        if(memberRepository.existsByNickname(req.getNickname())) {
            errors.rejectValue("nickname", "duplicatedNickname", "중복된 닉네임 입니다.");
        }
    }
}
