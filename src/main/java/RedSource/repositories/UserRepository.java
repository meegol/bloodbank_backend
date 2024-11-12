package RedSource.repositories;

import RedSource.Model.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {



    // Update the repository method to return a list
    List<User> findAllByRole(String role);


    Optional<User> findByEmail(String email);
}
