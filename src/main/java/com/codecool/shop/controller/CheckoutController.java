package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.dao.OrderDao;
import com.codecool.shop.dao.dao.UserDao;
import com.codecool.shop.dao.mem.OrderDaoMem;
import com.codecool.shop.dao.mem.UserDaoMem;
import com.codecool.shop.model.order.LineItem;
import com.codecool.shop.model.order.Order;
import com.codecool.shop.model.user.Address;
import com.codecool.shop.model.user.User;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(urlPatterns = {"/checkout"}, loadOnStartup = 3)
public class CheckoutController extends HttpServlet {
    Util util = new Util();
    OrderDao orderDataStore = OrderDaoMem.getInstance();
    UserDao userDataStorage = UserDaoMem.getInstance();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        if (!util.isExistingOrder(req)) {
            util.showErrorPage(resp, engine, context);
            return;
        }

        Order order = orderDataStore.getActual(Integer.parseInt(util.getCookieValueBy("sessionId", req)));
        List<LineItem> orderedProducts = order.getCart().getLineItems();
        float totalPrice = order.getCart().getLineItemsTotalPrice();
        String currency = order.getCart().getCartCurrency();
        String cartValue = String.format("%.2f", totalPrice);
        int itemsNumber = order.getCart().getCartSize();

        setContextParameters(context, orderedProducts, currency, cartValue, itemsNumber);
        engine.process("product/checkout.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String sessionId = util.getCookieValueBy("sessionId", req);

        User user = userDataStorage.find(Integer.parseInt(sessionId));
        setUserParameters(user, req);
//        RequestDispatcher dispatcher = getServletContext().getRequestDispatcher("/payment");
//        dispatcher.forward(req, resp);
        resp.sendRedirect("/payment");
    }

    private void setContextParameters(WebContext context, List<LineItem> orderedProducts, String currency, String cartValue, int itemsNumber) {
        context.setVariable("currency", currency);
        context.setVariable("itemsNumber", itemsNumber);
        context.setVariable("orderedProducts", orderedProducts);
        context.setVariable("cartValue", cartValue);
    }

    private void setUserParameters(User user, HttpServletRequest request) {
        user.setFirstName(request.getParameter("firstName"));
        user.setLastName(request.getParameter("lastName"));
        user.setEmail(request.getParameter("email"));
        user.setPhone(request.getParameter("phone"));
        user.setBillingAddress(getBillingAddress(request));
        if (request.getParameter("sameAddress").equals("on")) {
            user.setShippingAddress(getBillingAddress(request));
        } else user.setShippingAddress(getShippingAddress(request));
    }

    private Address getBillingAddress(HttpServletRequest request) {
        String billingCountry = request.getParameter("billingCountry");
        String billingCity = request.getParameter("billingCity");
        String billingZipCode = request.getParameter("billingZip");
        String billingAddress = request.getParameter("billingAddress");
        return new Address(billingCountry, billingCity, billingZipCode, billingAddress);
    }

    private Address getShippingAddress(HttpServletRequest request) {
        String shippingCountry = request.getParameter("shippingCountry");
        String shippingCity = request.getParameter("shippingCity");
        String shippingZip = request.getParameter("shippingZip");
        String shippingAddress = request.getParameter("shippingAddress");
        return new Address(shippingCountry, shippingCity, shippingZip, shippingAddress);
    }
}
