package noh.jinil.boot.service;

import noh.jinil.boot.config.SecurityConfig;
import noh.jinil.boot.domain.dao.RoleRepository;
import noh.jinil.boot.domain.dao.UserRepository;
import noh.jinil.boot.domain.model.RoleEntity;
import noh.jinil.boot.domain.model.UserEntity;
import noh.jinil.boot.service.shared.RegisterUserInit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

@Service(value = UserServiceImpl.NAME)
public class UserServiceImpl implements UserService {

	public final static String NAME = "UserService";
	private final static Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private RoleRepository roleRepository;

	@PostConstruct
	public void init() {
		userRepository.deleteAll();
		roleRepository.deleteAll();

		if (userRepository.count() == 0) {
			UserEntity adminEntity = new UserEntity();
			adminEntity.setUsername("1iTC5nOXFmeakKyM1NQ7EJr0TmB3");
			adminEntity.setPassword("admin");
			adminEntity.setEmail("for1self@gmail.com");

			adminEntity.setAuthorities(getAdminRoles());
			userRepository.save(adminEntity);

			UserEntity userEntity = new UserEntity();
			userEntity.setUsername("google_gabsikee@gmail.com");
			userEntity.setPassword("user");
			userEntity.setEmail("gabsikee@gmail.com");
			userEntity.setAuthorities(getUserRoles());

			userRepository.save(userEntity);
		}
	}

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserDetails userDetails = userRepository.findByUsername(username);
		if (userDetails == null)
			return null;

		/*
		Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
		for (GrantedAuthority role : userDetails.getAuthorities()) {
			grantedAuthorities.add(new SimpleGrantedAuthority(role.getAuthority()));
		}
		*/

		return new org.springframework.security.core.userdetails.User(userDetails.getUsername(),
				userDetails.getPassword(), userDetails.getAuthorities());
	}

	@Override
	@Transactional
	@Secured(value = SecurityConfig.Roles.ROLE_ANONYMOUS)
	public UserEntity registerUser(RegisterUserInit init) {

		UserEntity userLoaded = userRepository.findByUsername(init.getUserName());

		if (userLoaded == null) {
			UserEntity userEntity = new UserEntity();
			userEntity.setUsername(init.getUserName());
			userEntity.setEmail(init.getEmail());

			userEntity.setAuthorities(getUserRoles());
			userEntity.setPassword(UUID.randomUUID().toString());
			userRepository.save(userEntity);
			logger.info("registerUser -> user created");
			return userEntity;
		} else {
			logger.info("registerUser -> user exists");
			return userLoaded;
		}
	}

	private List<RoleEntity> getAdminRoles() {
		return Collections.singletonList(getRole(SecurityConfig.Roles.ROLE_ADMIN));
	}

	private List<RoleEntity> getUserRoles() {
		return Collections.singletonList(getRole(SecurityConfig.Roles.ROLE_USER));
	}

	private RoleEntity getRole(String authority) {
		RoleEntity role = roleRepository.findByAuthority(authority);
		if (role == null) {
			return new RoleEntity(authority);
		} else {
			return role;
		}
	}

}
