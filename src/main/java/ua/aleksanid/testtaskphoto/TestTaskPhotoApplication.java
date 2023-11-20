package ua.aleksanid.testtaskphoto;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import ua.aleksanid.testtaskphoto.configurations.AppConfiguration;
import ua.aleksanid.testtaskphoto.configurations.TelegramConfig;

@SpringBootApplication
@EnableJpaRepositories
@EnableConfigurationProperties({TelegramConfig.class, AppConfiguration.class})
@EnableScheduling
public class TestTaskPhotoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestTaskPhotoApplication.class, args);
    }

}
