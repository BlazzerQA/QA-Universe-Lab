package qa.universe.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import qa.universe.models.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    @Query(value = "SELECT * FROM users WHERE phone = :phone LIMIT 1", nativeQuery = true)
    Optional<User> findUserByPhone(@Param("phone") String phone);

}
