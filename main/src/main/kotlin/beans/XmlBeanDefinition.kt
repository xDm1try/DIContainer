package org.example.beans

import kotlinx.serialization.Serializable

@Serializable
data class Property(var name: String, var ref: String)

@Serializable
data class XmlBeanDefinition(var name: String,
                             var className: String,
                             var scope: String,
                             var properties: List<Property>? = null)

@Serializable
data class XmlBeanDefinitions(var beans: List<XmlBeanDefinition>)