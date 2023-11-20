package ua.aleksanid.testtaskphoto.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.math.BigDecimal;

/**
 * API response model
 */
public class CryptoInfoDTO {
    @JsonProperty("symbol")
    private String symbol;

    @JsonProperty("price")
    @JsonFormat(shape = JsonFormat.Shape.STRING)
    private BigDecimal price;

    public CryptoInfoDTO() {
    }

    public CryptoInfoDTO(String symbol, BigDecimal price) {
        this.symbol = symbol;
        this.price = price;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}