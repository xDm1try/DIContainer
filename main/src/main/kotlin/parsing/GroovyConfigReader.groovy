package parsing
import groovy.transform.ToString
import org.example.beans.BeanDefinitionPostProcessing
import org.example.model.ConstructorArg
import org.example.model.Property

/*
@ToString
class ConstructorArg {

    String name

    String value

    Class ref

    static void main(String[] args) {

    }
}

@ToString
class Property {

    String name

    String value

    Class ref

}

@ToString
class BeanDefinition {
    String name
    Class clazz
    String scope
    List<ConstructorArg> constructorArgs
    List<Property> properties
}
*/

class BeanDefinitionFactory {
    List<BeanDefinitionPostProcessing> beans = []

    static void main(String[] args) {}

    def bean(
            String name,
            Class clazz,
            String scope = "singleton",
            List<ConstructorArg> constructorArgs = null,
            List<Property> properties = null
    ) {
        beans << new BeanDefinitionPostProcessing(
                clazz,
                name,
                scope,
                constructorArgs,
                properties
        )
    }
}

class GroovyConfigReader {

    List<BeanDefinitionPostProcessing> readBeans(Closure closure) {

        BeanDefinitionFactory beanDefinitionFactory = new BeanDefinitionFactory()
        beanDefinitionFactory.with closure
        return beanDefinitionFactory.beans
    }

    def beans(Closure closure) {
        readBeans(closure)
    }
}
