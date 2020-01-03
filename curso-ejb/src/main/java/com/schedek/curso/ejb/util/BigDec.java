package com.schedek.curso.ejb.util;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

public class BigDec {

    public static final BigDecimal _0 = BigDecimal.ZERO;
    public static final BigDecimal _0S2 = new BigDecimal(BigInteger.ZERO, 2);
    public static final BigDecimal _1 = BigDecimal.ONE;
    public static final BigDecimal _100 = new BigDecimal(100);
    public static final DecimalFormat format =  new DecimalFormat("#,###.00");;

    public static final BigDecimal _0(BigDecimal b) {
        return coalesce(b, _0);
    }

    public static final BigDecimal _1(BigDecimal b) {
        return coalesce(b, _1);
    }

    public static final BigDecimal coalesce(BigDecimal b, BigDecimal c) {
        return b != null ? b : c;
    }

    public static String format(BigDecimal n) {
        return format.format(n);
    }
   
}
