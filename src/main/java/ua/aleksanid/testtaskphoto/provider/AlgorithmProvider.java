package ua.aleksanid.testtaskphoto.provider;

import com.pengrad.telegrambot.model.Update;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.jetbrains.annotations.NotNull;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ua.aleksanid.testtaskphoto.clients.TickerClient;
import ua.aleksanid.testtaskphoto.configurations.AppConfiguration;
import ua.aleksanid.testtaskphoto.models.CryptoInfoDTO;
import ua.aleksanid.testtaskphoto.models.PriceDifferenceDto;
import ua.aleksanid.testtaskphoto.models.entities.CryptoPrice;
import ua.aleksanid.testtaskphoto.models.entities.User;
import ua.aleksanid.testtaskphoto.services.CryptoPriceService;
import ua.aleksanid.testtaskphoto.services.TelegramBotService;
import ua.aleksanid.testtaskphoto.services.UserService;

/**
 * Top class for everything
 */
@Component
public class AlgorithmProvider {
    private final AppConfiguration appConfiguration;

    private final TelegramBotService telegramBotService;
    private final UserService userService;
    private final CryptoPriceService cryptoPriceService;


    private final TickerClient tickerClient;

    public AlgorithmProvider(AppConfiguration appConfiguration, TelegramBotService telegramBotService,
                             UserService userService,
                             CryptoPriceService cryptoPriceService, TickerClient tickerClient) {
        this.appConfiguration = appConfiguration;
        this.telegramBotService = telegramBotService;
        this.userService = userService;
        this.cryptoPriceService = cryptoPriceService;
        this.tickerClient = tickerClient;

        telegramBotService.addNewListener(updates -> updates.forEach(update -> {
            if (update.message().text().startsWith("/start")) {
                initiateStartForUser(update);
            } else if (update.message().text().startsWith("/reset")) {
                initiateResetForUser(update);
            }
        }));
    }

    private void initiateResetForUser(Update update) {
        int rowsUpdated = userService.updateUserStartTime(String.valueOf(update.message().chat().id()),
                Timestamp.from(Instant.now())); // TODO: For now leaving .now then fetch Telegram update date
        if (rowsUpdated >= 1) { // If no rows updated - user is not subscribed
            telegramBotService.sendMessage(update.message().chat().id(), "Your start time has been reset.");
        }
    }

    private void initiateStartForUser(Update it) {
        long currentUsers = userService.getUserCount();

        if(userService.isUserExistsByTelegramId(String.valueOf(it.message().chat().id()))){
            telegramBotService.sendMessage(it.message().chat().id(), "You are already subscribed, use /reset to reset your start time.");
            return;
        }

        if (currentUsers >= appConfiguration.getMaxUsersToSupport()) {
            telegramBotService.sendMessage(it.message().chat().id(), "Max user count is reached. Request rejected.");
            return;
        }

        Instant now = Instant.now();
        userService.addNewUser(String.valueOf(it.message().chat().id()),
                now); // TODO: For now leaving .now then fetch Telegram update date
        recordCurrentPrices(now);
        telegramBotService.sendMessage(it.message().chat().id(), "You have started");
    }

    private void recordCurrentPrices(Instant instant) {
        List<CryptoInfoDTO> cryptoInfoDTOList = tickerClient.getCurrentCryptoInfo();

        List<CryptoPrice> cryptoPrices = cryptoInfoDTOList
                .stream()
                .map(it -> new CryptoPrice(it.getSymbol(), it.getPrice(), Timestamp.from(instant)))
                .toList();

        cryptoPriceService.savePrices(cryptoPrices);
    }

    @Scheduled(fixedDelayString = "${app.refreshIntervalInMilliseconds}")
    private void analyzePriceChangesInDataBase() { // DB Approach
        Instant now = Instant.now();
        recordCurrentPrices(now); // This data is useless after calculation is done. Can be cleaned up.

        List<User> users = userService.getAllUsers();

        users.forEach(user -> {
            List<PriceDifferenceDto> differenceOverThresholds =
                    calculatePriceDifferences(user, now);

            if (!differenceOverThresholds.isEmpty()) {
                // Avoid sending long messages, send multiple instead
                // Not going to truncate floating point in case of setting threshold to like 0.0000000001
                notifyUserForChange(user, differenceOverThresholds);
            }
        });
    }

    @NotNull
    private List<PriceDifferenceDto> calculatePriceDifferences(User user, Instant now) {
        List<PriceDifferenceDto> priceDifferenceDtos =
                cryptoPriceService.getPriceDifferenceBetweenTime(user.getStartTime(), Timestamp.from(
                        now));

        return priceDifferenceDtos
                .stream()
                .filter(priceDifferenceDto -> appConfiguration.getPercentToNotify().compareTo(
                        priceDifferenceDto.getPriceDifferencePercentage() == null ? BigDecimal.ZERO :
                                priceDifferenceDto.getPriceDifferencePercentage().abs()) <= 0)
                .toList();
    }

    private void notifyUserForChange(User user, List<PriceDifferenceDto> differenceOverThresholds) {
        splitList(differenceOverThresholds, 20)
                .stream().map(list -> list.stream()
                        .map(priceDifferenceDto -> priceDifferenceDto.getSymbol() + " : " +
                                                   priceDifferenceDto.getPriceDifferencePercentage() + "%")
                        .collect(Collectors.joining("\n", "Price changes: \n", "")))
                .forEach(message -> telegramBotService.sendMessage(Long.valueOf(user.getTelegramId()), message));
    }

    private <T> List<List<T>> splitList(List<T> originalList, int partitionSize) {
        List<List<T>> sublists = new ArrayList<>();

        // Iterate through the original list and create sublists
        for (int i = 0; i < originalList.size(); i += partitionSize) {
            int endIndex = Math.min(i + partitionSize, originalList.size());
            List<T> sublist = originalList.subList(i, endIndex);
            sublists.add(sublist);
        }
        return sublists;
    }
}
