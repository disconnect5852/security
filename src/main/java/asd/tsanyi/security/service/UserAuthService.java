package asd.tsanyi.security.service;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.stereotype.Service;

import asd.tsanyi.security.db.UserRepository;
import asd.tsanyi.security.db.entity.User;

@Service
public class UserAuthService {
	
	@Autowired
	private UserRepository userRepo;
	
	public String authenticate(String username, String password) {
		Optional<User> possibleUser= userRepo.login(username, password);
		if (!possibleUser.isPresent()) return null;
		User user= possibleUser.get();
		String token=UUID.randomUUID().toString();
		user.setEmail_token(token);
		Date date=new Date();
		user.setUpdated_at(date);
		user.setLast_login(date);
		userRepo.save(user);
		return token;
	}
	
	@Transactional
    public Optional<org.springframework.security.core.userdetails.User> findByToken(String token) {
        Optional<User> maybeUser= userRepo.findByEmail_token(token);
        if(maybeUser.isPresent()){
        	User user = maybeUser.get();
        	String[] roles= user.getRoles().stream().map( r -> r.getRoles().toString()).toArray(String[]::new);
            org.springframework.security.core.userdetails.User secuUser= new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), !user.isDeleted_flag(), true, !user.isPassword_expired(), true, AuthorityUtils.createAuthorityList(roles));
            return Optional.of(secuUser);
        }
        return  Optional.empty();
    }
}
