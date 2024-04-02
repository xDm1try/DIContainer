package org.example

import groovy.lang.Binding
import groovy.lang.GroovyShell
import org.example.parsing.GroovyConfigParser
import org.example.processing.JsonBeanProcessor
import parsing.JsonConfigParser
import java.io.File


fun main() {
    val jsonParser = JsonConfigParser()
    val jsonBeanDefinitions = jsonParser.parse("/config.json")
    val jsonProcessor = JsonBeanProcessor()
    val beanDefinitionsPostProcessing = jsonProcessor.process(jsonBeanDefinitions)
    println("---------------------------")
    println("----------Json-------------")
    for (bean in beanDefinitionsPostProcessing) {
        println("Name: ${bean.name}, Class: ${bean.clazz}, Scope: ${bean.scope}")
        bean.properties?.forEach { property ->
            println("  Property: ${property.name}, Ref: ${property.ref}, Value: ${property.value}, PathToRef: ${property.pathToRef}")
        }
        bean.constructorArgs?.forEach { constructorArg ->
            println("  ConstructorArg: ${constructorArg.name}, Ref: ${constructorArg.ref}, Value: ${constructorArg.value}, PathToRef: ${constructorArg.pathToRef}")
        }
    }
    println("----------------------------")
    println("----------Groovy------------")
    val groovyConfigParser = GroovyConfigParser()
    val beanDefinitions = groovyConfigParser.parse("main/src/main/resources/config.groovy")
    for (bean in beanDefinitions) {
        println("Name: ${bean.name}, Class: ${bean.clazz}, Scope: ${bean.scope}")
        bean.properties?.forEach { property ->
            println("  Property: ${property.name}, Ref: ${property.ref}, Value: ${property.value}, PathToRef: ${property.pathToRef}")
        }
        bean.constructorArgs?.forEach { constructorArg ->
            println("  ConstructorArg: ${constructorArg.name}, Ref: ${constructorArg.ref}, Value: ${constructorArg.value}, PathToRef: ${constructorArg.pathToRef}")
        }
    }
}

