package noh.jinil.boot.repository

import noh.jinil.boot.entity.VideoEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface VideoRepository : JpaRepository<VideoEntity, Long> {
    override fun findById(id: Long): Optional<VideoEntity>
}