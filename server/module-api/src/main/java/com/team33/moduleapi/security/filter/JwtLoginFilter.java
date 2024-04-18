package com.team33.moduleapi.security.filter;


import com.team33.moduleapi.security.dto.LoginDto;
import java.io.IOException;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.team33.modulecore.utils.Mapper;

@Slf4j
@RequiredArgsConstructor
public class JwtLoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(
                                                    HttpServletRequest request,
                                                    HttpServletResponse response
    ) throws AuthenticationException{
        log.info("로그인 시도");
        LoginDto loginDto = getLoginDto(request);
        UsernamePasswordAuthenticationToken authenticationToken =
            new UsernamePasswordAuthenticationToken(loginDto.getUsername(), loginDto.getPassword());

        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    public void successfulAuthentication(
                                            HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain,
                                            Authentication authResult
    ) throws ServletException, IOException {
        log.info("로그인 성공");

        this.getSuccessHandler().onAuthenticationSuccess(request, response, authResult);
    }

    private LoginDto getLoginDto(HttpServletRequest request) {
        try {
            return Mapper.getInstance().readValue(request.getInputStream(), LoginDto.class);
        } catch (IOException e) {
            throw new AuthenticationServiceException(e.getMessage());
        }
    }
}
