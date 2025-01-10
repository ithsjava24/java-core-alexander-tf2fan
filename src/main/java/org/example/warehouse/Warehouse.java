package org.example.warehouse;


import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Warehouse {

    private static Warehouse instance;
    private String name;
    private List<ProductRecord> productRecordList;

    public String getName() {
        return name;
    }

    private Warehouse(String name) {
        this.name = name;
        this.productRecordList = new ArrayList<>();
    }


    public static Warehouse getInstance(String name) {
        if (instance == null)
            instance = new Warehouse(name);
        else if (!instance.getName().equals(name))
            throw new IllegalArgumentException("Error");
        return instance;
    }

    public static Warehouse getInstance() {
        if (instance == null) {
            throw new IllegalArgumentException("En warehouse har inte skapats än");
        }
        return instance;
    }

    public ProductRecord addProduct(UUID uuid, String productName, Category category, BigDecimal price) {
        ProductRecord product = new ProductRecord(uuid,productName,category,price);
        this.productRecordList.add(product);
        return product;
    }

    public List<ProductRecord> getProducts() {
        return productRecordList;
    }

    public boolean isEmpty() {
        return productRecordList.isEmpty();
    }

    public List<ProductRecord>  getProductById(UUID productId) {
        return productRecordList;
    }

    public void updateProductPrice(UUID productId, BigDecimal price) {
        ProductRecord product = productRecordList.getFirst();
        ProductRecord updateProductsPrice = new ProductRecord(productId, product.productName(), product.category(), price);
        int theSoleProduct = productRecordList.indexOf(product);
        productRecordList.set(theSoleProduct, updateProductsPrice);
    }

    public List<ProductRecord> getChangedProducts(){
        return getProducts();
    }

    public static void main(String[] args) {

    }

}

