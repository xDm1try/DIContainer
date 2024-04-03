package org.example.beans

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.example.model.ConstructorArg
import org.example.model.Property

abstract class BeanDefinition(
    val clazz: Class<*>,
    val name: String,
    val scope: String?,
    val constructorArgs: List<ConstructorArg>?,
    val properties: List<Property>?
)