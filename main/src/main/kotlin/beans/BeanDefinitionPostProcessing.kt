package org.example.beans

import groovy.transform.ToString
import org.example.model.ConstructorArg
import org.example.model.Property

class BeanDefinitionPostProcessing(
    val clazz: Class<*>,
    val name: String,
    val scope: String?,
    val constructorArgs: List<ConstructorArg>?,
    val properties: List<Property>?
) : BeanDefinition