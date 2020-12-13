package ru.itmo.model.entity

import ru.itmo.model.entity.ShoppingSession.Companion.TABLE_NAME
import ru.itmo.util.AuditAwareEntity
import javax.persistence.*

@Entity
@Table(name = TABLE_NAME)
data class ShoppingSession(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "${TABLE_NAME}__id_seq")
    @SequenceGenerator(name = "${Cart.TABLE_NAME}__id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    var id: Long? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "cart_id", nullable = false, updatable = false)
    var cart: Cart? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false, updatable = false)
    var user: User? = null,

    @Column(name = "is_active", nullable = false)
    var active: Boolean? = null
) : AuditAwareEntity() {

    companion object {
        const val TABLE_NAME = "shopping_session"

        const val ID = "id"

        const val CART = "cart"

        const val USER = "user"

        const val ACTIVE = "active"
    }
}