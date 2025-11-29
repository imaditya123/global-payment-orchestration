package com.global.shared.util;

import com.global.shared.enums.Currency;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public final class MoneyUtil {

    private MoneyUtil() { }

    public static BigDecimal round(BigDecimal amount, Currency currency) {
        Objects.requireNonNull(amount);
        Objects.requireNonNull(currency);
        return amount.setScale(currency.getMinorUnits(), RoundingMode.HALF_EVEN);
    }

    public static long toMinorUnits(BigDecimal amount, Currency currency) {
        BigDecimal rounded = round(amount, currency)
                .movePointRight(currency.getMinorUnits());
        return rounded.longValueExact();
    }

    public static BigDecimal fromMinorUnits(long minorUnits, Currency currency) {
        return BigDecimal.valueOf(minorUnits)
                .movePointLeft(currency.getMinorUnits());
    }
}
