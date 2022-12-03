package com.bangkit.anom.web.repository;

import com.bangkit.anom.web.entity.User;

public interface UserRepository {

    User insert(User user);

    User update(User user);

    User findById(String id);

    void deleteAll();

}
