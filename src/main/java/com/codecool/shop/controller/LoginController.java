package com.codecool.shop.controller;


import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.UserDao;
import com.codecool.shop.dao.manager.DatabaseManager;
import com.codecool.shop.model.AddressDetail;
import com.codecool.shop.model.user.Address;
import com.codecool.shop.model.user.User;

import com.google.gson.JsonObject;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet(urlPatterns = {"/login"}, loadOnStartup = 6)
public class LoginController extends HttpServlet {

    UserDao userDao = DatabaseManager.getInstance().userDao;
    Util util = new Util();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        String message = req.getParameter("message");
        String loginStatus = req.getParameter("login-status");
        System.out.println("LOG STATUS MESSAGE " + loginStatus);
        verifyLoginCredentials(context, message);
        checkIfWantsToLogout(resp, req,loginStatus);
        engine.process("product/userLogin.html", context, resp.getWriter());
    }

    private void checkIfWantsToLogout(HttpServletResponse resp, HttpServletRequest req, String loginStatus) throws IOException {
        if (loginStatus !=null) {
            if (loginStatus.equals("logout")){
                HttpSession session = req.getSession();
                session.invalidate();
                resp.sendRedirect("/");
            }
        }
    }

    private void verifyLoginCredentials(WebContext context, String message) {
        if (message != null) {
            if (message.equals("error"))
                context.setVariable("message", "Incorrect e-mail or password, please try again");
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User user = getUser(req);
        if (user != null) {
            HttpSession session = req.getSession();
            session.setAttribute("name",user.getFirstName());
            resp.sendRedirect("/");
        }
        else {
            resp.sendRedirect("/login?message=error");
        }

    }

    private User getUser(HttpServletRequest request) {
        String password = request.getParameter("password");//TODO hash password
        String email = request.getParameter("email");
        return userDao.findUserByEmailAndPassword(email, password);
    }

}