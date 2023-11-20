package ua.aleksanid.testtaskphoto.repositories;

import jakarta.transaction.Transactional;
import java.sql.Timestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ua.aleksanid.testtaskphoto.models.entities.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsByTelegramId(String telegramId);
    @Modifying
    @Transactional
    @Query("UPDATE User u SET u.startTime = :newStartTime WHERE u.telegramId = :telegramId")
    int updateStartTimeByTelegramId(@Param("telegramId") String telegramId,
                                    @Param("newStartTime") Timestamp newStartTime);
}
