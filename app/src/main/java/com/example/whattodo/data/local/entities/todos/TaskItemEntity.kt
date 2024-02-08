package com.example.whattodo.data.local.entities.todos

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskItemEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val content: String,
    @ColumnInfo(name = "parent_list_id") val parentListId: Long,
    @ColumnInfo(name = "is_done") val isDone: Boolean,
    @ColumnInfo(name = "created_at") val createdAt: String,
    @ColumnInfo(name = "valid_until") val validUntil: String,
    val priority: Int,
)
