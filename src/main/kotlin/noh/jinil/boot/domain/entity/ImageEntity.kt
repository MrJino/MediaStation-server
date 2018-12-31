package noh.jinil.boot.domain.entity

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name="IMAGE")
class ImageEntity : BaseMedia()