package celcat.domain

import kotlinx.serialization.Serializable

@Serializable
data class ParsedDescription(
    val classes: List<String>,
    val course: String? = null,
    val teacherID: Int? = null,
    val precisions: String? = null
)