package noh.jinil.boot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.builder.SpringApplicationBuilder
import org.springframework.boot.runApplication
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer

@SpringBootApplication
class MediaStationServerApplication : SpringBootServletInitializer() {
    override fun configure(builder: SpringApplicationBuilder): SpringApplicationBuilder =
            builder.sources(MediaStationServerApplication::class.java)
}

fun main(args: Array<String>) {
    runApplication<MediaStationServerApplication>(*args)
}