package com.example.whattodo.data.local.entities.tasks

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TaskListEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val title: String,
    val isActive: Boolean,
)