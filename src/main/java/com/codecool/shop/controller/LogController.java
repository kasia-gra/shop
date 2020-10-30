package com.codecool.shop.controller;

import com.codecool.shop.AdminLogger;
import com.google.gson.JsonObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(urlPatterns = {"/log"})
public class LogController extends HttpServlet {
    private final Util util = new Util();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        JsonObject jsonObject = util.getJsonObjectFromRequest(req);
        AdminLogger.appendLogToLogFile(jsonObject);
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("status", 200);
        util.setResponse(resp, jsonResponse);
    }
}
