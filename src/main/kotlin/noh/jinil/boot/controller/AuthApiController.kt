package noh.jinil.boot.controller

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import noh.jinil.boot.data.AuthData
import noh.jinil.boot.response.ResponseCode
import noh.jinil.boot.response.ResponseData
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.io.FileInputStream
import java.lang.Exception
import javax.annotation.PostConstruct

@Controller
@RequestMapping("/api/auth")
class AuthApiController {

    @Value("\${static.path}")
    val staticPath: String? = null

    @PostConstruct
    @Suppress("unused")
    fun init() {
        val serviceAccount = FileInputStream("${staticPath}/auth/mediastation-firebase-adminsdk.json")

        val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()

        FirebaseApp.initializeApp(options)
    }

    @PostMapping("/verify")
    @ResponseBody
    @Suppress("unused")
    fun verifyIdToken(@RequestBody auth: AuthData): ResponseData<String> {
        logger.info("verifyIdToken()")
        try {
            val decodedToken = FirebaseAuth.getInstance().verifyIdToken(auth.idToken)
            logger.debug("->${decodedToken.name}(${decodedToken.email}) has been login!!!")

            HashMap<String, Any>().run {
                put("admin", true)
                FirebaseAuth.getInstance().setCustomUserClaims(decodedToken.uid, this)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            return ResponseData.createFailed(ResponseCode.RESPONSE_CODE_FAILED, "Invalid User")
        }

        return ResponseData.createSuccess("success")
    }
}

private val logger = LoggerFactory.getLogger(AuthApiController::class.java)