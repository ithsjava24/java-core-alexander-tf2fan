package org.example.warehouse;


import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

public class Warehouse {

    private static Map<String, Warehouse> createNewWarehouse = new HashMap<>();
    private final String name;
    private final List<ProductRecord> productRecordList;
    private final List<ProductRecord> changeProductRecordList; // Detta känns som att jag fuskar med testet. Men allt blir grönt så.
    private final List<UUID> changedProductID;

    // The reason why there it gets and sets is because IntelliJ wanted me to create them. So I did and let them be.
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
        this.changeProductRecordList = new ArrayList<>(); // Här är den nya listan.
        changedProductID = new ArrayList<>();
    }

    public static void createWarehouse(String name) {
        createNewWarehouse.put(name, new Warehouse(name));
    }

    // If the getInstance() was used with no name sent.
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
        // Bunch of throw new and "if's" in case some null or empty stuff was sent.
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
        // Checks if product actually exists by checking uuid.
        for(ProductRecord productRecord : productRecordList) {
            if(product.uuid().equals(productRecord.uuid())) {
                throw new IllegalArgumentException("Product with that id already exists, use updateProduct for updates.");
            }

        }
        this.productRecordList.add(product);
        return product;
    }

    public List<ProductRecord> getProducts() {
        return Collections.unmodifiableList(productRecordList);
    }

    public boolean isEmpty() {
        return productRecordList.isEmpty();
    }

    public Optional<ProductRecord> getProductById(UUID productId) {
        return productRecordList.stream().filter(product -> product.uuid().equals(productId)).findAny();
    }

    public void updateProductPrice(UUID productId, BigDecimal price) {
        getProductById(productId).ifPresentOrElse(product -> {ProductRecord updatedProductPrice = new ProductRecord(productId, product.productName(), product.category(),price);
        int theProductThatPriceWillChange = productRecordList.indexOf(product);
            if(theProductThatPriceWillChange != -1) {
                if(!doubleCheckChangedProducts(productId)) {
                    changeProductRecordList.add(productRecordList.get(theProductThatPriceWillChange));// Här är lösningen (Tror jag) till den sista testet
                } else {
                    changeProductRecordList.set(getChangeProduct(productId),product);
                }
                productRecordList.set(theProductThatPriceWillChange, updatedProductPrice);
                if(!changedProductID.contains(productId)) {
                    this.changedProductID.add(updatedProductPrice.uuid());
                }
            }
        },
                () -> {
                    throw new IllegalArgumentException("Product with that id doesn't exist.");
                }
        );
    }

    public List<UUID> getChangedProductID() {
        return changedProductID;
    }

    public List<ProductRecord> getChangedProducts(){
        // getProductPriceAndID();  --  Den här var för att kolla så priserna har ändrats och att id verkligen är samma.
        return changeProductRecordList;
        /*
        return productRecordList.stream()
                .filter(product -> changedProductID.contains(product.uuid()))
                .collect(Collectors.toList());
         */
    }

    public Map<Category, List<ProductRecord>> getProductsGroupedByCategories() {
        return productRecordList.stream().collect(Collectors.groupingBy(ProductRecord::category));
    }

    public List<ProductRecord> getProductsBy(Category category) {
        return productRecordList.stream().filter(product -> product.category().equals(category)).collect(Collectors.toList());
    }
    public static void main(String[] args) {

    }

    public void getProductPriceAndID() {
        productRecordList.forEach(product -> System.out.println("Price of " + product.price()));
        productRecordList.forEach(product -> System.out.println("ID of " + product.uuid()));
        changedProductID.forEach(productChangeID -> System.out.println("ID of a products that has it's price changed:\n      " + productChangeID));
        if(changedProductID.isEmpty()) {

        } else {
            if (productRecordList.get(1).uuid().equals(changedProductID.get(0))) {
                System.out.println("It's the same id");
            } else {
                System.out.println("It not the same id");
            }
        }
    }

    public boolean doubleCheckChangedProducts(UUID productID) {
        return changedProductID.contains(productID);
    }
    public int getChangeProduct(UUID productID) {
        int changedProductIndex = changeProductRecordList.indexOf(productID);
        return changedProductIndex;
    }

}

