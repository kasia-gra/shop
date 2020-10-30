package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.jdbc.OrderDaoMem;
import com.codecool.shop.model.order.Order;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import com.codecool.shop.model.order.LineItem;
import com.google.gson.Gson;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@WebServlet(urlPatterns = {"/paymentConfirmation"}, loadOnStartup = 5)
public class PaymentConfirmationController extends HttpServlet {
    OrderDao orderDataStore = OrderDaoMem.getInstance();
    private  SendEmail sendEmail = new SendEmail();
    private final Util util = new Util();
    private final Gson gson = new Gson();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext()); //TODO think about refactor duplicated code

        if (util.getCookieValueBy("userId", req) == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            engine.process("product/error.html", context, resp.getWriter());
            return;
        }

        Order order = orderDataStore.getActual(Integer.parseInt(Objects.requireNonNull(
                util.getCookieValueBy("userId", req))));

        String htmlMessage =  createEmailHtmlMessage(order).toString();
        sendEmail.sendEmail(order.getUser().getEmail(), htmlMessage);
      
        if (order.getPayment().getCardOwner().equals("Daniel Rzeszutko")) { // draft version of payment validation
            context.setVariable("order", order);
            engine.process("product/paymentConfirmation.html", context, resp.getWriter());
        } else {
            engine.process("product/paymentFail.html", context, resp.getWriter());
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        System.out.println("post");
    }
}
