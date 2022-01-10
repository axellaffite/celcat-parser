package celcat.logic

import celcat.domain.Event
import kotlinx.serialization.json.*
import java.util.*

fun parseEventsFromJson(obj: JsonObject, classes: Set<String>, courses: Map<String, String>) = obj.run {
    val category = getCategory()
    val parsedDescription = parseDescription(category, classes, courses)
    val start = getStartDate()
    val end = getEndDate(start)
    val sites = getSites()

    Event(
        id = stringFromHtml("id"),
        category = category,
        description = parsedDescription.precisions,
        courseName = parsedDescription.course,
        locations = parsedDescription.classes,
        sites = sites.sorted(),
        start = start,
        end = end,
        allday = isAllDay(),
        backgroundColor = getBackgroundColor(),
        textColor = getTextColor(),
        noteID = null
    )
}


private fun JsonObject.getCategory() = nullableStringFromHtml("eventCategory")

private fun JsonObject.parseDescription(
    category: String?,
    classes: Set<String>,
    courses: Map<String, String>
) = parseDescriptionFromCelcat(
    category = category,
    description = nullableStringFromHtml("description"),
    classesNames = classes,
    coursesNames = courses
)

private fun JsonObject.getStartDate(): Date {
    return parseCelcatDate(getValue("start").jsonPrimitive.content)
}

private fun JsonObject.getEndDate(start: Date): Date {
    return nullableStringFromHtml("end")?.let(::parseCelcatDate) ?: start
}

private fun JsonObject.getSites(): List<String> {
    return get("sites")?.takeIf { it is JsonArray }?.run {
        jsonArray.filterIsInstance<JsonPrimitive>()
            .map { it.content.unescapeHtml().trim() }
    } ?: emptyList()
}

private fun JsonObject.isAllDay() = (get("allDay")?.jsonPrimitive?.booleanOrNull == true || get("end") == null)

private fun JsonObject.getBackgroundColor() = stringFromHtml("backgroundColor")

private fun JsonObject.getTextColor() = nullableStringFromHtml("textColor") ?: "#000000"

private fun JsonObject.stringFromHtml(key: String) = requireNotNull(nullableStringFromHtml(key))

private fun JsonObject.nullableStringFromHtml(key: String): String? {
    return get(key)?.jsonPrimitive?.contentOrNull?.unescapeHtml()
}