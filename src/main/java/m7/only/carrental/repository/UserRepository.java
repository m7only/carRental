package m7.only.carrental.repository;

import lombok.NonNull;
import m7.only.carrental.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    @NonNull
    Page<User> findAll(@NonNull Pageable pageable);
}
