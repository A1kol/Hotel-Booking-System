package prime.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import prime.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByMail(String mail);
    Optional<User> findByMail(String mail);
}
