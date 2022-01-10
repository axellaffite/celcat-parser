package celcat.logic

import celcat.domain.Event
import celcat.domain.ParsedDescription

/**
 * @param description The description to parse
 * @param classesNames All the classes names that exists (otherwise classes will always be empty)
 * @param coursesNames All the courses names that exists (otherwise course will always be null)
 */
fun parseDescriptionFromCelcat(
    category: String?,
    description: String?,
    classesNames: Set<String>,
    coursesNames: Map<String, String>
): ParsedDescription {
    val regex = Regex(".*(\\[\\w+\\])")

    println("Description: $description")
    println("Unescaped: ${description?.unescapeHtml()}")

    val classes = mutableListOf<String>()
    var course: String? = null
    var teacherID: Int? = null
    val precisions: String?

    val precisionBuilder = StringBuilder()
    if (description != null) {
        val trimmedLinesWithoutSuffixes = description.lines()
            .map(String::trim)
            .filter { it.isNotBlank() }
            .map { line ->
                regex.find(line)?.groups?.get(1)?.let { module ->
                    line.removeSuffix(module.value).trim()
                } ?: line
            }

        trimmedLinesWithoutSuffixes.forEach { line ->
            when (line) {
                in classesNames -> classes.add(line)
                in coursesNames -> course = coursesNames[line]
                category -> { /* ignore line */ }

                else -> when {
                    line.isDigitsOnly() -> teacherID = line.toInt()
                    precisionBuilder.isBlank() -> precisionBuilder.append(line)
                    else -> precisionBuilder.append("\n").append(line)
                }
            }
        }
    }

    precisions = precisionBuilder.takeIf { it.isNotBlank() }?.toString()
    course = cleanCourseName(course)

    return ParsedDescription(classes, course, teacherID, precisions)
}

private fun String.isDigitsOnly() = matches(Regex("\\d+"))

/**
 * @return The real course name without the boilerplate
 * if it has been guessed during the [event][Event] parsing,
 * or the course type.
 */
private fun cleanCourseName(course: String?): String? = course?.let {
    val regex = Regex("(\\w+\\s-) (.+)")
    val match = regex.find(course)
    return match?.let { it.groups[2]?.value }
}