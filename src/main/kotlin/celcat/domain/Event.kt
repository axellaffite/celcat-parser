package celcat.domain

import celcat.logic.parseCelcatDate
import celcat.logic.toCelcatDateTime
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import java.util.*

@Serializable
data class Event(
    val id: String,
    val category: String?,
    val description: String?,
    val courseName: String?,
    val locations: List<String>,
    val sites: List<String>,
    @Serializable(with = DateSerializer::class)
    val start: Date,
    @Serializable(with = DateSerializer::class)
    val end: Date?,
    val allday: Boolean,
    val backgroundColor: String?,
    val textColor: String?,
    val noteID: Long?
)

object DateSerializer: KSerializer<Date> {
    override val descriptor = PrimitiveSerialDescriptor("Date", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder) = parseCelcatDate(decoder.decodeString())

    override fun serialize(encoder: Encoder, value: Date) = encoder.encodeString(value.toCelcatDateTime())

}