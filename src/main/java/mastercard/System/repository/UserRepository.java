package mastercard.System.repository;

import mastercard.System.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<Long , User> {
    Optional<User> findByUsername(String username);
}
