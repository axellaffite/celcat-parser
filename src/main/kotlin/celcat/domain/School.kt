package celcat.domain

import kotlinx.serialization.Serializable

/**
 * Represents a School by its name and [information][Info].
 *
 * @property name
 * @property info
 */
@Serializable
data class School(
    val name: String,
    val info: List<Info>
)