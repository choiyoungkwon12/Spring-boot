package com.web.community.controller;

import com.web.community.annotation.SocialUser;
import com.web.community.domain.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class LoginController {
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping(value = "/{facebook|kakao|google}/complete")
    public String loginComplete(@SocialUser User user) {
        return "redirect:/board/list";
    }

    /*@GetMapping(value = "/{facebook|kakao|google}/complete")
    public String loginComplete(HttpSession session) {
        OAuth2Authentication authentication = (OAuth2Authentication) SecurityContextHolder.getContext().getAuthentication();

        Map<String, String> map = (Map<String, String>) authentication.getUserAuthentication().getDetails();

        session.setAttribute("user", User.builder()
                .name(map.get("name"))
                .email(map.get("email"))
                .principal(map.get("id"))
                .socialType(SocialType.FACEBOOK)
                .createdDate(LocalDateTime.now())
                .build()
        );
        return "redirect:/board/list";
    }*/

}
