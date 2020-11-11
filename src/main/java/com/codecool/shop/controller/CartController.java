package com.codecool.shop.controller;

import com.codecool.shop.AdminLogger;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.dao.ProductDao;
import com.codecool.shop.dao.dao.UserDao;
import com.codecool.shop.dao.mem.OrderDaoMem;
import com.codecool.shop.dao.mem.ProductDaoMem;
import com.codecool.shop.dao.mem.UserDaoMem;
import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.order.Order;
import com.codecool.shop.model.product.Product;
import com.codecool.shop.model.user.User;
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
    private final Util util = new Util();
    private final OrderDao orderDataStore = OrderDaoMem.getInstance();
    private final ProductDao productDataStore = ProductDaoMem.getInstance();
    private final UserDao userDataStore = UserDaoMem.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        if (util.isExistingOrder(req)) {
            setContextParameter(req, context);
        }

        engine.process("product/cart.html", context, resp.getWriter());
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonRequest = util.getJsonObjectFromRequest(req);

        Order order = getOrder(jsonRequest);
        addProductToCart(jsonRequest, order);

        JsonObject jsonResponse = prepareJsonResponse(order);

        util.setResponse(resp, jsonResponse);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonRequest = util.getJsonObjectFromRequest(req);
        Product product = getProduct(jsonRequest);

        User user = new User();
        userDataStore.add(user);
        int userId = user.getId();

        Order order = new Order(user);
        addOrderToDataStorage(product, order);

        AdminLogger.createLogFile(order.getId(), getServletContext());

        JsonObject jsonResponse = prepareJsonResponse(order);
        jsonResponse.addProperty("userId", userId);

        util.setResponse(resp, jsonResponse);
    }

    private void addOrderToDataStorage(Product product, Order order) {
        order.getCart().addLineItem(product, order.getCart().getId());
        orderDataStore.add(order);
    }

    private JsonObject prepareJsonResponse(Order order) {
        int itemsNumber = order.getCart().getCartSize();
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("itemsNumber", itemsNumber);
        return jsonResponse;
    }

    private void addProductToCart(JsonObject jsonRequest, Order order) {
        Product product = getProduct(jsonRequest);
        order.getCart().addLineItem(product, order.getCart().getId());
    }

    private Order getOrder(JsonObject jsonRequest) {
        int userId = jsonRequest.get("userId").getAsInt();
        return orderDataStore.getActual(userId);
    }

    private void setContextParameter(HttpServletRequest req, WebContext context) {
        Order order = orderDataStore.getActual(Integer.parseInt(util.getCookieValueBy("userId", req)));
        Cart cart = order.getCart();
        context.setVariable("cart", cart);
    }

    private Product getProduct(JsonObject jsonRequest) {
        int productId = jsonRequest.get("productId").getAsInt();
        return productDataStore.find(productId);
    }
}
