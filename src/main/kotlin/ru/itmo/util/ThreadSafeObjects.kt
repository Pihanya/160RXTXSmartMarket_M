package ru.itmo.util

import com.fasterxml.jackson.databind.ObjectMapper

object ThreadSafeObjects {

    val OBJECT_MAPPER: ObjectMapper

    init {
        OBJECT_MAPPER = ObjectMapper()
    }
}