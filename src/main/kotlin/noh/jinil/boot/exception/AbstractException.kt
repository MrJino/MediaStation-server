package noh.jinil.boot.exception

import java.lang.RuntimeException

class AbstractException(message: String?, var key: String, cause: Throwable) : RuntimeException(message, cause) {
    private val serialVersionUID = 9004674664241996502L
}