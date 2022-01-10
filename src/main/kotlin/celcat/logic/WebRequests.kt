package celcat.logic

import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.serialization.json.Json

val json = Json {
    isLenient = true
    ignoreUnknownKeys = true
    encodeDefaults = true
}

val client = HttpClient(CIO) {
    install(JsonFeature) {
        serializer = KotlinxSerializer(json = json)
    }
}
