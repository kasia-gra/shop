package com.codecool.shop.controller;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.lang.reflect.Type;

public class Util {

    private final Gson gson = new Gson();


    public void setResponse(HttpServletResponse resp, JsonObject jsonResponse) throws IOException {
        resp.setContentType("application/json");
        resp.setCharacterEncoding("UTF-8");
        PrintWriter out = resp.getWriter();
        out.println(jsonResponse);
        out.flush();
    }

    public JsonObject getJsonObjectFromRequest(HttpServletRequest request) throws IOException {
        StringBuilder stringBuilder = new StringBuilder();
        String line = null;
        BufferedReader reader = request.getReader();
        while ((line = reader.readLine()) != null)
            stringBuilder.append(line);
        Type listType = new TypeToken<JsonObject>(){}.getType();
        return gson.fromJson(String.valueOf(stringBuilder), listType);
    }

    public String getCookieValueBy(String name, HttpServletRequest req) {
        Cookie[] cookies = req.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals(name)) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    public File prepareFile(String relativeDirectoryPath, String filename, ServletContext context) {
        filename += ".json";
        String absoluteDirectoryPath = context.getRealPath(relativeDirectoryPath);
        new File(absoluteDirectoryPath).mkdir();
        return new File(absoluteDirectoryPath, filename);
    }

    public <T> void saveObjectToFile(T object, File file) throws IOException {
        String jsonObject = gson.toJson(object);
        saveFile(jsonObject, file);
    }

    public void saveFile(String jsonObject, File file) throws IOException {
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(jsonObject);
        fileWriter.flush();
    }

    public String readFile(File file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String currentLine = reader.readLine();
        reader.close();
        return currentLine;
    }

    public boolean isExistingOrder(HttpServletRequest request) {
        return getCookieValueBy("userId", request) != null;
    }

    public void showErrorPage(HttpServletResponse resp, TemplateEngine engine, WebContext context) throws IOException {
        resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        engine.process("product/error.html", context, resp.getWriter());
    }

    public void removeCookie(HttpServletResponse resp) {
        Cookie cookie = new Cookie("userId", "");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }
}
