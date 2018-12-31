package noh.jinil.boot.domain.model

import org.springframework.security.core.GrantedAuthority
import javax.persistence.*

@Entity(name = "ROLE")
@Table(name = "ROLE")
class RoleEntity(myAuthority: String) : GrantedAuthority {

    @Transient
    private val serialVersionUID = -8186644851823152209L

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long? = null

    @Column(name = "AUTHORITY")
    @JvmField var authority = myAuthority

    override fun getAuthority(): String {
        return authority
    }
}