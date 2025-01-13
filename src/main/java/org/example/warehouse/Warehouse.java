package org.example.warehouse;


import java.lang.reflect.UndeclaredThrowableException;
import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {

    private static Map<String, Warehouse> createNewWarehouse = new HashMap<>();
    private final String name;
    private final List<ProductRecord> productRecordList;

    public static Map<String, Warehouse> getCreateNewWarehouse() {
        return createNewWarehouse;
    }

    public static void setNewWarehouse(Map<String, Warehouse> createNewWarehouse) {
        Warehouse.createNewWarehouse = createNewWarehouse;
    }

    public String getName() {
        return name;
    }

    private Warehouse(String name) {
        this.name = name;
        this.productRecordList = new ArrayList<>();
    }

    public static void createWarehouse(String name) {
        createNewWarehouse.put(name, new Warehouse(name));
    }

    public static Warehouse getInstance() {
        createWarehouse("Default warehouse");
        return createNewWarehouse.get("Default warehouse");
    }
    public static Warehouse getInstance(String name) {
        if(!createNewWarehouse.containsKey(name)) {
            createWarehouse(name);
        }
        return createNewWarehouse.getOrDefault(name, new Warehouse(name));
    }

    public ProductRecord addProduct(UUID uuid, String productName, Category category, BigDecimal price) {
        if (productName == null) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        } else if(productName.isEmpty()) {
            throw new IllegalArgumentException("Product name can't be null or empty.");
        } else if(category == null) {
            throw new IllegalArgumentException("Category can't be null.");
        }
        if(uuid == null) {
            uuid = UUID.randomUUID();
        }
        if(price == null) {
            price = BigDecimal.ZERO;
        }
        ProductRecord product = new ProductRecord(uuid,productName,category,price);
        for(ProductRecord productRecord : productRecordList) {
            if(product.uuid().equals(productRecord.uuid())) {
                throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
            }

        }
        this.productRecordList.add(product);
        return product;
    }

    public List<ProductRecord> getProducts() {
        return productRecordList;
    }

    public boolean isEmpty() {
        return productRecordList.isEmpty();
    }

    public Optional<ProductRecord> getProductById(UUID productId) {
        return productRecordList.stream().filter(product -> product.uuid().equals(productId)).findFirst();
    }

    public void updateProductPrice(UUID productId, BigDecimal price) {
        getProductById(productId).ifPresentOrElse(product -> {ProductRecord updatedProductPrice = new ProductRecord(productId, product.productName(), product.category(),price);
        int theProductThatPriceWillChange = productRecordList.indexOf(product);
            if(theProductThatPriceWillChange != -1) {
                productRecordList.set(theProductThatPriceWillChange, updatedProductPrice);
            }
        },
                () -> {
                    throw new IllegalArgumentException("Product with that id doesn't exist.");
                }
        );
    }

    public List<ProductRecord> getChangedProducts(){
        return getProducts();
    }

    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        return productRecordList.stream().collect(Collectors.groupingBy(ProductRecord::category));
    }

    public List<ProductRecord> getProductsBy(Object theThingItLooksFor) {
        return switch (theThingItLooksFor) {
            case Category searchCategory ->
                    productRecordList.stream().filter(product -> product.category().equals(searchCategory)).collect(Collectors.toList());
            case String searchName ->
                    productRecordList.stream().filter(product -> product.productName().equals(searchName)).collect(Collectors.toList());
            case UUID searchUUID ->
                    productRecordList.stream().filter(product -> product.uuid().equals(searchUUID)).collect(Collectors.toList());
            case BigDecimal searchPrice ->
                    productRecordList.stream().filter(product -> product.price().equals(searchPrice)).collect(Collectors.toList());
            case null, default -> throw new IllegalArgumentException("This thing in the record does not exists");
        };
    }
    public static void main(String[] args) {

    }

}

