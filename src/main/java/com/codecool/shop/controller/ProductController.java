package com.codecool.shop.controller;

import com.codecool.shop.dao.ProductCategoryDao;
import com.codecool.shop.dao.ProductDao;
import com.codecool.shop.dao.SupplierDao;
import com.codecool.shop.dao.implementation.ProductCategoryDaoMem;
import com.codecool.shop.dao.implementation.ProductDaoMem;
import com.codecool.shop.config.TemplateEngineUtil;
import com.codecool.shop.dao.implementation.SupplierDaoMem;
import com.codecool.shop.model.Product;
import com.codecool.shop.model.ProductCategory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.WebContext;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@WebServlet(urlPatterns = {"/"})
public class ProductController extends HttpServlet {

    List<Product> products;
    private Gson gson = new Gson();
    private Type collectionType = new TypeToken<List<Product>>() {}.getType();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();

        TemplateEngine engine = TemplateEngineUtil.getTemplateEngine(req.getServletContext());
        WebContext context = new WebContext(req, resp, req.getServletContext());


//        context.setVariable("category", productCategoryDataStore.find(1));;
        context.setVariable("suppliers", supplierDataStore.getAll());


        if (req.getParameter("supplier") != null) {
            products = productDataStore.getBySupplierName(req.getParameter("supplier"));
            String json = gson.toJson(products, collectionType);
            resp.setContentType("application/json");
            resp.getWriter().write(json);
        }
        else {
            products = productDataStore.getAll();
            context.setVariable("products", products);

            // // Alternative setting of the template context
            // Map<String, Object> params = new HashMap<>();
            // params.put("category", productCategoryDataStore.find(1));
            // params.put("products", productDataStore.getBy(productCategoryDataStore.find(1)));
            // context.setVariables(params);
            engine.process("product/index.html", context, resp.getWriter());
        }

    }

}
