package noh.jinil.boot

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
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

    val serviceAccount = FileInputStream("./mediastation-firebase-adminsdk.json")

    val options = FirebaseOptions.Builder()
            .setCredentials(GoogleCredentials.fromStream(serviceAccount))
            .build()

    FirebaseApp.initializeApp(options)

    /*
    val additionalClaims = HashMap<String, Any>()
    val customToken = FirebaseAuth.getInstance().createCustomTokenAsync("SOME-UID", additionalClaims).get()
    */
}