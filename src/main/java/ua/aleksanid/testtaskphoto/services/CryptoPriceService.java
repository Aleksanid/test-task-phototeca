package ua.aleksanid.testtaskphoto.services;

import java.sql.Timestamp;
import java.util.List;
import org.springframework.stereotype.Service;
import ua.aleksanid.testtaskphoto.models.PriceDifferenceDto;
import ua.aleksanid.testtaskphoto.models.entities.CryptoPrice;
import ua.aleksanid.testtaskphoto.repositories.CryptoPriceRepository;

@Service
public class CryptoPriceService {
    private final CryptoPriceRepository cryptoPriceRepository;

    public CryptoPriceService(CryptoPriceRepository cryptoPriceRepository) {
        this.cryptoPriceRepository = cryptoPriceRepository;
    }

    public void savePrices(List<CryptoPrice> cryptoPrices) {
        cryptoPriceRepository.saveAll(cryptoPrices);
    }

    public List<PriceDifferenceDto> getPriceDifferenceBetweenTime(Timestamp start, Timestamp end) {
        return cryptoPriceRepository.calculatePriceDifferencePercentage(start, end);
    }
}
