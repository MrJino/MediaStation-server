package noh.jinil.boot.domain.entity

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name = "VIDEO")
class VideoEntity : BaseMedia()