package com.bangkit.anom.web.repository;

import com.bangkit.anom.web.entity.Session;

public interface SessionRepository {

    Session insert(Session session);

    Session findById(String id);

    void delete(String id);

    void deleteAll();

}
