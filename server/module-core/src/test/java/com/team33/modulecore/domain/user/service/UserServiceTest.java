package com.team33.modulecore.domain.user.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.team33.modulecore.domain.EnableUserTest;
import com.team33.modulecore.domain.user.UserServiceDto;
import com.team33.modulecore.domain.user.dto.OAuthUserServiceDto;
import com.team33.modulecore.domain.user.dto.UserPostDto;
import com.team33.modulecore.domain.user.dto.UserPostOauthDto;
import com.team33.modulecore.domain.user.entity.User;
import com.team33.modulecore.domain.user.entity.UserRoles;
import com.team33.modulecore.domain.user.repository.UserRepository;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@EnableUserTest
@Transactional
@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class UserServiceTest {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private DuplicationVerifier duplicationVerifier;


    @DisplayName("회원가입을 할 수 있다.")
    @Test
    void 회원가입() throws Exception {
        //given
        UserPostDto postDto = join("test1@gmail.com", "test22", "010-1112-1111");
        UserServiceDto userServiceDto = UserServiceDto.to(postDto);

        //when
        userService.join(userServiceDto);
        List<User> users = userRepository.findAll();

        //then
    	assertThat(users).hasSize(1);
        User user = users.get(0);
        assertThat(user.getEmail()).isEqualTo("test1@gmail.com");
        assertThat(user.getDisplayName()).isEqualTo("test22");
        assertThat(user.getPhone()).isEqualTo("010-1112-1111");
        assertThat(user.getAddress().getCity()).isEqualTo("서울시 부평구 송도동");
        assertThat(user.getRealName()).isEqualTo("홍길동");
        assertThat(user.getAddress().getDetailAddress()).isEqualTo( "101 번지");
    }

    @DisplayName("소셜로그인 시 추가 정보를 기입하면 회원정보가 업데이트된다.")
    @Test
    void 추가_정보_기입() throws Exception {
        //given
        User user = User.builder().realName("testset").email("test@gmail.com").roles(UserRoles.USER).build();
        userRepository.save(user);
        OAuthUserServiceDto oAuthUserServiceDto = OAuthUserServiceDto.to(oauthJoin());

        //when
        User result = userService.addOAuthInfo(oAuthUserServiceDto);

        //then
        assertThat(result.getEmail()).isEqualTo("test@gmail.com");
        assertThat(result.getAddress()).isEqualTo(oAuthUserServiceDto.getAddress());
        assertThat(result.getDisplayName()).isEqualTo(oAuthUserServiceDto.getDisplayName());
        assertThat(result.getPhone()).isEqualTo(oAuthUserServiceDto.getPhone());
    }

    private UserPostDto join(String email, String displayName, String phone) {

        String password = "sdfsdfe!1";
        String city = "서울시 부평구 송도동";
        String detailAddress = "101 번지";
        String realName = "홍길동";

        return UserPostDto.builder()
            .detailAddress(detailAddress)
            .city(city)
            .email(email)
            .phone(phone)
            .realName(realName)
            .password(password)
            .displayName(displayName)
            .build();
    }

    private UserPostOauthDto oauthJoin() {

        String email = "test@gmail.com";
        String displayName = "test2";
        String city = "서울";
        String detailAddress = "압구정동";
        String phone = "010-3333-333";

        return UserPostOauthDto.builder()
            .phone(phone)
            .detailAddress(detailAddress)
            .city(city)
            .email(email)
            .displayName(displayName)
            .build();
    }

}