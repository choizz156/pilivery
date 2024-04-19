package com.team33.modulecore.user.application;

import com.team33.modulecore.user.dto.UserServicePostDto;
import com.team33.modulecore.user.dto.OAuthUserServiceDto;
import com.team33.modulecore.user.dto.UserServicePatchDto;
import com.team33.modulecore.user.domain.User;
import com.team33.modulecore.user.repository.UserRepository;
import com.team33.modulecore.exception.BusinessLogicException;
import com.team33.modulecore.exception.ExceptionCode;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Slf4j
@Component
public class DuplicationVerifier {

    private final UserRepository userRepository;

    public void checkUserInfo(UserServicePostDto dto) {
        checkExistEmail(dto.getEmail());
        checkExistDisplayName(dto.getDisplayName());
        checkExistPhoneNum(dto.getPhone());
    }

    public void checkOauthAdditionalInfo(OAuthUserServiceDto dto) {
        checkExistDisplayName(dto.getDisplayName());
        checkExistPhoneNum(dto.getPhone());
    }

    public void checkDuplicationOnUpdate(UserServicePatchDto dto, User user) {
            checkExistPhoneOnUpdate(user, dto);
            checkExistDisplayNameOnUpdate(user, dto);
    }

    private void checkExistPhoneOnUpdate(User user, UserServicePatchDto userDto) {
        if (isTheSameMyPhone(user, userDto)) {
            return;
        }
        checkExistPhoneNum(userDto.getPhone());
    }

    private void checkExistDisplayNameOnUpdate(User loginUser, UserServicePatchDto userDto) {
        if (isTheSameMyDisplayName(loginUser, userDto)) {
            return;
        }
        checkExistDisplayName(userDto.getDisplayName());
    }

    private boolean isTheSameMyDisplayName(User loginUser, UserServicePatchDto userDto) {
        return loginUser.getDisplayName().equals(userDto.getDisplayName());
    }

    private boolean isTheSameMyPhone(User loginUser, UserServicePatchDto userDto) {
        return loginUser.getPhone().equals(userDto.getPhone());
    }

    private void checkExistDisplayName(String displayName) {
        log.info("displayName = {}", displayName);
        Optional<User> user = userRepository.findByDisplayName(displayName);
        if (user.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.EXIST_DISPLAY_NAME);
        }
    }

    private void checkExistPhoneNum(String phoneNum) {
        log.info("phone = {}", phoneNum);
        Optional<User> user = userRepository.findByPhone(phoneNum);
        if (user.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.EXIST_PHONE_NUMBER);
        }
    }

    private void checkExistEmail(String email) {
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            throw new BusinessLogicException(ExceptionCode.EXIST_EMAIL);
        }
    }
}
