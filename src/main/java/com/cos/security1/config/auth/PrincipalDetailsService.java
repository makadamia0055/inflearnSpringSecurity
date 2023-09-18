package com.cos.security1.config.auth;

import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// 시큐리티 설정에서 loginProcessingUrlUrl("/login");
// /login 요청이 오면 자동으로 UserDetailsService 타입으로 DI되어있는 loadUserByUsername 함수가 실행
@Service
public class PrincipalDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    // 시큐리티 Session = Authentication =UserDetails
    // Authentication 내부의 UserDetails에 UserDetails가 들어가고, 시큐리티 Session내부의 Authentication에 다시 Authentication이 들어감.
    //메소드 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // html 파일의 입력 폼의 name을 이 String 매개변수 username에 따라 작성해야한다.
        // 그렇비 않으면 SecurityConfig에서 필터 체인 속에서 loginForm의 usernameParameter를 설정하며 바꾸어주어야 한다.
        User userEntity = userRepository.findByUsername(username);
        if(userEntity!=null){
            return new PrincipalDetails(userEntity);
        }
        return null;
    }
}
