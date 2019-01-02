package noh.jinil.boot.controller

import noh.jinil.boot.domain.data.AuthenticationData
import noh.jinil.boot.domain.response.ResponseCode
import noh.jinil.boot.domain.response.ResponseData
import noh.jinil.boot.service.FirebaseService
import noh.jinil.boot.service.UserService
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/api/auth")
class AuthApiController {

    @Autowired
    private val firebaseService: FirebaseService? = null

    @Autowired
    private val userService: UserService? = null

    @PostMapping("/verify")
    @ResponseBody
    fun verifyUser(@RequestBody auth: AuthenticationData): ResponseData<Unit> {
        logger.info("verifyUser()")

        val holder = firebaseService?.parseToken(auth.idToken)
        val details = userService?.loadUserByUsername(holder?.uid)

        return if (details == null) {
            ResponseData.createExtra(ResponseCode.NEED_SIGNUP, "회원가입이 필요합니다.")
        } else {
            ResponseData.createSuccess(Unit)
        }
    }
}

private val logger = LoggerFactory.getLogger(AuthApiController::class.java)