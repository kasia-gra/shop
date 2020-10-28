package com.codecool.shop.controller;

import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.OrderDao;
import com.codecool.shop.dao.UserDao;
import com.codecool.shop.dao.implementation.OrderDaoMem;
import com.codecool.shop.dao.implementation.UserDaoMem;
import com.codecool.shop.model.order.Order;
import com.codecool.shop.model.product.Product;
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
import java.util.HashMap;
import java.util.Map;

@WebServlet(urlPatterns = {"/checkout"}, loadOnStartup = 3)
public class CheckoutController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());
        Map<Product, Integer> orderedProducts = new HashMap<>();
        float totalPrice = 0;
        String cartValue = "0";
        int itemsNumber = 0;
        String currency = "";

        if (CartController.getCookieValueBy("userId", req) != null) {
            OrderDao orderDataStore = OrderDaoMem.getInstance();
            Order order = orderDataStore.getActual(Integer.parseInt(CartController.getCookieValueBy("userId", req)));
            orderedProducts = order.getCart().getProducts();
            totalPrice = order.getCart().getTotalPrice();
            HashMap.Entry<Product, Integer> entry = orderedProducts.entrySet().iterator().next();
            currency = entry.getKey().getDefaultCurrency().getCurrencyCode();
            cartValue = String.format("%.2f", totalPrice);
            itemsNumber = order.getCart().getSize();
        }

        context.setVariable("currency", currency);
        context.setVariable("itemsNumber", itemsNumber);
        context.setVariable("orderedProducts", orderedProducts);
        context.setVariable("cartValue", cartValue);
        engine.process("product/checkout.html", context, resp.getWriter());
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String userId = CartController.getCookieValueBy("userId", req);
        UserDao userDataStorage = UserDaoMem.getInstance();
        User user = userDataStorage.find(Integer.parseInt(userId));
        setUserParameters(user, req);
        resp.sendRedirect("/");
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
