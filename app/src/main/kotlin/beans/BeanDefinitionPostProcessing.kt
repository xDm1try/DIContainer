package org.example.beans

import org.example.model.ConstructorArg
import org.example.model.Property

class BeanDefinitionPostProcessing(
    val clazz: Class<*>,
    val name: String? = null,
    val scope: String?,
    val constructorArgs: List<ConstructorArg>?,
    val properties: List<Property>?
) : BeanDefinition