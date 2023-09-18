package com.cos.security1.config.auth;

// 시큐리티가 /login 주소 요청이 오면 낚아채서 로그인을 진행시킴
// 로그인 진행이 완료가 되면 시큐리티 session을 만들어줌. (Security ContextHolder) 시큐리티 전용의 세션 공간
// 해당 세션 공간에 들어갈 수 있는 오브젝트 타입 => Authentication 타입 객체
// Authentication 안에 User 정보가 있어야 함.
// User오브젝트 타입 => UserDetails 타입 객체


import com.cos.security1.model.User;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

// SecuritySession 영역에는 Authentication 객체만 저장 가능 => UserDetails(현재 PrincipalDetails)
@Data
public class PrincipalDetails implements UserDetails, OAuth2User {

    private User user; // 콤포지션
    private Map<String, Object> attributes;

    // 일반 로그인시 생성자
    PrincipalDetails(User user){
        this.user = user;
    }

    // OAuth 로그인시 생성자
    public PrincipalDetails(User user, Map<String, Object> attributes){
        this.user = user;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    // 해당 유저의 권한을 리턴하는 곳.
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collect = new ArrayList<>();
        collect.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {

                return user.getRole();
            }
        });
        return collect;
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        // 사이트에서 1년동안 로그인하지 않은 회원을 휴먼 회원으로 적용할 때 사용할 수 있는 옵션
        return true;
    }

    @Override
    public String getName() {
        //return attributes.get("sub");
        return null;
    }
}
