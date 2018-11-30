package noh.jinil.boot.repository

import noh.jinil.boot.entity.Video
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface VideoRepository : JpaRepository<Video, Long> {
    override fun findById(id: Long): Optional<Video>
}