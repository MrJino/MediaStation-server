package noh.jinil.boot.service;

import noh.jinil.boot.firebase.FirebaseTokenHolder;

public interface FirebaseService {
	FirebaseTokenHolder parseToken(String idToken);
}
