package ua.aleksanid.testtaskphoto.repositories;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import ua.aleksanid.testtaskphoto.models.PriceDifferenceDto;
import ua.aleksanid.testtaskphoto.models.entities.CryptoPrice;

public interface CryptoPriceRepository extends JpaRepository<CryptoPrice, Long> {

    @Query(value = """
            SELECT startState.symbol,
                CASE
                    WHEN startState.price <> 0
                    THEN (currentState.price - startState.price) / startState.price * 100
                    ELSE NULL
                END AS priceDifferencePercentage
            FROM crypto_prices AS startState JOIN crypto_prices AS currentState ON startState.symbol = currentState.symbol
            WHERE startState.timestamp = :startTime AND currentState.timestamp = :endTime ;""", nativeQuery = true)
    /*
    Initially had simple MIN MAX without joins, which can be used if we don't want sign of the change
     */
    List<PriceDifferenceDto> calculatePriceDifferencePercentage(@Param("startTime") Timestamp timeA,
                                                                @Param("endTime") Timestamp timeB);
}