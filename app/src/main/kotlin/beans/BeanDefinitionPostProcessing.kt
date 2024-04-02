package org.example.beans

import org.example.model.ConstructorArg
import org.example.model.Property

class BeanDefinitionPostProcessing(
    clazz: Class<*>,
    name: String,
    scope: String?,
    constructorArgs: List<ConstructorArg>?,
    properties: List<Property>?
) : BeanDefinition(clazz, name, scope, constructorArgs, properties)