package noh.jinil.boot.domain.entity

import javax.persistence.*

@MappedSuperclass
abstract class BaseEntity {
    @Id
    @GeneratedValue
    var id: Long? = null

    @Column(updatable = false)
    var createdAt: Long? = null

    @Column
    var updatedAt: Long? = null

    @PrePersist
    fun onPersist() {
        this.updatedAt = System.currentTimeMillis()
        this.createdAt = this.updatedAt //LocalDateTime.now();
    }

    @PreUpdate
    protected fun onUpdate() {
        this.updatedAt = System.currentTimeMillis() //LocalDateTime.now();
    }
}