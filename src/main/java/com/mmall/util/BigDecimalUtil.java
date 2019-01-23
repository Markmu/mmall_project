package com.mmall.util;

import java.math.BigDecimal;

public class BigDecimalUtil {


    private BigDecimalUtil() {}


    public static BigDecimal add(Double v1, Double v2) {
        BigDecimal value1 = new BigDecimal(v1.toString());
        BigDecimal value2 = new BigDecimal(v2.toString());
        return value1.add(value2);
    }

    public static BigDecimal sub(Double v1, Double v2) {
        BigDecimal value1 = new BigDecimal(v1.toString());
        BigDecimal value2 = new BigDecimal(v2.toString());
        return value1.subtract(value2);
    }

    public static BigDecimal mul(Double v1, Double v2) {
        BigDecimal value1 = new BigDecimal(v1.toString());
        BigDecimal value2 = new BigDecimal(v2.toString());
        return value1.multiply(value2);
    }

    public static BigDecimal div(Double v1, Double v2) {
        BigDecimal value1 = new BigDecimal(v1.toString());
        BigDecimal value2 = new BigDecimal(v2.toString());
        return value1.divide(value2, BigDecimal.ROUND_HALF_UP);
    }

}
