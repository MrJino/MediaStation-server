package noh.jinil.boot.domain.entity

import javax.persistence.Column
import javax.persistence.MappedSuperclass

@MappedSuperclass
abstract class BaseMedia : BaseEntity() {
    @Column(name = "THUMB")
    var thumbsUri: String? = null

    @Column(name = "SOURCE")
    var sourceUri: String? = null

    @Column(name = "TAGS")
    var tags: String? = null

    @Column(name = "META_WIDTH")
    var width: Int? = null

    @Column(name = "META_HEIGHT")
    var height: Int? = null
}