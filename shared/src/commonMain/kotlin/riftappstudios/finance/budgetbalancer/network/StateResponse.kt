package riftappstudios.finance.budgetbalancer.network

sealed interface StateResponse<out T> {

    data class Success<T>(val data: T) : StateResponse<T>
    data class Error(val e: Exception) : StateResponse<Nothing>
}