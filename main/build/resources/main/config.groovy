import org.example.AnotherService
import org.example.AnotherService2
import org.example.MyService

beans {
    myService(MyService, anotherService: ref('anotherService')) {
        anotherService2 = ref('anotherService2')
        scope = 'singleton'
    }
    anotherService(AnotherService) {
        scope = 'prototype'
    }
    anotherService2(AnotherService2)
}

