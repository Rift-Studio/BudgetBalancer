package riftappstudios.finance.budgetbalancer.data.objects

import kotlinx.serialization.Serializable

@Serializable
data class User (
    val userId: String? = null,
)

class UserSingle() {
    var user: User? = null
}