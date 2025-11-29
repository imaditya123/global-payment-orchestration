package com.global.shared.enums;

public enum Currency {
    USD(2),
    EUR(2),
    GBP(2),
    INR(2),
    JPY(0);

    private final int minorUnits;

    Currency(int minorUnits) {
        this.minorUnits = minorUnits;
    }

    public int getMinorUnits() {
        return minorUnits;
    }
}
