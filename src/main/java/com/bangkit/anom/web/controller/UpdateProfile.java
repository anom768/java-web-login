package com.bangkit.anom.web.controller;

import com.bangkit.anom.web.model.UserUpdateProfileRequest;
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

@WebServlet(name = "update-profile", urlPatterns = "/users/profile")
public class UpdateProfile extends HttpServlet {

    private UserRepository userRepository;
    private UserService userService;
    private SessionRepository sessionRepository;
    private SessionService sessionService;

    public UpdateProfile() {
        userRepository = new UserRepositoryImpl(DatabaseUtil.getDataSource());
        userService = new UserServiceImpl(userRepository);
        sessionRepository = new SessionRepositoryImpl(DatabaseUtil.getDataSource());
        sessionService = new SessionService(userRepository, sessionRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        var user = sessionService.current(req);
        req.setAttribute("id", user.getId());
        req.getRequestDispatcher("/WEB-INF/view/user/profile.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserUpdateProfileRequest request = new UserUpdateProfileRequest();
        var user = sessionService.current(req);
        request.id = user.getId();
        request.newName = req.getParameter("name");

        try {
            userService.updateProfile(request);
            resp.sendRedirect("/");
        } catch (RuntimeException exception) {
            exception.printStackTrace();
            req.setAttribute("id", user.getId());
            req.setAttribute("error", exception.getMessage());
            req.getRequestDispatcher("/WEB-INF/view/user/profile.jsp").forward(req, resp);
        }
    }
}
