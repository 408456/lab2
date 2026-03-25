package goltsman.btuserservice.repository;


import goltsman.btuserservice.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKeycloakId(UUID keycloakId);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    Optional<User> findById(Long id);

    Optional<User> findByEmail(String email);
}
