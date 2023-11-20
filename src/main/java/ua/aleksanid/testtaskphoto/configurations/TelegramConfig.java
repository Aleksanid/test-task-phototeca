package ua.aleksanid.testtaskphoto.configurations;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "telegram")
public class TelegramConfig {
    private final String token;

    @ConstructorBinding
    public TelegramConfig(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
