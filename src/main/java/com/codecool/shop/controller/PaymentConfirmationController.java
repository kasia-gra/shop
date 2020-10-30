package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.jdbc.OrderDaoMem;
import com.codecool.shop.model.order.Order;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@WebServlet(urlPatterns = {"/paymentConfirmation"}, loadOnStartup = 5)
public class PaymentConfirmationController extends HttpServlet {

    Util util = new Util();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext()); //TODO think about refactor duplicated code

        if (util.getCookieValueBy("userId", req) == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            engine.process("product/error.html", context, resp.getWriter());
            return;
        }

        OrderDao orderDataStore = OrderDaoMem.getInstance();
        Order order = orderDataStore.getActual(Integer.parseInt(Objects.requireNonNull(
                util.getCookieValueBy("userId", req))));
        System.out.println(order.getPayment().getCardOwner());

        if (order.getPayment().getCardOwner().equals("Daniel Rzeszutko")) { // draft version of payment validation
            context.setVariable("order", order);
            engine.process("product/paymentConfirmation.html", context, resp.getWriter());
        } else {
            engine.process("product/paymentFail.html", context, resp.getWriter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post");
    }
}
