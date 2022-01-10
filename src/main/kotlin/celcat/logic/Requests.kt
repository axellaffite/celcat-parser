package celcat.logic

import celcat.domain.Event
import celcat.domain.Group
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.request.forms.*
import io.ktor.http.*
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.jsonObject
import java.time.temporal.ChronoUnit
import java.util.*


private inline fun Parameters.Companion.buildCelcatParameters(init: ParametersBuilder.() -> Unit): Parameters {
    return ParametersBuilder().apply {
        append("resType", "103")
        append("calView", "agendaDay")
        append("colourScheme", "3")
        init()
    }.build()
}

suspend fun getEvents(
    client: HttpClient,
    link: String,
    start: Date,
    end: Date,
    formations: List<String>,
    classes: Set<String>,
    courses: Map<String, String>
): List<Event> {
    val events: JsonArray = client.submitForm(
        url = link,
        formParameters = Parameters.buildCelcatParameters {
            append("start", start.toCelcatDate())
            append("end", end.toCelcatDate())

            formations.forEach { append("federationIds[]", it) }
        },
        block = { header(HttpHeaders.Accept, ContentType.Application.Json) }
    )

    return events.map { parseEventsFromJson(it.jsonObject, classes, courses) }
}

suspend fun getClassrooms(client: HttpClient, link: String): List<String> {
    return client.get<ClassroomsRequest>(link) {
        header(HttpHeaders.Accept, ContentType.Application.Json.withCharset(Charsets.UTF_8))
    }.results
}

suspend fun getCoursesNames(client: HttpClient, link: String): Map<String, String> {
    return client.get<CoursesRequest>(link).results
}

suspend fun getGroups(client: HttpClient, link: String): List<Group> {
    return client.get<GroupsRequest>(link).results
}