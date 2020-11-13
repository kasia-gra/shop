package com.codecool.shop;

import com.codecool.shop.controller.Util;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import javax.servlet.ServletContext;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AdminLogger {
    private static String fileName;
    private static File file;
    private static final Util util = new Util();
    private static final Gson gson = new Gson();

    public static void createLogFile(int orderId, ServletContext context) throws IOException {
        String fileName = generateFileName(orderId);
        file = util.prepareFile("/admin", fileName, context);
        util.saveFile("[]", file);
    }

    private static String generateFileName(int orderId) {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime());
        fileName = orderId + "_" + timeStamp;
        return fileName;
    }

    public static String getFileName() {
        return fileName;
    }

    public static void setFileName(String fileName) {
        AdminLogger.fileName = fileName;
    }

    public static void appendLogToLogFile(JsonObject jsonObject) throws IOException {
        String data = util.readFile(file);
        Type listType = new TypeToken<JsonArray>(){}.getType();
        JsonArray jsonArray = gson.fromJson(data, listType);
        jsonArray.add(jsonObject);
        util.saveFile(gson.toJson(jsonArray), file);
    }

    public static void setFile(File file) {
        AdminLogger.file = file;
    }
}
