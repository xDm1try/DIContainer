package ru.nsu.fit.dicontainer.configurator

import groovy.lang.GroovyShell
import org.example.beans.BeanDefinition
import org.example.beans.BeanDefinitionPostProcessing
import org.example.parsing.GroovyConfigParser
import org.example.processing.JsonBeanProcessor
import parsing.JsonConfigParser
import java.io.File

class GroovyConfigurator : BeanConfigurator {
    private var path: String
    private var groovyParser: GroovyConfigParser = GroovyConfigParser()
    constructor(_path: String){
        path = _path
    }

    override fun getBeanDefinitions(): List<BeanDefinition> {
        val beanDefinitions = groovyParser.parse(path)
        return beanDefinitions
    }
}