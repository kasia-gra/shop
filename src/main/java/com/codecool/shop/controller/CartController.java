package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.dao.ProductDao;
import com.codecool.shop.dao.dao.UserDao;
import com.codecool.shop.dao.jdbc.OrderDaoMem;
import com.codecool.shop.dao.jdbc.ProductDaoMem;
import com.codecool.shop.dao.jdbc.UserDaoMem;
import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.order.Order;
import com.codecool.shop.model.product.Product;
import com.codecool.shop.model.user.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/cart"}, loadOnStartup = 2)
public class CartController extends HttpServlet {
    private Gson gson = new Gson();
    private Util util = new Util();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        Cart cart = new Cart();
        if (util.getCookieValueBy("userId", req) != null) {
            OrderDao orderDataStore = OrderDaoMem.getInstance();
            Order order = orderDataStore.getActual(Integer.parseInt(util.getCookieValueBy("userId", req)));
            cart = order.getCart();
        }
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        context.setVariable("cart", cart);
        engine.process("product/cart.html", context, resp.getWriter());
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        OrderDao orderDataStore = OrderDaoMem.getInstance();

        JsonObject jsonRequest = util.getJsonObjectFromRequest(req);
        int productId = Integer.parseInt(jsonRequest.get("productId").getAsString());
        int userId = Integer.parseInt(jsonRequest.get("userId").getAsString());
        Product product = productDataStore.find(productId);

        Order order = orderDataStore.getActual(userId);
        order.getCart().addLineItem(product);
        int itemsNumber = order.getCart().getCartSize();
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("itemsNumber", itemsNumber);

        util.setResponse(resp, jsonResponse);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        OrderDao orderDataStore = OrderDaoMem.getInstance();
        UserDao userDataStore = UserDaoMem.getInstance();

        JsonObject jsonRequest = util.getJsonObjectFromRequest(req);
        int productId = Integer.parseInt(jsonRequest.get("productId").getAsString());
        Product product = productDataStore.find(productId);

        User user = new User();
        int userId = userDataStore.add(user);

        Order order = new Order(user);
        order.getCart().addLineItem(product);

        orderDataStore.add(order);

        int itemsNumber = 1;
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("itemsNumber", itemsNumber);
        jsonResponse.addProperty("userId", userId);

        util.setResponse(resp, jsonResponse);
    }
}
