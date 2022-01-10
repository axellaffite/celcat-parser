package celcat

import celcat.logic.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.time.temporal.ChronoUnit
import java.util.*
import kotlin.system.exitProcess

const val eventLink = "https://edt.univ-tlse3.fr/calendar2/Home/GetCalendarData"
const val classroomsLink = "https://edt.univ-tlse3.fr/calendar2/Home/ReadResourceListItems?myResources=false&searchTerm=___&pageSize=10000&pageNumber=1&resType=102&_=1601408259546"
const val coursesLink = "https://edt.univ-tlse3.fr/calendar2/Home/ReadResourceListItems?myResources=false&searchTerm=___&pageSize=10000&pageNumber=1&resType=100&_=1601408259545"


suspend fun main(args: Array<String>) {
    args.forEach {
        check(it.matches(Regex(".*=.*"))) { "Argument $it should be specified as: name=value" }
    }

    val argsMap = args
        .map { it.split("=", limit = 2) }
        .associate { it[0] to it[1] }

    val formations = argsMap["formations"]?.split(";") ?: run {
        println("Formations should be provided with the following argument: formations=formA;formB;formC;[...]")
        exitProcess(1)
    }

    val startDate = argsMap.getDate("start")

    val endDate = argsMap.getDate("end", default = Date.from(startDate.toInstant().plus(365, ChronoUnit.DAYS)))

    val prettyJson = argsMap["pretty"]?.toBooleanStrictOrNull() ?: true

    val events = getEvents(
        client = client,
        link = eventLink,
        start = startDate,
        end = endDate,
        formations = formations,
        classes = getClassrooms(
            client = client,
            link = classroomsLink
        ).toSet(),
        courses = getCoursesNames(
            client = client,
            link = coursesLink
        )
    )

    println(Json { prettyPrint = prettyJson; encodeDefaults = true }.encodeToString(events))
}

private fun Map<String, String>.getDate(key: String, default: Date = Date()) = try {
    get(key)?.let(::parseCelcatDate) ?: default
} catch (e: Exception) {
    println("Dates should be provided with the following format: 2022-01-10T22:44:00, eg: yyyy-mm-ddThh:mm:ss")
    exitProcess(2)
}