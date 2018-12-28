package noh.jinil.boot.firebase;

import noh.jinil.boot.exception.FirebaseUserNotExistsException;
import noh.jinil.boot.service.UserServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class FirebaseAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	@Qualifier(value = UserServiceImpl.NAME)
	private UserDetailsService userService;

	public boolean supports(Class<?> authentication) {
		return (FirebaseAuthenticationToken.class.isAssignableFrom(authentication));
	}

	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
		if (!supports(authentication.getClass())) {
			return null;
		}

		FirebaseAuthenticationToken authenticationToken = (FirebaseAuthenticationToken) authentication;
		UserDetails details = userService.loadUserByUsername(authenticationToken.getName());

		if (details == null) {
			throw new FirebaseUserNotExistsException();
		}

		Logger logger = LoggerFactory.getLogger(FirebaseAuthenticationProvider.class);
		logger.debug("->username: " + details.getUsername());
		details.getAuthorities().forEach((authority) -> logger.debug("->authority: " + authority.getAuthority()));

		authenticationToken = new FirebaseAuthenticationToken(details, authentication.getCredentials(), details.getAuthorities());

		return authenticationToken;
	}

}
