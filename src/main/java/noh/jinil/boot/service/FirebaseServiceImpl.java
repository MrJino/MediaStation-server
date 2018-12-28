package noh.jinil.boot.service;

import noh.jinil.boot.firebase.FirebaseTokenHolder;
import noh.jinil.boot.service.shared.FirebaseParser;
import org.springframework.stereotype.Service;

@Service
public class FirebaseServiceImpl implements FirebaseService {
	@Override
	public FirebaseTokenHolder parseToken(String firebaseToken) {
		return new FirebaseParser().parseToken(firebaseToken);
	}
}
