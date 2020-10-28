package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.model.order.Order;
import com.codecool.shop.model.product.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/checkout"}, loadOnStartup = 3)
public class CheckoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        Map<Product, Integer> orderedProducts = new HashMap<>();
        float totalPrice = 0;
        String cartValue = "0";
        int itemsNumber = 0;
        String currency = "";

        if (CartController.getCookieValueBy("userId", req) != null) {
            OrderDao orderDataStore = OrderDaoMem.getInstance();
            Order order = orderDataStore.getActual(Integer.parseInt(CartController.getCookieValueBy("userId", req)));
            orderedProducts = order.getCart().getProducts();
            totalPrice = order.getCart().getTotalPrice();
            HashMap.Entry<Product, Integer> entry = orderedProducts.entrySet().iterator().next();
            currency = entry.getKey().getDefaultCurrency().getCurrencyCode();
            cartValue = String.format("%.2f", totalPrice);
            itemsNumber = order.getCart().getSize();
        }

        context.setVariable("currency", currency);
        context.setVariable("itemsNumber", itemsNumber);
        context.setVariable("orderedProducts", orderedProducts);
        context.setVariable("cartValue", cartValue);
        engine.process("product/checkout.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post" + req.getParameter("billingFirstName"));
    }
}
