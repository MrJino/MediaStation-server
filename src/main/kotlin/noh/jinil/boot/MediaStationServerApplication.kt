package noh.jinil.boot

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer
import java.io.FileInputStream

@SpringBootApplication
class MediaStationServerApplication : SpringBootServletInitializer() {
    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder =
            builder.sources(MediaStationServerApplication::class.java)
}

fun main(args: Array<String>) {
    runApplication<MediaStationServerApplication>(*args)
}