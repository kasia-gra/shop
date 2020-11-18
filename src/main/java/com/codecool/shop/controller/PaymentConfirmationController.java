package com.codecool.shop.controller;

import com.codecool.shop.AdminLogger;
import com.codecool.shop.Log;
import com.codecool.shop.LogType;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.LogfileDao;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.manager.DatabaseManager;
import com.codecool.shop.model.order.LineItem;
import com.codecool.shop.model.order.Order;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;
import java.util.Random;

@WebServlet(urlPatterns = {"/paymentConfirmation"}, loadOnStartup = 5)
public class PaymentConfirmationController extends HttpServlet {
    private final OrderDao orderDao = DatabaseManager.getInstance().orderDao;
    private final LogfileDao logfileDao = DatabaseManager.getInstance().logfileDao;
    private final Util util = new Util();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext()); //TODO think about refactor duplicated code

        if (!util.isExistingOrder(req)) {
            util.showErrorPage(resp, engine, context);
            return;
        }

        Order order = orderDao.getActual(Integer.parseInt(util.getCookieValueBy("sessionId", req)));

        if (chanceOfSucess()) { // draft version of payment validation
            finalizeSuccessfulPayment(resp, context, order);
            engine.process("product/paymentConfirmation.html", context, resp.getWriter());
        } else {
            sendLog(false, req);
            engine.process("product/paymentFail.html", context, resp.getWriter());
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post");
    }

    private void finalizeSuccessfulPayment(HttpServletResponse resp, WebContext context, Order order) throws IOException {
        String htmlMessage = createEmailHtmlMessage(order).toString();
        SendEmail.sendEmail(order.getUser().getEmail(), htmlMessage);
        sendLog(true, context.getRequest());
        context.setVariable("order", order);
        util.removeCookie(resp);
    }

    private void sendLog(boolean isSuccessful, HttpServletRequest req) throws IOException {
        Log log = new Log(new Date(), LogType.PAYMENT, isSuccessful);

        Order order = orderDao.getActual(Integer.parseInt(util.getCookieValueBy("sessionId", req)));
        String filename = logfileDao.getBy(order.getId()).getFilename();
        AdminLogger.setFile(util.prepareFile("/admin", filename, req.getServletContext()));

        AdminLogger.appendLogToLogFile((JsonObject) gson.toJsonTree(log));
    }

    private StringBuffer createEmailHtmlMessage(Order order) {
        StringBuffer output = new StringBuffer();
        List<LineItem> lineItems = order.getCart().getLineItems();
        DecimalFormat df = new DecimalFormat();
        df.setMinimumFractionDigits(2);
        output.append("<h3>CONGRATULATIONS - YOUR PAYMENT HAS BEEN PROCESSED: </h3>");
        output.append("<p>Order ID is: #" + order.getId() + " order total: " + df.format(order.getCart().getLineItemsTotalPrice()) +
                " " + order.getCart().getCartCurrency() + ", see details below: </p>");
        for (int lineIndex = 0; lineIndex < lineItems.size(); lineIndex++) {
            output.append("<p> PRODUCT: " + lineItems.get(lineIndex).getProduct().getName());
            output.append(" - " + lineItems.get(lineIndex).getQty() + " Unit(s)");
            output.append(" x " + df.format(lineItems.get(lineIndex).getLinePrice()) + "  "
                    + order.getCart().getCartCurrency()
                    + " = " + df.format(lineItems.get(lineIndex).getLinePrice())
                    + " " + order.getCart().getCartCurrency() + "</p>");
        }
        return output;
    }

    private boolean chanceOfSucess() {
        Random random = new Random();
        return random.nextBoolean();
    }
}
