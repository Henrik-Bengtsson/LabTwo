package se.iths.javatwentytwo.labTwo;

import java.math.BigDecimal;

public record ShoppingCart(Product shoppingProduct, int amountToBuy) {

    public static BigDecimal applyDiscount(Discount discount, BigDecimal total) {
        return discount.applyDiscount(total);
    }
}
