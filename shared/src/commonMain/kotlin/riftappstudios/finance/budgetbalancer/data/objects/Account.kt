package riftappstudios.finance.budgetbalancer.data.objects

import kotlinx.serialization.Serializable

@Serializable
data class Account(
    val id: String,
    val name: String,
    val entity: String,
    val type: String,
    val balance: Float,
)