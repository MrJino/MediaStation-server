package noh.jinil.boot.controller

import com.google.firebase.auth.FirebaseAuth
import noh.jinil.boot.data.AuthData
import noh.jinil.boot.response.ResponseCode
import noh.jinil.boot.response.ResponseData
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/api/firebase")
class AuthApiController {

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