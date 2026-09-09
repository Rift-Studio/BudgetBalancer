package riftappstudios.finance.budgetbalancer.data.objects

import kotlinx.serialization.Serializable

@Serializable
data class Categories(
    val rows: List<String>
)

typealias Budgets = MutableMap<String,Float>

@Serializable
data class Budgeting(
    val rows: List<IndividualBudget>
)

@Serializable
data class IndividualBudget(
    val budget: String,
    val target: Int
)