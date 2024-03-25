package com.example.whattodo.domain.models.serializers

import com.example.whattodo.domain.models.task.item.TaskPriority
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind.INT
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

object TaskPrioritySerializer : KSerializer<TaskPriority> {
    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor(serialName = "Priority", kind = INT)

    override fun deserialize(decoder: Decoder): TaskPriority {
        val value = decoder.decodeInt()
        return TaskPriority.fromInt(value)
    }

    override fun serialize(
        encoder: Encoder,
        value: TaskPriority,
    ) {
        encoder.encodeInt(value.priorityAsInt)
    }
}