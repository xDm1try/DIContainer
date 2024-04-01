package ru.nsu.fit.dicontainer.configurator

import org.example.beans.BeanDefinition
import org.example.processing.JsonBeanProcessor
import parsing.JsonConfigParser

class JsonConfigurator: BeanConfigurator {
    private var path: String
    private var jsonParser: JsonConfigParser = JsonConfigParser()
    constructor(_path: String){
        path = _path
    }

    override fun getBeanDefinitions(): List<BeanDefinition> {
        val jsonBeanDefinitions = jsonParser.parse(path)
        val jsonProcessor = JsonBeanProcessor()
        return jsonProcessor.process(jsonBeanDefinitions)
    }
}