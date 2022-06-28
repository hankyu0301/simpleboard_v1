package org.hankyu.simpleboard_v1.factory.dto;
import org.hankyu.simpleboard_v1.dto.sign.SignUpRequest;

public class SignUpRequestFactory {

    public static SignUpRequest createSignUpRequest() {
        return new SignUpRequest("email@email.com", "123456a!", "username", "nickname");
    }

    public static SignUpRequest createSignUpRequest(String email, String password, String username, String nickname) {
        return new SignUpRequest(email, password, username, nickname);
    }

    public static SignUpRequest createSignUpRequestWithEmail(String email) {
        return new SignUpRequest(email, "123456a!", "username", "nickname");
    }

    public static SignUpRequest createSignUpRequestWithPassword(String password) {
        return new SignUpRequest("email@email.com", password, "username", "nickname");
    }

    public static SignUpRequest createSignUpRequestWithUsername(String username) {
        return new SignUpRequest("email@email.com", "123456a!", username, "nickname");
    }

    public static SignUpRequest createSignUpRequestWithNickname(String nickname) {
        return new SignUpRequest("email@email.com", "123456a!", "username", nickname);
    }
}

