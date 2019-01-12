package noh.jinil.boot.domain.repository

import noh.jinil.boot.domain.entity.UserEntity
import org.springframework.data.repository.CrudRepository

interface UserRepository : CrudRepository<UserEntity, Long> {
    fun findByUsername(username: String): UserEntity?
}