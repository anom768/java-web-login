package com.bangkit.anom.web.controller;

import com.bangkit.anom.web.model.UserRegisterRequest;
import com.bangkit.anom.web.repository.UserRepository;
import com.bangkit.anom.web.repository.UserRepositoryImpl;
import com.bangkit.anom.web.service.UserService;
import com.bangkit.anom.web.service.UserServiceImpl;
import com.bangkit.anom.web.util.DatabaseUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "register", urlPatterns = "/users/register")
public class Register extends HttpServlet {

    private UserRepository userRepository;
    private UserService userService;

    public Register() {
        userRepository = new UserRepositoryImpl(DatabaseUtil.getDataSource());
        userService = new UserServiceImpl(userRepository);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/view/user/register.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UserRegisterRequest request = new UserRegisterRequest();
        request.id = req.getParameter("id");
        request.name = req.getParameter("name");
        request.password = req.getParameter("password");
        request.passwordVerify = req.getParameter("passwordverify");

        try {
            userService.register(request);
            resp.sendRedirect("/");
        } catch (RuntimeException exception) {
            req.setAttribute("error", exception.getMessage());
            req.getRequestDispatcher("/WEB-INF/view/user/register.jsp").forward(req, resp);
        }
    }
}
