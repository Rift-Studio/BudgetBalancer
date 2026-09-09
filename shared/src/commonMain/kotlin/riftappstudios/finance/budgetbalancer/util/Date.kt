package riftappstudios.finance.budgetbalancer.util

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import kotlinx.datetime.format.char
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

// 1. Define the MM/dd/yyyy blueprint
val customDateFormat = LocalDate.Format {
    monthNumber()   // MM
    char('/')
    dayOfMonth()   // dd
    char('/')
    year()          // yyyy
}

fun String.toDate(): LocalDate {
    return LocalDate.parse(this,customDateFormat)
}

fun LocalDate.asString(): String {
    return this.format(customDateFormat)
}

object CustomLocalDateSerializer : KSerializer<LocalDate> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("LocalDate", PrimitiveKind.STRING)

    // Transforms incoming JSON String -> LocalDate object
    override fun deserialize(decoder: Decoder): LocalDate {
        val string = decoder.decodeString()
        return LocalDate.parse(string, customDateFormat)
    }

    // Transforms outgoing LocalDate object -> JSON String
    override fun serialize(encoder: Encoder, value: LocalDate) {
        encoder.encodeString(value.format(customDateFormat))
    }
}