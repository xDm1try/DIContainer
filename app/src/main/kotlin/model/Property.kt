package org.example.model

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Property(
    @SerialName("name") var name: String,
    @SerialName("ref") var ref: String? = null,
    @SerialName("value") var value: String? = null,
    @Transient var pathToRef: @Contextual Class<*>? = null
) {
    constructor(name: String, pathToRef: Class<*>) : this(name, pathToRef.toString(), null, pathToRef)
    constructor(name: String, value: String) : this(name, null, value, null)
    init {
        require((ref != null && value == null) || (ref == null && value != null))
    }
}