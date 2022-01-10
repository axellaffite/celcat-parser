package celcat.domain

import kotlinx.serialization.Serializable

/**
 * Represent a [school][School] information.
 *
 * @property name The faculty name
 * @property url The faculty schedule url
 * @property groups The link to get all the groups
 * @property rooms The link to get all the rooms
 * @property courses The link to get all the courses
 */
@Serializable
data class Info (
    val name: String,
    val url: String,
    val groups: String,
    val rooms: String,
    val courses: String
)