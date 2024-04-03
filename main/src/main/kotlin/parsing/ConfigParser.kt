package org.example.parsing

import org.example.beans.BeanDefinition
import java.util.Objects

interface ConfigParser {
    fun parse(fileName: String) : List<BeanDefinition>
}