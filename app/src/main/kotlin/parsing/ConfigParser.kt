package org.example.config

import org.example.beans.BeanDefinition
import org.example.beans.JsonBeanDefinition
import java.util.Objects

interface ConfigParser {
    fun parse(fileName: String) : List<JsonBeanDefinition>
}