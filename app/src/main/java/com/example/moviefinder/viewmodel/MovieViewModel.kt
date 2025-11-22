package com.example.moviefinder.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.moviefinder.data.model.Movie
import com.example.moviefinder.data.repository.MovieRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieViewModel(
    private val repository: MovieRepository = MovieRepository()
) : ViewModel() {
    private val _moviesState = MutableStateFlow<UiState<List<Movie>>>(UiState.Idle)
    val moviesState: StateFlow<UiState<List<Movie>>> = _moviesState.asStateFlow()

    init {
        loadPopularMovies()
    }

    fun loadPopularMovies() {
        viewModelScope.launch {
            _moviesState.value = UiState.Loading

            val result = repository.getPopularMovies()

            _moviesState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull() ?: emptyList())
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Unknown Error")
            }
        }
    }

    fun searchMovies(query: String) {
        if (query.isBlank()) {
            loadPopularMovies()
            return
        }

        viewModelScope.launch {
            _moviesState.value = UiState.Loading

            val result = repository.searchMovies(query = query)

            _moviesState.value = if (result.isSuccess) {
                UiState.Success(result.getOrNull() ?: emptyList())
            } else {
                UiState.Error(result.exceptionOrNull()?.message ?: "Search Failed")
            }
        }
    }
}