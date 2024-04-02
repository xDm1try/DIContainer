import org.example.AnotherService
import org.example.AnotherService2
import org.example.MyService
import org.example.model.ConstructorArg
import org.example.model.Property
import ru.nsu.fit.dicontainer.reader.GroovyConfigReader

def reader = new GroovyConfigReader()

reader.beans {
    bean(
            "GiftPresenterName",
            GiftPresenterName,
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


