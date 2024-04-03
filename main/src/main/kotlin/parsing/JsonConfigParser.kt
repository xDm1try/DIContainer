package parsing

import kotlinx.serialization.json.Json
import org.example.beans.JsonBeanDefinition
import org.example.beans.JsonBeanDefinitions
import org.example.parsing.ConfigParser

class JsonConfigParser : ConfigParser {

    override fun parse(fileName: String): List<JsonBeanDefinition> {
        val inputStream = object {}.javaClass.getResourceAsStream(fileName)
        val json = inputStream?.bufferedReader().use { it?.readText() }
        val beans = json?.let { Json.decodeFromString<JsonBeanDefinitions>(it) }

        val beansDefinitions: List<JsonBeanDefinition> = beans?.beans!!.toList()
        return beansDefinitions
    }
}