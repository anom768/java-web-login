package com.bangkit.anom.web.service;

import at.favre.lib.crypto.bcrypt.BCrypt;
import com.bangkit.anom.web.entity.User;
import com.bangkit.anom.web.model.*;
import com.bangkit.anom.web.repository.UserRepository;

public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse register(UserRegisterRequest request) {
        validateRegister(request);
        String bcrypt = BCrypt.withDefaults().hashToString(12, request.password.toCharArray());

        try {
            User user = new User(request.id, request.name, bcrypt);
            userRepository.insert(user);

            UserResponse response = new UserResponse();
            response.user = user;
            return response;
        } catch (RuntimeException exception) {
            throw exception;
        }
    }


    private void validateRegister(UserRegisterRequest request) {
        User user = userRepository.findById(request.id);
        if (user != null) {
            throw new RuntimeException("id is already exsist");
        }

        if (request.id == null | request.name == null | request.password == null | request.passwordVerify == null |
                request.id.trim().equals("") | request.name.trim().equals("") | request.password.trim().equals("") | request.passwordVerify.trim().equals("")) {
            throw new RuntimeException("id, name and password can not blank");
        }

        if (request.id.length() < 5 | request.password.length() < 5 | request.passwordVerify.length() <= 5) {
            throw new RuntimeException("id and password minimun must 5 characters");
        }

        if (!request.password.equals(request.passwordVerify)) {
            throw new RuntimeException("password not same");
        }

    }

    @Override
    public UserResponse login(UserLoginRequest request) {
        User user = validateLogin(request);

        UserResponse response = new UserResponse();
        response.user = user;

        return response;
    }

    private User validateLogin(UserLoginRequest request) {
        User user = userRepository.findById(request.id);

        if (request.id == null | request.password == null |
                request.id.trim().equals("") | request.password.trim().equals("")) {
            throw new RuntimeException("id and password can not blank");
        }

        if (user == null) {
            throw new RuntimeException("id or password is wrong");
        }

        if (!BCrypt.verifyer().verify(request.password.toCharArray(), user.getPassword()).verified) {
            throw new RuntimeException("id or password is wrong");
        }

        return user;
    }

    @Override
    public UserResponse updatePassword(UserUpdatePasswordRequest request) {
        User user = validateUpdatePassword(request);

        try {
            user.setPassword(BCrypt.withDefaults().hashToString(12, request.newPassword.toCharArray()));
            userRepository.update(user);

            UserResponse response = new UserResponse();
            response.user = user;
            return response;
        } catch (RuntimeException exception) {
            throw exception;
        }
    }

    private User validateUpdatePassword(UserUpdatePasswordRequest request) {
        User user = userRepository.findById(request.id);

        if (request.oldPassword == null | request.newPassword == null | request.newPasswordVerify == null |
                request.oldPassword.trim().equals("") | request.newPassword.trim().equals("") | request.newPasswordVerify.trim().equals("")) {
            throw new RuntimeException("password can not blank");
        }

        if (request.newPassword.length() < 5 | request.newPasswordVerify.length() < 5) {
            throw new RuntimeException("password minimun must 5 characters");
        }

        if (!request.newPasswordVerify.equals(request.newPassword)) {
            throw new RuntimeException("password not same");
        }

        if (!BCrypt.verifyer().verify(request.oldPassword.toCharArray(), user.getPassword()).verified) {
            throw new RuntimeException("old password is wrong");
        }

        if (BCrypt.verifyer().verify(request.newPassword.toCharArray(), user.getPassword()).verified) {
            throw new RuntimeException("can not same with current password");
        }

        return user;

    }

    @Override
    public UserResponse updateProfile(UserUpdateProfileRequest request) {
        validateUpdateProfile(request);

        try {
            User user = userRepository.findById(request.id);
            user.setName(request.newName);
            userRepository.update(user);

            UserResponse response = new UserResponse();
            response.user = user;
            return response;
        } catch (RuntimeException exception) {
            throw exception;
        }
    }


    private void validateUpdateProfile(UserUpdateProfileRequest request) {
        if (request.newName == null | request.newName.trim().equals("")) {
            throw new RuntimeException("name can not blank");
        }

    }

}
