import org.example.model.ConstructorArg
import org.example.model.Property
import ru.nsu.fit.dicontainer.reader.GroovyConfigReader
import ru.nsu.fit.dicontainer.service.GiftPresenter
import ru.nsu.fit.dicontainer.service.impl.Bike
import ru.nsu.fit.dicontainer.service.impl.Car
import ru.nsu.fit.dicontainer.service.impl.CardPaymentSystem
import ru.nsu.fit.dicontainer.service.impl.CashPaymentSystem
import ru.nsu.fit.dicontainer.service.impl.CourierYandex
import ru.nsu.fit.dicontainer.service.impl.PhoneTracker
import ru.nsu.fit.dicontainer.service.impl.PostDeliverySystem
import ru.nsu.fit.dicontainer.service.impl.SmartGiftChooseHelper
import ru.nsu.fit.dicontainer.service.impl.SmartRecommender

def reader = new GroovyConfigReader()

reader.beans {
    bean(
            "GiftPresenterName",
            GiftPresenter,
            "singleton",
            [],
            [
                    new Property("SmartGiftChooseHelperName", SmartGiftChooseHelper),
                    new Property("CashPaymentSystemName", CashPaymentSystem),
                    new Property("PostDeliverySystemName", PostDeliverySystem)
            ],
    )
    bean(
            "SmartGiftChooseHelperName",
            SmartGiftChooseHelper,
            "singleton",
            [],
            [
                    new Property("SmartRecommenderName", SmartRecommender)
            ]
    )
    bean(
            "SmartRecommenderName",
            SmartRecommender,
            "singleton"
    )
    bean(
            "CashPaymentSystemName",
            CashPaymentSystem,
            "singleton"
    )
    bean(
            "CardPaymentSystemName",
            CardPaymentSystem,
            "singleton"
    )
    bean(
            "PostDeliverySystemName",
            PostDeliverySystem,
            "singleton",
            [
                    new ConstructorArg("CarName", Car),
                    new ConstructorArg("YandexCourierName", CourierYandex),
                    new ConstructorArg("DeviceName", PhoneTracker)
            ]
    )
    bean(
            "YandexCourierName",
            CourierYandex,
            "thread",
            [
                    new ConstructorArg("PhraseCourier", "\"Yandex > Google\" сказал курьер Штирлиц")
            ]
    )
    bean(
            "CarName",
            Car,
            "thread"
    )
    bean(
            "BikeName",
            Bike,
            "thread"
    )
    bean(
            "DeviceName",
            PhoneTracker,
            "thread"
    )
}


