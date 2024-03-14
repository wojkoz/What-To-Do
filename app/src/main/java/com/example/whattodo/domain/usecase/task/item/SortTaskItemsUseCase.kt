package com.example.whattodo.domain.usecase.task.item

import com.example.whattodo.domain.models.SortBy
import com.example.whattodo.domain.models.SortBy.CreationDateAscending
import com.example.whattodo.domain.models.SortBy.CreationDateDescending
import com.example.whattodo.domain.models.SortBy.ValidDateAscending
import com.example.whattodo.domain.models.SortBy.ValidDateDescending
import com.example.whattodo.domain.models.task.item.TaskItem

class SortTaskItemsUseCase {
    operator fun invoke(
        taskItemList: List<TaskItem>,
        sortBy: SortBy = CreationDateDescending,
    ): List<TaskItem> {
        return when (sortBy) {
            CreationDateAscending -> taskItemList.sortedBy { it.createdAt }
            CreationDateDescending -> taskItemList.sortedByDescending { it.createdAt }
            ValidDateAscending -> taskItemList.sortedBy { it.validUntil }
            ValidDateDescending -> taskItemList.sortedByDescending { it.validUntil }
        }
    }
}