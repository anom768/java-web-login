package com.bangkit.anom.web.service;

import static org.junit.jupiter.api.Assertions.*;

import com.bangkit.anom.web.entity.User;
import com.bangkit.anom.web.model.UserLoginRequest;
import com.bangkit.anom.web.model.UserRegisterRequest;
import com.bangkit.anom.web.model.UserUpdatePasswordRequest;
import com.bangkit.anom.web.model.UserUpdateProfileRequest;
import com.bangkit.anom.web.repository.UserRepository;
import com.bangkit.anom.web.repository.UserRepositoryImpl;
import com.bangkit.anom.web.util.DatabaseUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import at.favre.lib.crypto.bcrypt.BCrypt;

public class UserServiceImplTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = new UserRepositoryImpl(DatabaseUtil.getDataSource());
        userService = new UserServiceImpl(userRepository);

        userRepository.deleteAll();
    }

    @Test
    void testRegisterSuccess() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.id = "bangkit";
        request.name = "Bangkit";
        request.password = "bangkit";
        request.passwordVerify = "bangkit";

        var response = userService.register(request);

        assertEquals(request.id, response.user.getId());
        assertEquals(request.name, response.user.getName());
        assertTrue(BCrypt.verifyer().verify(request.password.toCharArray(), response.user.getPassword()).verified);
    }

    @Test
    void testRegisterDuplicate() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.id = "bangkit";
        request.name = "Bangkit";
        request.password = "bangkit";
        request.passwordVerify = "bangkit";
        userService.register(request);

        assertThrows(RuntimeException.class, () -> {
            userService.register(request);
        });
    }

    @Test
    void testRegisterNullAttribute() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.id = "bangkit";
        request.name = "Bangkit";
        request.password = "Bangkit";
        request.passwordVerify = null;

        assertThrows(RuntimeException.class, () -> {
            userService.register(request);
        });
    }

    @Test
    void testRegisterBlankAttribute() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.id = "";
        request.name = "";
        request.password = "";
        request.passwordVerify = "";

        assertThrows(RuntimeException.class, () -> {
            userService.register(request);
        });
    }

    @Test
    void testRegisterLengthPassword() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.id = "anom";
        request.name = "Anom";
        request.password = "anom";
        request.passwordVerify = "anom";

        assertThrows(RuntimeException.class, () -> {
            userService.register(request);
        });
    }

    @Test
    void testRegisterDifferentPassword() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.id = "bangkit";
        request.name = "Bangkit";
        request.password = "bangkit";
        request.passwordVerify = "sedhayu";

        assertThrows(RuntimeException.class, () -> {
            userService.register(request);
        });
    }

    @Test
    void testLoginSuccess() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.id = "bangkit";
        request.name = "Bangkit";
        request.password = "bangkit";
        request.passwordVerify = "bangkit";
        userService.register(request);

        UserLoginRequest request2 = new UserLoginRequest();
        request2.id = "bangkit";
        request2.password = "bangkit";

        var response = userService.login(request2);

        assertNotNull(response);
        assertEquals(request.id, response.user.getId());
        assertEquals(request.name, response.user.getName());
        assertTrue(BCrypt.verifyer().verify(request2.password.toCharArray(), response.user.getPassword()).verified);
    }

    @Test
    void testloginNullAttribute() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.id = "bangkit";
        request.name = "Bangkit";
        request.password = "bangkit";
        request.passwordVerify = "bangkit";
        userService.register(request);

        UserLoginRequest request2 = new UserLoginRequest();
        request2.id = null;
        request2.password = null;

        assertThrows(RuntimeException.class, () -> {
            userService.login(request2);
        });
    }

    @Test
    void testloginBlankAttribute() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.id = "bangkit";
        request.name = "Bangkit";
        request.password = "bangkit";
        request.passwordVerify = "bangkit";
        userService.register(request);

        UserLoginRequest request2 = new UserLoginRequest();
        request2.id = "";
        request2.password = "";

        assertThrows(RuntimeException.class, () -> {
            userService.login(request2);
        });
    }

    @Test
    void testloginWrongPassword() {
        UserRegisterRequest request = new UserRegisterRequest();
        request.id = "bangkit";
        request.name = "Bangkit";
        request.password = "bangkit";
        request.passwordVerify = "bangkit";
        userService.register(request);

        UserLoginRequest request2 = new UserLoginRequest();
        request2.id = "bangkit";
        request2.password = "salah";

        assertThrows(RuntimeException.class, () -> {
            userService.login(request2);
        });
    }

    @Test
    void testloginNotFound() {

        UserLoginRequest request2 = new UserLoginRequest();
        request2.id = "bangkit";
        request2.password = "bangkit";

        assertThrows(RuntimeException.class, () -> {
            userService.login(request2);
        });
    }

    @Test
    void testUpdatePasswordSuccess() {
        userRepository.insert(new User("bangkit", "Bangkit", BCrypt.withDefaults().hashToString(12, "bangkit".toCharArray())));

        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest();
        request.id = "bangkit";
        request.oldPassword = "bangkit";
        request.newPassword = "sedhayu";
        request.newPasswordVerify = "sedhayu";
        var response = userService.updatePassword(request);

        assertEquals(request.id, response.user.getId());
        assertTrue(BCrypt.verifyer().verify(request.newPassword.toCharArray(), response.user.getPassword()).verified);
    }

    @Test
    void testUpdatePasswordNullAttribute() {
        userRepository.insert(new User("sedhayu", "Sedhayu", BCrypt.withDefaults().hashToString(12, "sedhayu".toCharArray())));

        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest();
        request.id = "sedhayu";
        request.oldPassword = "sedhayu";
        request.newPassword = null;
        request.newPasswordVerify = null;

        assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(request);
        });
    }

    @Test
    void testUpdatePasswordBlankAttribute() {
        userRepository.insert(new User("sedhayu", "Sedhayu", BCrypt.withDefaults().hashToString(12, "sedhayu".toCharArray())));

        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest();
        request.id = "sedhayu";
        request.oldPassword = "sedhayu";
        request.newPassword = "";
        request.newPasswordVerify = "";

        assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(request);
        });
    }

    @Test
    void testUpdatePasswordLengthAttribute() {
        userRepository.insert(new User("sedhayu", "Sedhayu", BCrypt.withDefaults().hashToString(12, "sedhayu".toCharArray())));

        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest();
        request.id = "sedhayu";
        request.oldPassword = "sedhayu";
        request.newPassword = "anom";
        request.newPasswordVerify = "anom";

        assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(request);
        });
    }

    @Test
    void testUpdatePasswordNotSame() {
        userRepository.insert(new User("sedhayu", "Sedhayu", BCrypt.withDefaults().hashToString(12, "sedhayu".toCharArray())));

        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest();
        request.id = "sedhayu";
        request.oldPassword = "sedhayu";
        request.newPassword = "bangkit";
        request.newPasswordVerify = "12345567";

        assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(request);
        });
    }

    @Test
    void testUpdatePasswordNotSameCurrentPassword() {
        userRepository.insert(new User("sedhayu", "Sedhayu", BCrypt.withDefaults().hashToString(12, "sedhayu".toCharArray())));

        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest();
        request.id = "sedhayu";
        request.oldPassword = "sedhayu";
        request.newPassword = "sedhayu";
        request.newPasswordVerify = "sedhayu";

        assertThrows(RuntimeException.class, () -> {
            userService.updatePassword(request);
        });
    }

    @Test
    void testUpdateProfileSuccess() {
        userRepository.insert(new User("sedhayu", "Sedhayu", BCrypt.withDefaults().hashToString(12, "sedhayu".toCharArray())));

        UserUpdateProfileRequest request = new UserUpdateProfileRequest();
        request.id = "sedhayu";
        request.newName = "Bangkit";
        var response = userService.updateProfile(request);

        assertEquals("Bangkit", response.user.getName());
        assertNotEquals("Sedhayu", response.user.getName());
    }

    @Test
    void testUpdateProfileNullAttribute() {
        userRepository.insert(new User("sedhayu", "Sedhayu", BCrypt.withDefaults().hashToString(12, "sedhayu".toCharArray())));

        UserUpdateProfileRequest request = new UserUpdateProfileRequest();
        request.id = "sedhayu";
        request.newName = null;

        assertThrows(RuntimeException.class, () -> {
            userService.updateProfile(request);
        });
    }

    @Test
    void testUpdateProfileBlankAttribute() {
        userRepository.insert(new User("sedhayu", "Sedhayu", BCrypt.withDefaults().hashToString(12, "sedhayu".toCharArray())));

        UserUpdateProfileRequest request = new UserUpdateProfileRequest();
        request.id = "sedhayu";
        request.newName = "";

        assertThrows(RuntimeException.class, () -> {
            userService.updateProfile(request);
        });
    }

}
