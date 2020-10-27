package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Currency;
import java.util.HashMap;
import java.util.Map;


@WebServlet(urlPatterns = {"/cards"})
public class CardController extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();

        HashMap<Product, Integer> orderedProducts = new HashMap<>();
        float totalPrice = 0;
        for (Product product : productDataStore.getAll()) {
            int qty = orderedProducts.containsKey(product) ? orderedProducts.get(product) : 0;
            orderedProducts.put(product, qty + 1);
            totalPrice = totalPrice + (product.getDefaultPrice() * (qty+1));
        }
        HashMap.Entry<Product, Integer> entry = orderedProducts.entrySet().iterator().next();
        String currency = entry.getKey().getDefaultCurrency().getCurrencyCode();
        String cartValue = String.format("%.2f %s", totalPrice, currency);


        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());

        context.setVariable("ordered_products", orderedProducts);
        context.setVariable("cartValue", cartValue);
        engine.process("product/card.html", context, resp.getWriter());
    }

}