package noh.jinil.boot.service.shared;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import noh.jinil.boot.exception.FirebaseTokenInvalidException;
import noh.jinil.boot.firebase.FirebaseTokenHolder;

public class FirebaseParser {
	public FirebaseTokenHolder parseToken(String idToken) {
		if (idToken == null || idToken.isEmpty()) {
			throw new IllegalArgumentException("FirebaseTokenBlank");
		}
		try {
			FirebaseToken token = FirebaseAuth.getInstance().verifyIdToken(idToken);

			return new FirebaseTokenHolder(token);
		} catch (Exception e) {
			throw new FirebaseTokenInvalidException(e.getMessage());
		}
	}
}