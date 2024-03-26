package org.example

import org.example.processing.JsonBeanProcessor
import parsing.JsonConfigParser

fun main() {
    val jsonParser = JsonConfigParser()
    val jsonBeanDefinitions = jsonParser.parse("/config.json")
    val jsonProcessor = JsonBeanProcessor()
    val beanDefinitionsPostProcessing = jsonProcessor.process(jsonBeanDefinitions)
    for(bean in beanDefinitionsPostProcessing) {
            println("Name: ${bean.name}, Class: ${bean.clazz}, Scope: ${bean.scope}, Java class: ${bean.javaClass}")
            bean.properties?.forEach { property ->
                println("  Property: ${property.name}, Ref: ${property.ref}, Value: ${property.value}, PathToRef: ${property.pathToRef}")
            }
            bean.constructorArgs?.forEach { constructorArg ->
                println("  ConstructorArg: ${constructorArg.name}, Ref: ${constructorArg.ref}, Value: ${constructorArg.value}, PathToRef: ${constructorArg.pathToRef}")
            }
        }
    }

    //val binding = Binding()
    //val shell = GroovyShell(binding)
    //val filePath = "main/src/main/resources/config.groovy"
    //shell.evaluate(File(filePath))
    //print(binding.getVariable("beans"))
