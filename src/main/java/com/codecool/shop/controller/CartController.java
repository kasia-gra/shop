package com.codecool.shop.controller;

import com.codecool.shop.AdminLogger;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.dao.ProductDao;
import com.codecool.shop.dao.manager.DatabaseManager;
import com.codecool.shop.dao.mem.OrderDaoMem;
import com.codecool.shop.model.Session;
import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.order.Order;
import com.codecool.shop.model.product.Product;
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
    private final ProductDao productDao = DatabaseManager.getInstance().productDao;
    private final OrderDao orderDao = DatabaseManager.getInstance().orderDao;

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
        orderDao.addItemToOrder(order, jsonRequest.get("productId").getAsInt());
//        orderDao.update(order);
//        addProductToCart(jsonRequest, order);

        JsonObject jsonResponse = prepareJsonResponse(order);

        util.setResponse(resp, jsonResponse);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonRequest = util.getJsonObjectFromRequest(req);
        Product product = getProduct(jsonRequest);

        Cart cart = new Cart();

        Session session = new Session();

        Order order = new Order(cart, session);

        orderDao.add(order, product.getId());

        AdminLogger.createLogFile(order.getId(), getServletContext());

        JsonObject jsonResponse = prepareJsonResponse(order);
        jsonResponse.addProperty("sessionId", session.getId());

        util.setResponse(resp, jsonResponse);
    }

//    private void addOrderToDataStorage(Product product, Order order) {
//        order.getCart().addLineItem(product, order.getCart().getId());
//        orderDataStore.add(order);
//    }

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
        int sessionId = jsonRequest.get("sessionId").getAsInt();
        return orderDao.getActual(sessionId);
    }

    private void setContextParameter(HttpServletRequest req, WebContext context) {
        Order order = orderDao.getActual(Integer.parseInt(util.getCookieValueBy("sessionId", req)));
        Cart cart = order.getCart();
        context.setVariable("cart", cart);
    }

    private Product getProduct(JsonObject jsonRequest) {
        int productId = jsonRequest.get("productId").getAsInt();
        return productDao.find(productId);
    }
}
