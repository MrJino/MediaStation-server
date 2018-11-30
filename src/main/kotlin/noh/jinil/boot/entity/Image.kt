package noh.jinil.boot.entity

import javax.persistence.Entity
import javax.persistence.Table

@Entity
@Table(name="IMAGE")
class Image : MediaEntity()