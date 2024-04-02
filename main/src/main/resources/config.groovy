import org.example.MyService
import org.example.AnotherService
import org.example.AnotherService2
import org.example.model.ConstructorArg
import org.example.model.Property
import parsing.*

def reader = new GroovyConfigReader()

reader.beans {
    bean(
            "myService",
            MyService,
            "singleton",
            [
                    new ConstructorArg("anotherService", AnotherService),
                    new ConstructorArg("secondParameter", "chipi chipi chapa chapa")
            ],
            [
                    new Property("anotherService2", AnotherService2)
            ]
    )
    bean(
            "anotherService",
            AnotherService,
            "prototype"
    )
    bean(
            "anotherService2",
            AnotherService2
    )
}
