package com.example.whattodo.domain.models

sealed class SortBy {
    data object TitleAscending : SortBy()
    data object TitleDescending : SortBy()
    data object CreationDateAscending : SortBy()
    data object CreationDateDescending : SortBy()
    data object ValidDateAscending : SortBy()
    data object ValidDateDescending : SortBy()
    data object PriorityAscending : SortBy()
    data object PriorityDescending : SortBy()
}
