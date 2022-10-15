package se.iths.javatwentytwo.labTwo;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
    private final int productId;
    private String name;
    private int productBalance;
    private BigDecimal price;
    private String maker;
    private Category category;

    public Product(Category category, String name, int productBalance, BigDecimal price, String maker, int productId) {
        this.productId = productId;
        this.name = name;
        this.productBalance = productBalance;
        this.price = price;
        this.maker = maker;
        this.category = category;
    }

    public Category getCategory() {
        return category;
    }

    public String getName() {
        return name;
    }

    public int getProductBalance() {
        return productBalance;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getMaker() {
        return maker;
    }

    public int getProductId() {
        return productId;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setProductBalance(int productBalance) {
        this.productBalance = productBalance;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public void setMaker(String maker) {
        this.maker = maker;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Product product)) return false;
        return productId == product.productId && productBalance == product.productBalance && Objects.equals(name, product.name) && Objects.equals(price, product.price) && Objects.equals(maker, product.maker) && category == product.category;
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, name, productBalance, price, maker, category);
    }

    @Override
    public String toString() {
        return "Product{" +
                "Id = " + productId +
                ", Namn = " + name +
                ", Saldo = " + productBalance +
                ", Pris = " + price +
                ", Tillverkare = " + maker +
                ", Kategori = " + category +
                '}';
    }
}
