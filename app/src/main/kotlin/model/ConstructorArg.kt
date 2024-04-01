package org.example.model


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class ConstructorArg(
    @SerialName("name") var name: String,
    @SerialName("ref") var ref: String? = null,
    @SerialName("value") var value: String? = null,
    @Transient var pathToRef: Class<*>? = null
) {
    init {
        require((ref != null && value == null) || (ref == null && value != null))
    }
}