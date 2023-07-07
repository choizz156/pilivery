package team33;

import com.team33.modulecore.ModuleCoreApplication;
import com.team33.modulecore.domain.user.entity.User;
import com.team33.modulecore.domain.user.repository.UserRepository;
import com.team33.modulecore.domain.user.service.UserService;
import com.team33.modulecore.global.security.security.jwt.JwtTokenProvider;
import io.restassured.RestAssured;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = ModuleCoreApplication.class)
@ActiveProfiles({"test"})
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public abstract class ApiTest {

    @LocalServerPort
    private int port;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @BeforeEach
    void beforeEach() {
        RestAssured.port = port;
    }

    @AfterEach
    void afterEach() {
        userRepository.deleteAll();
    }

    protected String getToken() {
        User loginUser = userService.getLoginUser();
        return "Bearer " + jwtTokenProvider.delegateAccessToken(loginUser);
    }
}
