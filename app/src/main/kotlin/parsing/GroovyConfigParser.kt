package org.example.parsing

import groovy.lang.GroovyShell
import org.example.beans.BeanDefinitionPostProcessing
import org.example.config.ConfigParser
import java.io.File

class GroovyConfigParser {

    fun parse(fileName: String) : List<BeanDefinitionPostProcessing> {
        val shell = GroovyShell()
        shell.evaluate(File("main/src/main/kotlin/parsing/GroovyConfigReader.groovy"))
        val beanDefinitions = shell.evaluate(File(fileName)) as List<BeanDefinitionPostProcessing>
        return beanDefinitions
    }
}