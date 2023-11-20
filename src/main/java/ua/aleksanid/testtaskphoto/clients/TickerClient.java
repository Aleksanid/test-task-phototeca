package ua.aleksanid.testtaskphoto.clients;

import java.util.List;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import ua.aleksanid.testtaskphoto.models.CryptoInfoDTO;

@Component
public class TickerClient {
    private static final String TICKER_URL = "https://api.mexc.com/api/v3/ticker/price";
    private static final ParameterizedTypeReference CRYPTO_INFO_LIST_TYPE =
            new ParameterizedTypeReference<List<CryptoInfoDTO>>() {
            };

    private final RestTemplate restTemplate;


    public TickerClient() {
        this.restTemplate = new RestTemplateBuilder().build();
    }


    public List<CryptoInfoDTO> getCurrentCryptoInfo() {
        ResponseEntity<List<CryptoInfoDTO>>
                response = restTemplate.exchange(TICKER_URL, HttpMethod.GET, null, CRYPTO_INFO_LIST_TYPE);
        return response.getBody();
    }
}
