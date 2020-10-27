package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
        JsonObject jsonProductId = getJsonObjectFromRequest(req);
        int productId = Integer.parseInt(jsonProductId.get("productId").getAsString());

        ProductDao productDataStore = ProductDaoMem.getInstance();
//        Cart cart  = Cart.getInstance();
//        cart.addOrder(new Order(productDataStore.find(productId)));
//
//
//        int itemsNumber = cart.getOrders().stream().mapToInt(Order::getQuantity).sum();

        int itemsNumber = 0;
        JsonObject jsonItemsNumber = new JsonObject();
        jsonItemsNumber.addProperty("itemsNumber", itemsNumber);

        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();

        out.println(jsonItemsNumber);
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
}
