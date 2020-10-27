package com.codecool.shop.controller;

import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.dao.implementation.UserDaoMem;
import com.codecool.shop.model.User;
import com.codecool.shop.model.order.Order;
import com.codecool.shop.model.product.Product;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;

@WebServlet(urlPatterns = {"/cart"}, loadOnStartup = 2)
public class CartController extends HttpServlet {
    private Gson gson = new Gson();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("Doing get from CartController");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        OrderDao orderDataStore = OrderDaoMem.getInstance();

        JsonObject jsonRequest = getJsonObjectFromRequest(req);
        int productId = Integer.parseInt(jsonRequest.get("productId").getAsString());
        int userId = Integer.parseInt(jsonRequest.get("userId").getAsString());
        Product product = productDataStore.find(productId);

        Order order = orderDataStore.getActual(userId);
        order.getCart().addProduct(product);

        int itemsNumber = order.getCart().getSize();
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("itemsNumber", itemsNumber);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        out.println(jsonResponse);
        out.flush();
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        OrderDao orderDataStore = OrderDaoMem.getInstance();
        UserDao userDataStore = UserDaoMem.getInstance();

        JsonObject jsonRequest = getJsonObjectFromRequest(req);
        int productId = Integer.parseInt(jsonRequest.get("productId").getAsString());
        Product product = productDataStore.find(productId);

        User user = new User();
        int userId = userDataStore.add(user);

        Order order = new Order(user);
        order.getCart().addProduct(product);
        orderDataStore.add(order);

        int itemsNumber = 1;
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("itemsNumber", itemsNumber);
        jsonResponse.addProperty("userId", userId);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.println(jsonResponse);
        out.flush();
    }

    private JsonObject getJsonObjectFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;

        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null)
            stringBuilder.append(line);
        Type listType = new TypeToken<JsonObject>(){}.getType();
        return gson.fromJson(String.valueOf(stringBuilder), listType);
    }

    private String getCookieValueBy(String name, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return "";
    }
}
