package com.global.shared;

import com.global.shared.util.CorrelationIdUtil;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class CorrelationIdUtilTests {

    @Test
    void testCreateGeneratesNonEmpty() {
        String cid = CorrelationIdUtil.create();
        assertNotNull(cid);
        assertFalse(cid.isEmpty());
    }

    @Test
    void testPutAndReadHeaders() {
        Map<String, Object> headers = new HashMap<>();
        String cid = CorrelationIdUtil.create();
        CorrelationIdUtil.putToHeaders(headers, cid);

        assertEquals(cid, headers.get("X-Correlation-Id"));

        var read = CorrelationIdUtil.fromHeaders(headers);
        assertTrue(read.isPresent());
        assertEquals(cid, read.get());
    }
}
