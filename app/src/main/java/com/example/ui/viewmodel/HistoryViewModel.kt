package com.example.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.CreationEntity
import com.example.data.repository.CreationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class HistoryFilter {
    ALL,
    IMAGES,
    VIDEOS,
    FAVORITES
}

data class HistoryUiState(
    val filter: HistoryFilter = HistoryFilter.ALL,
    val searchQuery: String = "",
    val isGridView: Boolean = true
)

class HistoryViewModel(
    private val repository: CreationRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HistoryUiState())
    val uiState: StateFlow<HistoryUiState> = _uiState.asStateFlow()

    val creations: StateFlow<List<CreationEntity>> = combine(
        repository.allCreations,
        _uiState
    ) { allItems, state ->
        val filteredByType = when (state.filter) {
            HistoryFilter.ALL -> allItems
            HistoryFilter.IMAGES -> allItems.filter { it.type == "IMAGE" }
            HistoryFilter.VIDEOS -> allItems.filter { it.type == "VIDEO" }
            HistoryFilter.FAVORITES -> allItems.filter { it.isFavorite }
        }

        if (state.searchQuery.isBlank()) {
            filteredByType
        } else {
            val q = state.searchQuery.trim().lowercase()
            filteredByType.filter {
                it.prompt.lowercase().contains(q) ||
                it.title.lowercase().contains(q) ||
                it.modelEngine.lowercase().contains(q) ||
                it.tags.lowercase().contains(q)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun setFilter(filter: HistoryFilter) {
        _uiState.update { it.copy(filter = filter) }
    }

    fun onSearchQueryChange(query: String) {
        _uiState.update { it.copy(searchQuery = query) }
    }

    fun toggleViewMode() {
        _uiState.update { it.copy(isGridView = !it.isGridView) }
    }

    fun toggleFavorite(item: CreationEntity) {
        viewModelScope.launch {
            repository.toggleFavorite(item.id, !item.isFavorite)
        }
    }

    fun deleteItem(id: Long) {
        viewModelScope.launch {
            repository.deleteCreation(id)
        }
    }

    fun clearAll() {
        viewModelScope.launch {
            repository.clearHistory()
        }
    }

    fun restoreSamples() {
        viewModelScope.launch {
            repository.restoreSamples()
        }
    }
}
