package ru.itmo.model.entity

import ru.itmo.model.entity.Cart.Companion.TABLE_NAME
import ru.itmo.util.AuditAwareEntity
import javax.persistence.*

@Entity
@Table(name = TABLE_NAME)
data class Cart(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "${TABLE_NAME}__id_seq")
    @SequenceGenerator(name = "${TABLE_NAME}__id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    var id: Long? = null,

    @Column(name = "secret_token", unique = true, nullable = false, updatable = false)
    var secretToken: String? = null
) : AuditAwareEntity() {

    companion object {
        const val TABLE_NAME = "cart"

        const val ID = "id"

        const val SECRET_TOKEN = "secretToken"
    }
}