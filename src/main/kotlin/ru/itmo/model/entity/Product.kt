package ru.itmo.model.entity

import ru.itmo.model.entity.Product.Companion.TABLE_NAME
import ru.itmo.util.AuditAwareEntity
import javax.persistence.*

@Entity
@Table(name = TABLE_NAME)
data class Product(
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "${TABLE_NAME}__id_seq")
    @SequenceGenerator(name = "${Cart.TABLE_NAME}__id_seq", allocationSize = 1)
    @Column(name = "id", unique = true, nullable = false, insertable = false, updatable = false)
    var id: Long? = null,

    @Column(name = "name", nullable = false)
    var name: String? = null
): AuditAwareEntity() {

    companion object {
        const val TABLE_NAME = "product"
    }
}