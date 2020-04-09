package asd.tsanyi.security.rest.controller;

import java.security.Principal;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import asd.tsanyi.security.db.UserRepository;
import asd.tsanyi.security.db.entity.User;
import asd.tsanyi.security.dto.InfoResponse;
import asd.tsanyi.security.dto.LoginCrendential;
import asd.tsanyi.security.dto.UserDTO;
import asd.tsanyi.security.enums.ResponseTypes;
import asd.tsanyi.security.service.UserAuthService;

@RestController
public class Rest {
	private Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private UserAuthService userauth;

	@Transactional
	@PostMapping(path = "/authentication")
	@ResponseBody
	ResponseEntity<InfoResponse> authenticate(@RequestBody LoginCrendential login) {
		String token = userauth.authenticate(login.getUsername(), login.getPassword());
		if (token == null || token.isEmpty())
			return InfoResponse.createResponseEntity(ResponseTypes.ERROR, "login fail", HttpStatus.UNAUTHORIZED);
		return InfoResponse.createResponseEntity(ResponseTypes.SUCCESS, token, HttpStatus.ACCEPTED);
	}

	@Transactional
	@PostMapping(path = "/user")
	@ResponseBody
	ResponseEntity<InfoResponse> createUser(@RequestBody UserDTO userDTO, Principal principal) {
		User newUser = new User(userDTO);
		if (principal != null) { // needed when unit tests bypassing authentication
			Optional<Long> creatorId = userRepo.findIdByUsername(principal.getName());
			newUser.setCreated(creatorId.isPresent() ? creatorId.get() : 0L);
		}
		return InfoResponse.createResponseEntity(ResponseTypes.SUCCESS, "new user id: " + userRepo.save(newUser).getId(), HttpStatus.CREATED);
	}

	@GetMapping(path = "/user")
	@ResponseBody
	ResponseEntity getMyself(Principal principal) {
		if (principal != null) { // needed when unit tests bypassing authentication
			Optional<User> user = userRepo.findByUsername(principal.getName());
			if (user.isPresent())
				return new ResponseEntity<User>(user.get(), HttpStatus.FOUND);
		}
		return InfoResponse.createResponseEntity(ResponseTypes.WARNING, "I've lost myself", HttpStatus.NOT_FOUND);
	}

	@GetMapping(path = "/user/{id}")
	@ResponseBody
	ResponseEntity getUserById(@PathVariable long id) {
		Optional<User> user = userRepo.findById(id);
		if (user.isPresent())
			return new ResponseEntity<User>(user.get(), HttpStatus.FOUND);
		return InfoResponse.createResponseEntity(ResponseTypes.WARNING, "user not found", HttpStatus.NOT_FOUND);
	}

	@Transactional
	@DeleteMapping(path = "/user/{id}")
	@ResponseBody
	ResponseEntity<InfoResponse> deleteUser(@PathVariable long id, Principal principal) {
		// userRepo.deleteById(id);
		Optional<User> deletable = userRepo.findById(id);
		if (deletable.isPresent()) {
			User entity = deletable.get();
			if (entity.isDeleted_flag())
				return InfoResponse.createResponseEntity(ResponseTypes.WARNING, "entity deleted already!", HttpStatus.GONE);
			entity.setDeleted_flag(true);
			if (principal != null) { // needed when unit tests bypassing authentication
				Optional<Long> deleterId = userRepo.findIdByUsername(principal.getName());
				entity.setDeleted(deleterId.isPresent() ? deleterId.get() : 0L);
			}
			userRepo.save(entity);
		} else {
			return InfoResponse.createResponseEntity(ResponseTypes.WARNING, "entity not found :'(", HttpStatus.NOT_FOUND);
		}
		return InfoResponse.createResponseEntity(ResponseTypes.SUCCESS, id + " is deleted", HttpStatus.OK);
	}
}
