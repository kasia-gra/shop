package com.codecool.shop.model.product;

import java.util.Currency;

public class Product extends BaseModel {

    private float defaultPrice;

    private final String pictureName;
    private ProductCategory productCategory;
    private Supplier supplier;
    public Product(String name, float defaultPrice, String pictureName, String description, ProductCategory productCategory, Supplier supplier) {
        super(name, description);
        this.setPrice(defaultPrice);
        this.pictureName = pictureName;
        this.setSupplier(supplier);
        this.setProductCategory(productCategory);
    }

    public float getDefaultPrice() {
        return defaultPrice;
    }

    public void setDefaultPrice(float defaultPrice) {
        this.defaultPrice = defaultPrice;
    }

    public String getPictureName() {
        return pictureName;
    }

    public String getPrice() {
        return String.valueOf(this.defaultPrice) + " " + "USD";
    }

    public void setPrice(float price) {
        this.defaultPrice = price;
    }

    public ProductCategory getProductCategory() {
        return productCategory;
    }

    public void setProductCategory(ProductCategory productCategory) {
        this.productCategory = productCategory;
        this.productCategory.addProduct(this);
    }

    public Supplier getSupplier() {
        return supplier;
    }

    public void setSupplier(Supplier supplier) {
        this.supplier = supplier;
        this.supplier.addProduct(this);
    }

    @Override
    public String toString() {
        return String.format("id: %1$d, " +
                        "name: %2$s, " +
                        "defaultPrice: %3$f, " +
                        "productCategory: %4$s, " +
                        "supplier: %5$s",
                this.id,
                this.name,
                this.defaultPrice,
                this.productCategory.getName(),
                this.supplier.getName());
    }
}
