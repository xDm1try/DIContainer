package org.example.config

import org.example.beans.BeanDefinition
import java.util.Objects

interface ConfigParser {
    fun parse(fileName: String) : List<BeanDefinition>
}