package parsing

import kotlinx.serialization.json.Json
import org.example.beans.BeanDefinition
import org.example.beans.JsonBeanDefinition
import org.example.beans.JsonBeanDefinitions
import org.example.config.ConfigParser

class JsonConfigParser : ConfigParser {

    override fun parse(fileName: String): List<JsonBeanDefinition> {
        val inputStream = object {}.javaClass.getResourceAsStream(fileName)
        val json = inputStream?.bufferedReader().use { it?.readText() }
        val beans = json?.let { Json.decodeFromString<JsonBeanDefinitions>(it) }
        /*beans?.beans?.forEach { bean ->
            println("Name: ${bean.name}, Class: ${bean.classPath}, Scope: ${bean.scope}, Java class: ${bean.javaClass}")
            bean.properties?.forEach { property ->
                println("  Property: ${property.name}, Ref: ${property.ref}")
            }
            bean.constructorArgs?.forEach { constructorArg ->
                println("  ConstructorArg: ${constructorArg.name}, Ref: ${constructorArg.ref}")
            }
        }*/
        val beansDefinitions: List<JsonBeanDefinition> = beans?.beans!!.toList()
        return beansDefinitions
    }
}