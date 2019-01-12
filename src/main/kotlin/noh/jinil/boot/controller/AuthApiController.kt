package noh.jinil.boot.controller

import noh.jinil.boot.domain.data.AuthenticationData
import noh.jinil.boot.domain.response.ResponseCode
import noh.jinil.boot.domain.response.ResponseData
import noh.jinil.boot.service.FirebaseService
import noh.jinil.boot.service.UserService
import noh.jinil.boot.service.shared.RegisterUserInit
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

        val holder = firebaseService?.parseToken(auth.idToken) ?: return ResponseData.createFailed(ResponseCode.FAILED, "Failed")

        return if (userService?.verifyUser(holder) == true) {
            ResponseData.createExtra(ResponseCode.SIGNIN_DONE, "로그인에 성공하였습니다")
        } else if (userService?.registerUser(holder) != null) {
            ResponseData.createExtra(ResponseCode.SIGNUP_DONE, "회원가입을 축하드립니다")
        } else {
            ResponseData.createFailed(ResponseCode.FAILED, "Failed")
        }

        /*
        val details = userService?.loadUserByUsername(holder?.uid)

        return if (details == null) {
            ResponseData.createExtra(ResponseCode.NEED_SIGNUP, "회원가입이 필요합니다.")
        } else {
            ResponseData.createSuccess(Unit)
        }
        */
    }
}

private val logger = LoggerFactory.getLogger(AuthApiController::class.java)