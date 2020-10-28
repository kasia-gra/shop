package com.codecool.shop.dao.dao;

import com.codecool.shop.model.product.Supplier;
import com.codecool.shop.model.product.Product;
import com.codecool.shop.model.product.ProductCategory;

import java.util.List;

public interface ProductDao {

    void add(Product product);
    Product find(int id);
    void remove(int id);

    List<Product> getAll();
    List<Product> getBy(Supplier supplier);
    List<Product> getBy(ProductCategory productCategory);

}
