package noh.jinil.boot.auth.firebase

import noh.jinil.boot.exception.FirebaseTokenInvalidException
import noh.jinil.boot.service.FirebaseService
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse


class FirebaseRequestFilter(private val firebaseService: FirebaseService?) : OncePerRequestFilter() {

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {
        val xAuth = request.getHeader(HEADER_NAME)
        if (xAuth.isNullOrEmpty()) {
            filterChain.doFilter(request, response)
            return
        }

        try {
            val holder = firebaseService?.parseToken(xAuth)
            val auth = FirebaseAuthToken(holder?.uid ?: "", holder)
            SecurityContextHolder.getContext().authentication = auth

            filterChain.doFilter(request, response)
        } catch (e: FirebaseTokenInvalidException) {
            throw SecurityException(e)
        }

    }

    companion object {
        var HEADER_NAME = "X-Authorization-Firebase"
        var HEADER_EXPLAIN = "IdToken from firebase"
    }
}