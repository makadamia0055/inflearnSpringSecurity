package com.cos.security1.config.auth.oauth.provider;

import java.util.Map;

public class NaverUserInfoImp implements OAuth2UserInfo{

    private Map<String, Object> attributes;

    public NaverUserInfoImp(Map<String, Object> attributes){
        this.attributes = attributes;
    }
    @Override
    public String getProviderId() {
        return (String) attributes.get("id");
    }

    @Override
    public String getProvider() {
        return "naver";
    }

    @Override
    public String getEmail() {
        return (String) attributes.get("email");
    }

    @Override
    public String getName() {
        return (String) attributes.get("name");
    }
}
