package org.hankyu.simpleboard_v1.controller.member;

import lombok.RequiredArgsConstructor;
import org.hankyu.simpleboard_v1.dto.sign.SignUpRequest;
import org.hankyu.simpleboard_v1.service.member.MemberService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/signUp")
    @ResponseStatus(HttpStatus.OK)
    public String signUp(Model model) {
        model.addAttribute("signUpRequest", new SignUpRequest());
        return "/member/signUp";
    }

    @PostMapping("/signUp")
    @ResponseStatus(HttpStatus.CREATED)
    public String signUp(@Valid @RequestBody SignUpRequest req, BindingResult bindingResult) {

        if(bindingResult.hasErrors()) {

            return "/member/signUp";
        }

        memberService.signUp(req);
        return "redirect:/";
    }

    @GetMapping("/member/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String read(@PathVariable Long id,Model model) {
        model.addAttribute("memberDto", memberService.read(id));
        return "/member/read";
    }

    @DeleteMapping("/member/{id}")
    @ResponseStatus(HttpStatus.OK)
    public String delete(@PathVariable Long id) {
        memberService.delete(id);
        return "redirect:/";
    }
}
