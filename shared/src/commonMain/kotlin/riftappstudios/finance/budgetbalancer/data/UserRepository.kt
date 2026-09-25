package riftappstudios.finance.budgetbalancer.data

import riftappstudios.finance.budgetbalancer.network.StateResponse
import riftappstudios.finance.budgetbalancer.network.UserService

class UserRepository(
    val userService: UserService
) {
    suspend fun login(email: String, password: String): String {
        return when (val state = userService.login(email, password)) {
            is StateResponse.Success -> state.data
            is StateResponse.Error -> "Error Logging for $email"
        }
    }

    suspend fun createUser(email: String, password: String): String {
        return when (val state = userService.createUser(email, password)) {
            is StateResponse.Success -> state.data
            is StateResponse.Error -> "Error Logging for $email"
        }
    }
}