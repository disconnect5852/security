package asd.tsanyi.security.db;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import asd.tsanyi.security.db.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findByUsername(String username);
	
	@Query("SELECT u.id FROM User u WHERE u.username = ?1")
	Optional<Long> findIdByUsername(String username);
	
	@Query("SELECT u FROM User u WHERE u.username = ?1 AND u.password = ?2")
	Optional<User> login(String username, String password);
	
	@Query("SELECT u FROM User u WHERE u.email_token =?1")
	Optional<User> findByEmail_token(String token);
}
