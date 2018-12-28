package noh.jinil.boot.utils

import com.google.firebase.auth.FirebaseAuth
import org.slf4j.LoggerFactory

object AuthUtils {
    fun verify(idToken: String): Boolean {
        logger.info("verify() idToken=$idToken")

        val decoded = FirebaseAuth.getInstance().verifyIdToken(idToken)
        logger.debug("->${decoded.name}(${decoded.email}) has been login!!!")

        if (decoded.claims["adult"] == true) {
            return true
        }
        return false
    }
}

private val logger = LoggerFactory.getLogger(AuthUtils::class.java)