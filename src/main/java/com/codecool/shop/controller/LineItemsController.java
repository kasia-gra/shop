package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.dao.ProductDao;
import com.codecool.shop.dao.dao.UserDao;
import com.codecool.shop.dao.jdbc.OrderDaoMem;
import com.codecool.shop.dao.jdbc.ProductDaoMem;
import com.codecool.shop.dao.jdbc.UserDaoMem;
import com.codecool.shop.model.User;
import com.codecool.shop.model.order.Cart;
import com.codecool.shop.model.order.LineItem;
import com.codecool.shop.model.order.Order;
import com.codecool.shop.model.product.Product;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.google.gson.reflect.TypeToken;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sound.sampled.Line;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.*;

@WebServlet(urlPatterns = {"/line_item"}, loadOnStartup = 3)
public class LineItemsController extends HttpServlet {

    Util util = new Util();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { ;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonRequest = util.getJsonObjectFromRequest(req);
        int qty = jsonRequest.get("qty").getAsInt();
        int lineItemId = jsonRequest.get("lineItemId").getAsInt();

        OrderDao orderDataStore = OrderDaoMem.getInstance();
        Order order = orderDataStore.getActual(Integer.parseInt(util.getCookieValueBy("userId", req)));
        LineItem editedLineItem = order.getCart().getLineItemById(lineItemId);
        editedLineItem.setQty(qty);

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("linePrice", editedLineItem.getLinePrice());
        jsonResponse.addProperty("totalCartValue", order.getCart().getLineItemsTotalPrice());
        jsonResponse.addProperty("numberOfProductsInCart", order.getCart().getCartSize() );
        util.setResponse(resp, jsonResponse);
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonRequest = util.getJsonObjectFromRequest(req);
        int lineItemId = jsonRequest.get("lineItemId").getAsInt();

        OrderDao orderDataStore = OrderDaoMem.getInstance();
        Order order = orderDataStore.getActual(Integer.parseInt(util.getCookieValueBy("userId", req)));
        order.getCart().removeLineItemById(lineItemId);

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("totalCartValue", order.getCart().getLineItemsTotalPrice());
        jsonResponse.addProperty("numberOfProductsInCart", order.getCart().getCartSize());
        util.setResponse(resp, jsonResponse);
    }

}
