package com.bangkit.anom.web.controller;

import com.bangkit.anom.web.model.UserLoginRequest;
import com.bangkit.anom.web.repository.SessionRepository;
import com.bangkit.anom.web.repository.SessionRepositoryImpl;
import com.bangkit.anom.web.repository.UserRepository;
import com.bangkit.anom.web.repository.UserRepositoryImpl;
import com.bangkit.anom.web.service.SessionService;
import com.bangkit.anom.web.service.UserService;
import com.bangkit.anom.web.service.UserServiceImpl;
import com.bangkit.anom.web.util.DatabaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "login", urlPatterns = "/users/login")
public class Login extends HttpServlet {

    private UserRepository userRepository;
    private UserService userService;
    private SessionRepository sessionRepository;
    private SessionService sessionService;

    public Login() {
        userRepository = new UserRepositoryImpl(DatabaseUtil.getDataSource());
        userService = new UserServiceImpl(userRepository);
        sessionRepository = new SessionRepositoryImpl(DatabaseUtil.getDataSource());
        sessionService = new SessionService(userRepository, sessionRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/view/user/login.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserLoginRequest request = new UserLoginRequest();
        request.id = req.getParameter("id");
        request.password = req.getParameter("password");

        try {
            var response = userService.login(request);
            sessionService.create( resp , response.user.getId());
            resp.sendRedirect("/");
        } catch (RuntimeException exception) {
            req.setAttribute("error", exception.getMessage());
            req.getRequestDispatcher("/WEB-INF/view/user/login.jsp").forward(req, resp);
        }
    }
}
