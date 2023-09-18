package com.cos.security1.config;

import com.cos.security1.config.auth.oauth.PrincipalOauth2UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled =true, prePostEnabled = true) // secureEnabled -> @Secured 어노테이션 활성화, prePostEnabled -> preAuthorize 어노테이션과 postAuthorize 어노테이션 활성화
public class SecurityConfig {

    private PrincipalOauth2UserService principalOauth2UserService;
    // 해당 메서드의 리턴되는 오브젝트를 빈으로 등록함.

    @Autowired
    SecurityConfig(PrincipalOauth2UserService principalOauth2UserService){
        this.principalOauth2UserService = principalOauth2UserService;
    }



    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf().disable();
        http.authorizeRequests()
                .antMatchers("/user/**").authenticated()
                .antMatchers("/manager/**").access("hasAnyRole('ROLE_MANAGER','ROLE_ADMIN')")
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .anyRequest().permitAll()
                .and()
                .formLogin()
                .loginPage("/loginForm")
                .loginProcessingUrl("/login") // /login 주소가 호출되면 시큐리티가 낚아채서 대신 로그인을 진행해줌.
                .defaultSuccessUrl("/")
                .and()
                .oauth2Login()
                .loginPage("/loginForm")
        // 구글 로그인이 완료된 뒤의 후처리가 필요 1. 코드 받기(인증), 2. 엑세스 토큰,
        // 3.사용자 프로필 정보를 가져오고 4-1. 그 정보를 토대로 회원가입을 자동으로 진행시키기도 함.
        // 4-2 (이메일, 전화번호, 이름, 아이디) 이외의 필요 정보들이 추가적으로 필요할 때는 그 해당 정보들만 추가 기입
        // 구글 로그인이 완료된 후의 후처리가 필요함. Tip. 코드 x, (엑세스토큰 + 사용자 프로필 정보O)
                .userInfoEndpoint().userService(principalOauth2UserService);
        return http.build();
    }



}
