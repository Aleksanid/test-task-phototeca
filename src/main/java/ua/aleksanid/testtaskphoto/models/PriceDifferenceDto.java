package ua.aleksanid.testtaskphoto.models;

import java.math.BigDecimal;

/**
 * Projection for DB calculations
 */
public interface PriceDifferenceDto {
    String getSymbol();

    BigDecimal getPriceDifferencePercentage();

}
