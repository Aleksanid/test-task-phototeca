package ua.aleksanid.testtaskphoto.services;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import java.util.ArrayList;
import java.util.List;
import org.springframework.stereotype.Service;
import ua.aleksanid.testtaskphoto.configurations.TelegramConfig;
import ua.aleksanid.testtaskphoto.listeners.EventListener;

@Service
public class TelegramBotService {
    private final TelegramBot telegramBot;

    private final TelegramConfig telegramConfig;

    private final List<EventListener<List<Update>>> updateListeners;


    public TelegramBotService(TelegramConfig telegramConfig) {
        this.telegramConfig = telegramConfig;
        telegramBot = new TelegramBot(telegramConfig.getToken());
        this.updateListeners = new ArrayList<>();

        telegramBot.setUpdatesListener(updates -> {
            updateListeners.forEach(it -> it.onEvent(updates));
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        }, e -> {
            if (e.response() != null) {
                e.response().errorCode();
                e.response().description();
            } else {
                e.printStackTrace();
            }
        });
    }

    public void addNewListener(EventListener<List<Update>> eventListener) {
        updateListeners.add(eventListener);
    }

    public void sendMessage(Long chatId, String text) {
        SendResponse result = telegramBot.execute(new SendMessage(chatId, text));
        if (!result.isOk()) {
            throw new IllegalArgumentException();  // Add logging
        }
    }
}
