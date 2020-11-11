package com.codecool.shop.controller;

import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.mem.OrderDaoMem;
import com.codecool.shop.model.order.LineItem;
import com.codecool.shop.model.order.Order;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/line_item"}, loadOnStartup = 3)
public class LineItemsController extends HttpServlet {
    Util util = new Util();
    OrderDao orderDataStore = OrderDaoMem.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException { ;
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonRequest = util.getJsonObjectFromRequest(req);
        Order order = orderDataStore.getActual(Integer.parseInt(util.getCookieValueBy("sessionId", req)));

        LineItem lineItem = addLineItem(jsonRequest, order);
        JsonObject jsonResponse = prepareJsonResponse(order);
        jsonResponse.addProperty("linePrice", lineItem.getLinePrice());

        util.setResponse(resp, jsonResponse);
    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonRequest = util.getJsonObjectFromRequest(req);
        int lineItemId = jsonRequest.get("lineItemId").getAsInt();

        Order order = orderDataStore.getActual(Integer.parseInt(util.getCookieValueBy("sessionId", req)));
        order.getCart().removeLineItemById(lineItemId);

        if (isEmptyCart(order)) {
            orderDataStore.remove(order.getId());
            util.removeCookie(resp);
        }

        JsonObject jsonResponse = prepareJsonResponse(order);
        util.setResponse(resp, jsonResponse);
    }

    private LineItem addLineItem(JsonObject jsonRequest, Order order) {
        int qty = jsonRequest.get("qty").getAsInt();
        int lineItemId = jsonRequest.get("lineItemId").getAsInt();
        LineItem editedLineItem = order.getCart().getLineItemById(lineItemId);
        editedLineItem.setQty(qty);
        return editedLineItem;
    }

    private JsonObject prepareJsonResponse(Order order) {
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("totalCartValue", order.getCart().getLineItemsTotalPrice());
        jsonResponse.addProperty("numberOfProductsInCart", order.getCart().getCartSize());
        return jsonResponse;
    }



    private boolean isEmptyCart(Order order) {
        return order.getCart().getCartSize() == 0;
    }

}
