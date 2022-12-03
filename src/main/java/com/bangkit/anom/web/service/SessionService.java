package com.bangkit.anom.web.service;

import com.bangkit.anom.web.entity.Session;
import com.bangkit.anom.web.entity.User;
import com.bangkit.anom.web.repository.SessionRepository;
import com.bangkit.anom.web.repository.UserRepository;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

public class SessionService {

    private UserRepository userRepository;
    private SessionRepository sessionRepository;

    public SessionService(UserRepository userRepository, SessionRepository sessionRepository) {
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
    }

    public Session create(HttpServletResponse resp, String id_user) {
        Session session = new Session(UUID.randomUUID().toString(), id_user);
        sessionRepository.insert(session);
        Cookie cookie = new Cookie("X-BAS-SESSION", session.getId());
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        resp.addCookie(cookie);
        return session;
    }

    public User current(HttpServletRequest req) {

        var id_session = getIdSession(req);
        var session = sessionRepository.findById(id_session);
        if (session != null) {
            var user = userRepository.findById(session.getId_user());
            return user;
        }
        return null;
    }

    private String getIdSession(HttpServletRequest req) {
        var cookies = req.getCookies();
        String id_session = null;
        if (cookies != null) {
            for (var cookie : cookies) {
                if (cookie.getName().equals("X-BAS-SESSION")) {
                    id_session = cookie.getValue();
                }
            }
        }
        return id_session;
    }

    public void destroy(HttpServletRequest req, HttpServletResponse resp) {
        var id_session = getIdSession(req);
        sessionRepository.delete(id_session);

        Cookie cookie = new Cookie("X-BAS-SESSION", "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        resp.addCookie(cookie);
    }
}
