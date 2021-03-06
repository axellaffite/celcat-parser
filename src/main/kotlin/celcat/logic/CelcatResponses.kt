package celcat.logic

import celcat.domain.Group
import celcat.domain.School
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.builtins.MapSerializer
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


@Serializable
data class CoursesRequest(
    @Serializable(with = CourseSerializer::class)
    val results: Map<String, String>
)

@Serializable
data class GroupsRequest(val results: List<Group>)

@Serializable
data class ClassroomsRequest(
    @Serializable(with = ClassSerializer::class)
    val results: List<String>
)


object CourseSerializer: KSerializer<Map<String, String>> {

    @Serializable
    private data class JsonCourse(val id: String, val text: String)

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Course", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): Map<String, String> {
        val serializer = ListSerializer(JsonCourse.serializer())

        return decoder
            .decodeSerializableValue(serializer)
            .fold(listOf<Pair<String, String>>()) { acc, jsonCourse ->
                acc + (jsonCourse.id to jsonCourse.text) + (jsonCourse.text to jsonCourse.text)
            }.toMap()
    }

    override fun serialize(encoder: Encoder, value: Map<String, String>) {
        encoder.encodeSerializableValue(MapSerializer(String.serializer(), String.serializer()), value)
    }
}


object ClassSerializer: KSerializer<List<String>> {

    @Serializable
    private data class JsonClass(val id: String)

    override val descriptor: SerialDescriptor
        get() = PrimitiveSerialDescriptor("Class", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): List<String> {
        val serializer = ListSerializer(JsonClass.serializer())

        return decoder.decodeSerializableValue(serializer).map { it.id.unescapeHtml().trim() }
    }

    override fun serialize(encoder: Encoder, value: List<String>) {
        encoder.encodeSerializableValue(ListSerializer(String.serializer()), value  )
    }

}