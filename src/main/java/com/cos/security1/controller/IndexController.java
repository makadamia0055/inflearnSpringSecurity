package com.cos.security1.controller;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.jboss.jandex.Index;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller // view 를 리턴하겠다.
public class IndexController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @GetMapping({"", "/"}) // 주소를 여러개 매핑
    public String index(){
        // 머스테치 기본 폴더 src/main/resources/
        // 뷰리졸버를 설정 : templates (prefix), .mustache(suffix) 생략 가능
        return "index"; // src/main/resources/templates/index.mustache
    }
    @GetMapping("/user")
    public @ResponseBody String user(){
        return "user";
    }
    @GetMapping("/admin")
    public @ResponseBody  String admin(){
        return "admin";
    }
    @GetMapping("/manager")
    public @ResponseBody  String manager(){
        return "manager";
    }
    
    
    // 스프링 시큐리티가 해당 주소를 낚아채버림 - SecurityConfig 파일 생성후 작동 안함.


    @GetMapping("/loginForm")
    public String loginForm(){
        return "loginForm";
    }

    @GetMapping("/joinForm")
    public String joinForm(){
        return "joinForm";
    }
    @PostMapping("/join")
    public String join(User user){
        System.out.println(user);
        user.setRole("ROLE_USER");
        String rawPassword = user.getPassword();
        String encPassword = bCryptPasswordEncoder.encode(rawPassword);
        user.setPassword(encPassword);
        userRepository.save(user);//회원가입 잘 됨. 그러나 비밀번호 암호화가 없음. 그래서 시큐리티로 로그인을 할 수 없음.
        return "redirect:/loginForm";
    }
    @Secured("ROLE_ADMIN")
    @GetMapping("/info")
    public @ResponseBody String info(){
        return "개인정보";
    }
    @PreAuthorize("hasRole('ROLE_MANAGER') or hasRole('ROLE_ADMIN')") // 이 데이터라는 메소드가 실행되기 직전에 실행되는 메소드, hasRole등은 프레임워크에서 만들어 둔 메소드
    // @PostAuthorize => 이 메소드 종료 후 실행할 메소드 설정
    @GetMapping("/data")
    public @ResponseBody String data(){
        return "데이터";
    }

}
