package se.iths.javatwentytwo.labTwo;

import java.math.BigDecimal;

public interface Discount {
    BigDecimal applyDiscount(BigDecimal discount);

    static Discount overThreeHundred(){
        return amount -> amount.multiply(BigDecimal.valueOf(0.98));
    }

    static Discount overFiveHundred(){
        return amount -> amount.multiply(BigDecimal.valueOf(0.95));
    }
}
