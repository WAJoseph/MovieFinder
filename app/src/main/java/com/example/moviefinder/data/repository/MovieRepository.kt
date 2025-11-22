package com.example.moviefinder.data.repository

import com.example.moviefinder.data.model.Movie
import com.example.moviefinder.data.remote.RetrofitInstance
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers

class MovieRepository {

    private val api = RetrofitInstance.api
    private val apiKey = RetrofitInstance.API_KEY

    suspend fun getPopularMovies(): Result<List<Movie>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getPopularMovies(apiKey = apiKey)
                Result.success(response.results)
            } catch (e: Exception) {
                Result.failure(e)
            }
        }
    }

    suspend fun searchMovies(query: String): Result<List<Movie>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.searchMovies(apiKey = apiKey, query = query)
                Result.success(response.results)
            } catch(e: Exception) {
                Result.failure(e)
            }
        }
    }
}