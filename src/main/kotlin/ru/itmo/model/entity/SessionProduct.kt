package ru.itmo.model.entity

import ru.itmo.model.entity.SessionProduct.Companion.TABLE_NAME
import ru.itmo.util.AuditAwareEntity
import javax.persistence.*

@Entity
@Table(
    name = TABLE_NAME,
    uniqueConstraints = [
        UniqueConstraint(
            name = "cart_id__product_id__session_id__unq",
            columnNames = ["product_id", "session_id"]
        )
    ]
)
data class SessionProduct(

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "${Product.TABLE_NAME}__id_seq")
    @SequenceGenerator(name = "${Cart.TABLE_NAME}__id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    var id: Long? = null,

    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = false, updatable = false)
    var product: Product? = null,

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "session_id", nullable = false, updatable = false)
    var session: ShoppingSession? = null,

    @Column(name = "quantity", nullable = false)
    var quantity: Int? = null
) : AuditAwareEntity() {

    companion object {
        const val TABLE_NAME = "session_product"

        const val ID = "id"

        const val PRODUCT = "product"

        const val SESSION = "session"

        const val QUANTITY = "quantity"
    }
}
