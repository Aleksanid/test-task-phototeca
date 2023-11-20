package ua.aleksanid.testtaskphoto.services;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import org.springframework.stereotype.Service;
import ua.aleksanid.testtaskphoto.models.entities.User;
import ua.aleksanid.testtaskphoto.repositories.UserRepository;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void addNewUser(String telegramId, Instant startInstant) {
        User user = new User();
        user.setTelegramId(telegramId);
        user.setStartTime(Timestamp.from(startInstant));
        userRepository.save(user);
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public long getUserCount() {
        return userRepository.count();
    }

    public int updateUserStartTime(String telegramId, Timestamp startTime) {
        return userRepository.updateStartTimeByTelegramId(telegramId, startTime);
    }

    public boolean isUserExistsByTelegramId(String telegramId) {
        return userRepository.existsByTelegramId(telegramId);
    }
}
