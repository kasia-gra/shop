package com.codecool.shop.controller;

import com.codecool.shop.AdminLogger;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.LogfileDao;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.dao.ProductDao;
import com.codecool.shop.dao.manager.DatabaseManager;
import com.codecool.shop.model.Logfile;
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
    private final ProductDao productDao = DatabaseManager.getInstance().productDao;
    private final OrderDao orderDao = DatabaseManager.getInstance().orderDao;
    private final LogfileDao logfileDao = DatabaseManager.getInstance().logfileDao;

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

        int addedQuantity = jsonRequest.get("qty").getAsInt();
        int productId = jsonRequest.get("productId").getAsInt();

        Order order = getOrder(jsonRequest);
        orderDao.addItemToOrder(order, productId, addedQuantity);

        JsonObject jsonResponse = prepareJsonResponse(order, productId);

        util.setResponse(resp, jsonResponse);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonRequest = util.getJsonObjectFromRequest(req);
        Product product = getProduct(jsonRequest);
        int productId = product.getId();
        Cart cart = new Cart();

        Session session = new Session();
        Order order = new Order(cart, session);

        orderDao.add(order, productId);

        AdminLogger.createLogFile(order.getId(), getServletContext());
        logfileDao.add(new Logfile(order.getId(), AdminLogger.getFileName()));

        JsonObject jsonResponse = prepareJsonResponse(order, productId);
        jsonResponse.addProperty("sessionId", session.getId());

        util.setResponse(resp, jsonResponse);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonRequest = util.getJsonObjectFromRequest(req);
        int productId = jsonRequest.get("productId").getAsInt();

        Order order = orderDao.getActual(Integer.parseInt(util.getCookieValueBy("sessionId", req)));
        orderDao.removeItem(productId, order.getCart());
//        order.getCart().removeLineItemById(productId);

        if (isEmptyCart(order)) {
            orderDao.remove(order);
            util.removeCookie(resp);
        }

        JsonObject jsonResponse = prepareJsonResponse(order,  productId);
        util.setResponse(resp, jsonResponse);
    }

//    private void addOrderToDataStorage(Product product, Order order) {
//        order.getCart().addLineItem(product, order.getCart().getId());
//        orderDataStore.add(order);
//    }

    private JsonObject prepareJsonResponse(Order order, int productId) {
        int itemsNumber = order.getCart().getCartSize();
        JsonObject jsonResponse = new JsonObject();

        jsonResponse.addProperty("linePrice", order.getCart().getLineItemPriceByProduct(productId));
        jsonResponse.addProperty("productId", productId);
        jsonResponse.addProperty("itemsNumber", itemsNumber);
        jsonResponse.addProperty("totalCartValue", order.getCart().getLineItemsTotalPrice());

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

    private boolean isEmptyCart(Order order) {
        return order.getCart().getCartSize() == 0;
    }
}
