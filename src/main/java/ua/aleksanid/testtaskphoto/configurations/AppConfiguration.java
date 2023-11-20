package ua.aleksanid.testtaskphoto.configurations;

import java.math.BigDecimal;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "app")
public class AppConfiguration {
    private final BigDecimal percentToNotify;
    private final int maxUsersSupported;

    @ConstructorBinding
    public AppConfiguration(BigDecimal percentToNotify, int maxUsersSupported) {
        this.percentToNotify = percentToNotify;
        this.maxUsersSupported = maxUsersSupported;
    }

    public BigDecimal getPercentToNotify() {
        return percentToNotify;
    }

    public int getMaxUsersSupported() {
        return maxUsersSupported;
    }
}
