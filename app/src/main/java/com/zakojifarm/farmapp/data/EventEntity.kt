package com.zakojifarm.farmapp.data

import androidx.room.*
import java.time.LocalDateTime

@Entity(
    tableName = EventDatabase.DB_NAME,
    foreignKeys = [ForeignKey(
        entity = UserEntity::class,
        parentColumns = ["id"],
        childColumns = ["user_id"],
        onDelete = ForeignKey.CASCADE
    )]
)
@TypeConverters(
    LocalDateTimeConverters::class,
    EventKindConverters::class,
    WorkKindConverters::class
)
data class EventEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "event_id") var id: Long,
    val time: LocalDateTime,
    val kind: EventKind,
    @ColumnInfo(name = "work_kind") val workKind: WorkKind,
    @ColumnInfo(name = "user_id", index = true) var userId: Long
) {
    companion object {
        fun create(time: LocalDateTime, kind: EventKind, workKind: WorkKind): EventEntity {
            return EventEntity(0, time, kind, workKind, 0)
        }

        fun create(kind: EventKind, workKind: WorkKind): EventEntity {
            return create(LocalDateTime.now(), kind, workKind)
        }
    }
}
