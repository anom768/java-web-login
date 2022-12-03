package com.bangkit.anom.web.controller;

import com.bangkit.anom.web.repository.SessionRepository;
import com.bangkit.anom.web.repository.SessionRepositoryImpl;
import com.bangkit.anom.web.repository.UserRepository;
import com.bangkit.anom.web.repository.UserRepositoryImpl;
import com.bangkit.anom.web.service.SessionService;
import com.bangkit.anom.web.util.DatabaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "logout", urlPatterns = "/users/logout")
public class Logout extends HttpServlet {

    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private SessionService sessionService;

    public Logout() {
        userRepository = new UserRepositoryImpl(DatabaseUtil.getDataSource());
        sessionRepository = new SessionRepositoryImpl(DatabaseUtil.getDataSource());
        sessionService = new SessionService(userRepository, sessionRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        sessionService.destroy(req, resp);
        resp.sendRedirect("/");
    }
}
