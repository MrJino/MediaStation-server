package noh.jinil.boot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MediaStationServerApplication

fun main(args: Array<String>) {
    runApplication<MediaStationServerApplication>(*args)
}
