package noh.jinil.boot.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.io.FileInputStream
import javax.annotation.PostConstruct

@Configuration
class FirebaseConfig {

    @Value("\${static.path}")
    private val staticPath: String? = null

    @Suppress("unused")
    @PostConstruct
    fun init() {
        val serviceAccount = FileInputStream("${staticPath}/auth/mediastation-firebase-adminsdk.json")

        val options = FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build()

        FirebaseApp.initializeApp(options)
    }
}