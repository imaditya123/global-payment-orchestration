package com.global.orchestrator.client;


import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.beans.factory.annotation.Value;


import java.math.BigDecimal;
import java.net.URI;


@Component
public class FxServiceClient {


    private final RestTemplate rt = new RestTemplate();
    private final String baseUrl;


    public FxServiceClient(@Value("${services.fx.base-url}") String baseUrl) {
        this.baseUrl = baseUrl;
    }


    public BigDecimal convert(String from, String to, BigDecimal amount) {
        URI uri = UriComponentsBuilder.fromHttpUrl(baseUrl).path("/fx/convert")
        .queryParam("from", from)
        .queryParam("to", to)
        .queryParam("amount", amount)
        .build().toUri();
        // Expecting simple response { "convertedAmount": 123.45 }
        try {
            var resp = rt.getForObject(uri, java.util.Map.class);
            Object val = resp.get("convertedAmount");
            return new BigDecimal(val.toString());
        } catch (Exception ex) {
            throw new RuntimeException("FX conversion failed", ex);
        }
    }
}