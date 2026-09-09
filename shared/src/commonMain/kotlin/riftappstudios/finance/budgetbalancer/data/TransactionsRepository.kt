package riftappstudios.finance.budgetbalancer.data

import riftappstudios.finance.budgetbalancer.data.objects.Budgeting
import riftappstudios.finance.budgetbalancer.data.objects.Categories
import riftappstudios.finance.budgetbalancer.data.objects.Transactions
import riftappstudios.finance.budgetbalancer.network.BudgetService
import riftappstudios.finance.budgetbalancer.network.StateResponse

class TransactionsRepository(
    val budgetService: BudgetService
) {

    suspend fun refresh(): Boolean {
        return when (val state = budgetService.refresh()) {
            is StateResponse.Success -> true
            is StateResponse.Error -> false
        }
    }

    suspend fun budgets(): Budgeting? {
        return when (val state = budgetService.getBudgets()) {
            is StateResponse.Success -> state.data
            is StateResponse.Error -> null
        }
    }

    suspend fun updateCategory(id: String, category: String) {
        budgetService.changeCategory(id,category)
    }

    suspend fun transactions(): Transactions? {
        return when (val state = budgetService.getTransactions()) {
                is StateResponse.Success -> state.data
                is StateResponse.Error -> null
            }
        }

    suspend fun categories(): Categories? {
        return when (val state = budgetService.getCategories()) {
            is StateResponse.Success -> state.data
            is StateResponse.Error -> null
        }
    }
}