package com.bangkit.anom.web.controller;

import com.bangkit.anom.web.model.UserUpdatePasswordRequest;
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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "update-password", urlPatterns = "/users/password")
public class UpdatePassword extends HttpServlet {

    private UserRepository userRepository;
    private UserService userService;
    private SessionRepository sessionRepository;
    private SessionService sessionService;

    public UpdatePassword() {
        userRepository = new UserRepositoryImpl(DatabaseUtil.getDataSource());
        userService = new UserServiceImpl(userRepository);
        sessionRepository = new SessionRepositoryImpl(DatabaseUtil.getDataSource());
        sessionService = new SessionService(userRepository, sessionRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = sessionService.current(req);
        req.setAttribute("id", user.getId());
        req.getRequestDispatcher("/WEB-INF/view/user/password.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = sessionService.current(req);
        req.setAttribute("id", user.getId());

        UserUpdatePasswordRequest request = new UserUpdatePasswordRequest();
        request.id = user.getId();
        request.oldPassword = req.getParameter("oldPassword");
        request.newPassword = req.getParameter("newPassword");
        request.newPasswordVerify = req.getParameter("newPasswordVerify");

        try {
            userService.updatePassword(request);
            resp.sendRedirect("/");
        } catch (Exception exception) {
            req.setAttribute("error", exception.getMessage());
            req.getRequestDispatcher("/WEB-INF/view/user/password.jsp").forward(req, resp);
        }
    }
}
