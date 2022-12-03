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

@WebServlet(name = "home", urlPatterns = "/")
public class Home extends HttpServlet {

    private UserRepository userRepository;
    private SessionRepository sessionRepository;
    private SessionService sessionService;

    public Home() {
        userRepository = new UserRepositoryImpl(DatabaseUtil.getDataSource());
        sessionRepository = new SessionRepositoryImpl(DatabaseUtil.getDataSource());
        sessionService = new SessionService(userRepository, sessionRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        var user = sessionService.current(req);
        if (user == null) {
            req.getRequestDispatcher("/WEB-INF/view/home/index.jsp").forward(req, resp);
        } else {
            req.setAttribute("name", user.getName());
            req.getRequestDispatcher("/WEB-INF/view/home/dashboard.jsp").forward(req, resp);
        }

    }
}
