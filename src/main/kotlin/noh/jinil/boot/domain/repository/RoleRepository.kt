package noh.jinil.boot.domain.repository

import noh.jinil.boot.domain.entity.RoleEntity
import org.springframework.data.repository.CrudRepository

interface RoleRepository : CrudRepository<RoleEntity, Long> {
    fun findByAuthority(authority: String): RoleEntity?
}