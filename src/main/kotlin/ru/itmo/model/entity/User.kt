package ru.itmo.model.entity

import ru.itmo.model.entity.User.Companion.TABLE_NAME
import ru.itmo.util.AuditAwareEntity
import javax.persistence.*

@Entity
@Table(name = TABLE_NAME)
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "${TABLE_NAME}__id_seq")
    @SequenceGenerator(name = "${Cart.TABLE_NAME}__id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    var id: Long? = null,

    @Column(name = "email", unique = true, nullable = false)
    var email: String? = null,

    @Column(name = "password", nullable = false)
    var password: String? = null
): AuditAwareEntity() {

    companion object {
        const val TABLE_NAME = "service_user"

        const val ID = "id"

        const val EMAIL = "email"

        const val password = "password"
    }
}