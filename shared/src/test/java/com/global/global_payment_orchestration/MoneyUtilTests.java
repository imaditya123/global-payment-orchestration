package com.global.shared;

import com.global.shared.enums.Currency;
import com.global.shared.util.MoneyUtil;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class MoneyUtilTests {

    @Test
    void testRoundUsd() {
        BigDecimal input = new BigDecimal("10.456");
        BigDecimal rounded = MoneyUtil.round(input, Currency.USD);
        assertEquals(new BigDecimal("10.46"), rounded);
    }

    @Test
    void testToMinorUnits() {
        BigDecimal amount = new BigDecimal("1.23");
        long result = MoneyUtil.toMinorUnits(amount, Currency.USD);
        assertEquals(123L, result);
    }

    @Test
    void testFromMinorUnits() {
        long minor = 123L;
        BigDecimal amount = MoneyUtil.fromMinorUnits(minor, Currency.USD);
        assertEquals(new BigDecimal("1.23"), amount);
    }
}
