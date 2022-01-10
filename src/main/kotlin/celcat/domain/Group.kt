package celcat.domain

import kotlinx.serialization.Serializable

/**
 * Represent a faculty group.
 *
 * @property id The group id
 * @property text The textual representation
 */
@Serializable
data class Group (
    val id: String,
    val text: String
)