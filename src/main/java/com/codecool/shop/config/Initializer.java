package com.codecool.shop.config;

import com.codecool.shop.dao.dao.ProductCategoryDao;
import com.codecool.shop.dao.dao.ProductDao;
import com.codecool.shop.dao.dao.SupplierDao;
import com.codecool.shop.dao.jdbc.ProductCategoryDaoMem;
import com.codecool.shop.dao.jdbc.ProductDaoMem;
import com.codecool.shop.dao.jdbc.SupplierDaoMem;
import com.codecool.shop.model.product.Product;
import com.codecool.shop.model.product.ProductCategory;
import com.codecool.shop.model.product.Supplier;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class Initializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ProductDao productDataStore = ProductDaoMem.getInstance();
        ProductCategoryDao productCategoryDataStore = ProductCategoryDaoMem.getInstance();
        SupplierDao supplierDataStore = SupplierDaoMem.getInstance();

        //setting up a new supplier
        Supplier amazon = new Supplier("Amazon", "Digital content and services");
        supplierDataStore.add(amazon);
        Supplier lenovo = new Supplier("Lenovo", "Computers");
        supplierDataStore.add(lenovo);
        Supplier nokia = new Supplier("Nokia", "Smartphones and stuff");
        supplierDataStore.add(nokia);
        Supplier samsung = new Supplier("Samsung", "Just stuff");
        supplierDataStore.add(samsung);
        Supplier legoGroup = new Supplier("Lego", "Bricks");
        supplierDataStore.add(legoGroup);



        //setting up a new product category
        ProductCategory tablet = new ProductCategory("Tablets", "Hardware", "A tablet computer, commonly shortened to tablet, is a thin, flat mobile computer with a touchscreen display.");
        productCategoryDataStore.add(tablet);
        ProductCategory smartphone = new ProductCategory("Smartphones", "Hardware", "Smartphone, Everyone knows what is it.");
        productCategoryDataStore.add(smartphone);
        ProductCategory legoBricks = new ProductCategory("Lego Bricks", "Toys", "Plastic construction toys that are manufactured by The Lego Group, a privately held company based in Billund, Denmark.");
        productCategoryDataStore.add(legoBricks);


        //setting up products and printing it
        productDataStore.add(new Product("Amazon Fire", 49.9f, "USD", "Fantastic price. Large content ecosystem. Good parental controls. Helpful technical support.", tablet, amazon));
        productDataStore.add(new Product("Lenovo IdeaPad Miix 700", 479, "USD", "Keyboard cover is included. Fanless Core m5 processor. Full-size USB ports. Adjustable kickstand.", tablet, lenovo));
        productDataStore.add(new Product("Amazon Fire HD 8", 89, "USD", "Amazon's latest Fire HD 8 tablet is a great value for media consumption.", tablet, amazon));
        productDataStore.add(new Product("Hello Kitty Smartphone",99, "USD", "Smartphone for girls", smartphone, samsung));
        productDataStore.add(new Product("Pink flip smartphone",79, "USD", "Smartphone for little girls", smartphone, samsung));
        productDataStore.add(new Product("Old school phone",149, "USD", "Smartphone for real man", smartphone, nokia));
        productDataStore.add(new Product("Lego set ",59, "USD", "Lego set for kids", legoBricks, legoGroup));
        productDataStore.add(new Product("Lego set Fire Station",209, "USD", "Lego set with fire station for kids", legoBricks, legoGroup));
        productDataStore.add(new Product("Lego set Star Wars ",119, "USD", "Lego set with Star Wars theme for kids", legoBricks, legoGroup));
    }
}
