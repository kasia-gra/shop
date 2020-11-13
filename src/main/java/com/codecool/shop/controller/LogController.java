package com.codecool.shop.controller;

import com.codecool.shop.AdminLogger;
import com.codecool.shop.dao.dao.LogfileDao;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.manager.DatabaseManager;
import com.codecool.shop.model.order.Order;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/log"})
public class LogController extends HttpServlet {
    private final LogfileDao logfileDao = DatabaseManager.getInstance().logfileDao;
    private final OrderDao orderDao = DatabaseManager.getInstance().orderDao;
    private final Util util = new Util();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonObject = util.getJsonObjectFromRequest(req);

        Order order = orderDao.getActual(Integer.parseInt(util.getCookieValueBy("sessionId", req)));
        String filename = logfileDao.getBy(order.getId()).getFilename();
        AdminLogger.setFile(util.prepareFile("/admin", filename, req.getServletContext()));

        AdminLogger.appendLogToLogFile(jsonObject);
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", 200);
        util.setResponse(resp, jsonResponse);
    }
}
