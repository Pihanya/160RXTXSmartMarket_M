package ru.itmo.util

import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.io.Serializable
import java.time.LocalDateTime
import javax.persistence.*

@EntityListeners(AuditingEntityListener::class)
abstract class AuditAwareEntity : Serializable {

    @Column(name = "create_date", updatable = false)
    var creationDate: LocalDateTime? = null

    @Column(name = "edit_date")
    var editDate: LocalDateTime? = null

    @Version
    @Column(name = "Version")
    var version = 0

    @PrePersist
    fun prePersist() {
        (creationDate ?: LocalDateTime.now()).also { now ->
            creationDate = now
            editDate = now
        }
        version = 0
    }

    @PreUpdate
    fun preUpdate() {
        editDate = LocalDateTime.now()
        version += 1
    }
}