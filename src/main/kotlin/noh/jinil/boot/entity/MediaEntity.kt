package noh.jinil.boot.entity

import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.MappedSuperclass
import javax.persistence.Table

@MappedSuperclass
abstract class MediaEntity : BaseEntity() {
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