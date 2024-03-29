package org.example.beans

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.example.model.ConstructorArg
import org.example.model.Property

@Serializable
data class JsonBeanDefinition(var name: String,
                              var classPath: String,
                              var scope: String? = "singleton",
                              var constructorArgs: List<ConstructorArg>? = null,
                              var properties: List<Property>? = null) : BeanDefinition

@Serializable
data class JsonBeanDefinitions(var beans: List<JsonBeanDefinition>)