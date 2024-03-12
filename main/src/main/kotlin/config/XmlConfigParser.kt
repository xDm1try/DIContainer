package config

import kotlinx.serialization.json.Json
import org.example.beans.XmlBeanDefinitions

class XmlConfigParser {

    fun parse(fileName: String) {
        val inputStream = object {}.javaClass.getResourceAsStream(fileName)
        val json = inputStream?.bufferedReader().use { it?.readText() }
        val beans = json?.let { Json.decodeFromString<XmlBeanDefinitions>(it) }
        /*beans?.beans?.forEach { bean ->
            println("Name: ${bean.name}, Class: ${bean.className}, Scope: ${bean.scope}")
            bean.properties?.forEach { property ->
                println("  Property: ${property.name}, Ref: ${property.ref}")
            }
        }*/
    }
}