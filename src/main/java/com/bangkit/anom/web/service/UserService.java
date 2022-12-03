package com.bangkit.anom.web.service;

import com.bangkit.anom.web.model.*;

public interface UserService {

    UserResponse register(UserRegisterRequest request);

    UserResponse login(UserLoginRequest request);

    UserResponse updatePassword(UserUpdatePasswordRequest request);

    UserResponse updateProfile(UserUpdateProfileRequest request);

}
