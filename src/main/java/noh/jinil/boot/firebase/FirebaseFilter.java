package noh.jinil.boot.firebase;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noh.jinil.boot.exception.FirebaseTokenInvalidException;
import noh.jinil.boot.service.FirebaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

public class FirebaseFilter extends OncePerRequestFilter {

	private static String HEADER_NAME = "X-Authorization-Firebase";

	private FirebaseService firebaseService;

	public FirebaseFilter(FirebaseService firebaseService) {
		this.firebaseService = firebaseService;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {

		Logger logger = LoggerFactory.getLogger(FirebaseFilter.class);

		String xAuth = request.getHeader(HEADER_NAME);
		if (xAuth == null || xAuth.isEmpty()) {
			filterChain.doFilter(request, response);
			return;
		}

		try {
			FirebaseTokenHolder holder = firebaseService.parseToken(xAuth);

			String userName = holder.getUid();
			logger.debug("holder userName: " + userName);

			Authentication auth = new FirebaseAuthenticationToken(userName, holder);
			logger.debug("auth name: " + auth.getName());
			logger.debug(auth.toString());

			SecurityContextHolder.getContext().setAuthentication(auth);

			filterChain.doFilter(request, response);
		} catch (FirebaseTokenInvalidException e) {
			throw new SecurityException(e);
		}
	}

}
