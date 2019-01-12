package noh.jinil.boot.service

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseToken
import noh.jinil.boot.exception.FirebaseTokenInvalidException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class FirebaseService {
    fun parseToken(idToken: String?): FirebaseToken {
        if (idToken == null || idToken.isEmpty()) {
            throw IllegalArgumentException("FirebaseTokenBlank")
        }
        try {
            //logger.debug("parseToken()")
            return FirebaseAuth.getInstance().verifyIdToken(idToken)

            /*
            HashMap<String, Any>().run {
                put("admin", true)
                FirebaseAuth.getInstance().setCustomUserClaims(decodedToken.uid, this)
            }
             */
        } catch (e: Exception) {
            throw FirebaseTokenInvalidException(e.message)
        }
    }
}

//private val logger = LoggerFactory.getLogger(FirebaseService::class.java)