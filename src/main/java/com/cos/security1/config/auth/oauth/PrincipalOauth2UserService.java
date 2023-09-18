package com.cos.security1.config.auth.oauth;

import com.cos.security1.config.auth.PrincipalDetails;
import com.cos.security1.config.auth.oauth.provider.FacebookUserInfoImp;
import com.cos.security1.config.auth.oauth.provider.GoogleUserInfoImp;
import com.cos.security1.config.auth.oauth.provider.NaverUserInfoImp;
import com.cos.security1.config.auth.oauth.provider.OAuth2UserInfo;
import com.cos.security1.model.User;
import com.cos.security1.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class PrincipalOauth2UserService extends DefaultOAuth2UserService {

    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    PrincipalOauth2UserService(BCryptPasswordEncoder bCryptPasswordEncoder){
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Autowired
    private UserRepository userRepository;

    // 구글로 부터 받은 userRequest 데이터에 대한 후처리 메소드
    //메소드 종료시 @AuthenticationPrincipal 어노테이션이 만들어진다.

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        System.out.println("userRequest : " + userRequest.getClientRegistration()); // registrationId로 어떤 Oauth로 로그인했는지 알 수 있음.
        System.out.println("userAccessToken : " + userRequest.getAccessToken().getTokenValue());


        OAuth2User oAuth2User = super.loadUser(userRequest);
        // 구글 로그인 버튼 클릭 -> 구글 로그인창 -> 로그인을 완료 -> code를 리턴 (OAuth-client라이브러리)-> AccessToken 요청
        // userRequest 정보 -> loadUser메소드 호출 -> 구글로부터 회원 프로필 받아줌.
        System.out.println("userAttributes : " + super.loadUser(userRequest).getAttributes());


        // 회원가입을 강제로 진행해볼 예정.
        OAuth2UserInfo oAuth2UserInfo = null;

        if(userRequest.getClientRegistration().getRegistrationId().equals("google")){
            System.out.println("구글 로그인 요청");
            oAuth2UserInfo = new GoogleUserInfoImp(oAuth2User.getAttributes());
        }else if(userRequest.getClientRegistration().getRegistrationId().equals("facebook")) {
            System.out.println("페이스북 로그인 요청");
            oAuth2UserInfo = new FacebookUserInfoImp(oAuth2User.getAttributes());

        }else if(userRequest.getClientRegistration().getRegistrationId().equals("naver")){
            System.out.println("네이버 로그인");
            oAuth2UserInfo = new NaverUserInfoImp((Map<String, Object>) oAuth2User.getAttributes().get("response"));

        }else{
            System.out.println("우리는 구글과 페이스북, 네이버만 지원한다. ");
        }


        String provider = oAuth2UserInfo.getProvider();// google
        String providerId = oAuth2UserInfo.getProviderId();

        String username = provider +"_"+ providerId; // google_해당 sub
        String password = bCryptPasswordEncoder.encode("겟인데어"); // 해당 password는 필요없긴한데 (OAuth로 로그인하니까) 아무 값이나 집어넣어줌.
        String email = oAuth2UserInfo.getEmail();
        String role = "ROLE_USER";

        User userEntity = userRepository.findByUsername(username);
        if(userEntity ==null){
            System.out.println("OAuth2이 최초입니다.");
            userEntity = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .role(role)
                    .provider(provider)
                    .providerId(providerId)
                    .build();
            userRepository.save(userEntity);
        }else{
            System.out.println("구글 로그인을 이미 진행한 적이 있어 이미 회원가입된 회원입니다.");
        }
        // PrincipalDetails에서 생성한 생성자로 리턴
        // 해당 PrincipalDetails가 UserDetails와 OAuth2User를 상속(구현)하므로 OAuth2User를 대신해 리턴타입으로 기능함.
        return new PrincipalDetails(userEntity, oAuth2User.getAttributes());
    }
}
