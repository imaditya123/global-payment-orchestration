package com.global.settlement.util;

import com.global.settlement.entity.SettlementItemEntity;

import java.util.List;
import java.util.StringJoiner;

/**
 * Simple CSV serializer. Replace with ISO20022/etc per connector.
 */
public final class FileFormatUtil {

    private FileFormatUtil() {}

    public static String toCsv(List<SettlementItemEntity> items) {
        StringJoiner sj = new StringJoiner("\n");
        sj.add("payment_id,vendor_id,amount,currency");
        for (SettlementItemEntity it : items) {
            sj.add(String.format("%s,%s,%s,%s",
                    it.getPaymentId(),
                    maskVendor(it.getVendorId()),
                    it.getAmount(),
                    it.getCurrency()));
        }
        return sj.toString();
    }

    private static String maskVendor(String vendor) {
        if (vendor == null) return "";
        if (vendor.length() <= 4) return "****";
        return vendor.substring(0, 2) + "****" + vendor.substring(vendor.length()-2);
    }
}
