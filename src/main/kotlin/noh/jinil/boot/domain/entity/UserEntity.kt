package noh.jinil.boot.domain.entity

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity(name = "USER")
@Table(name = "USER")
class UserEntity : UserDetails {

    @Transient
    private val serialVersionUID = 4815877135015943617L

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private var id = 0L

    @Column(name = "USERNAME", nullable = false, unique = true)
    @JvmField var username = ""

    @Column(name = "PASSWORD", nullable = false)
    @JvmField var password = ""

    @Column(name = "EMAIL", nullable = false)
    var email = ""

    @ManyToMany(fetch = FetchType.EAGER, cascade = [CascadeType.ALL])
    @JvmField var authorities: List<RoleEntity>? = null

    override fun getAuthorities(): MutableCollection<out GrantedAuthority>? = authorities?.toMutableList()

    override fun getUsername() = username

    override fun getPassword() = password

    override fun isEnabled() = false

    override fun isCredentialsNonExpired() = false

    override fun isAccountNonExpired() = false

    override fun isAccountNonLocked() = false
}