package org.example.processing

import org.example.beans.BeanDefinitionPostProcessing
import org.example.beans.JsonBeanDefinition

class JsonBeanProcessor {
    fun process(beans: List<JsonBeanDefinition>): List<BeanDefinitionPostProcessing> {
        val beanDefinitions: MutableList<BeanDefinitionPostProcessing> = mutableListOf()
        for(bean in beans) {
            beanDefinitions.add(BeanDefinitionPostProcessing(
                Class.forName(bean.classPath),
                bean.name,
                bean.scope,
                bean.constructorArgs,
                bean.properties)
            )
        }

        for(beanDefinition in beanDefinitions) {
            if (beanDefinition.constructorArgs != null) {
                for (constructorArg in beanDefinition.constructorArgs) {
                    if (constructorArg.ref != null) {
                        for (beanDefinition2 in beanDefinitions) {
                            if (beanDefinition2 == beanDefinition) {
                                continue
                            }
                            if (beanDefinition2.name == constructorArg.ref) {
                                constructorArg.pathToRef = beanDefinition2.clazz
                                break
                            }
                        }
                    }
                }
            }
        }

        for(beanDefinition in beanDefinitions) {
            if (beanDefinition.properties != null) {
                for (property in beanDefinition.properties) {
                    if (property.ref != null) {
                        for (beanDefinition2 in beanDefinitions) {
                            if (beanDefinition2 == beanDefinition) {
                                continue
                            }
                            if (beanDefinition2.name == property.ref) {
                                property.pathToRef = beanDefinition2.clazz
                                break
                            }
                        }
                    }
                }
            }
        }
        return beanDefinitions
    }
}