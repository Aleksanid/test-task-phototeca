package ua.aleksanid.testtaskphoto.configurations;

import java.math.BigDecimal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "app")
public class AppConfiguration {
    private final BigDecimal percentToNotify;
    private final int maxUsersToSupport;

    @ConstructorBinding
    public AppConfiguration(BigDecimal percentToNotify, int maxUsersToSupport) {
        this.percentToNotify = percentToNotify;
        this.maxUsersToSupport = maxUsersToSupport;
    }

    public BigDecimal getPercentToNotify() {
        return percentToNotify;
    }

    public int getMaxUsersToSupport() {
        return maxUsersToSupport;
    }
}
