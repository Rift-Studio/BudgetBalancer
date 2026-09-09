package riftappstudios.finance.budgetbalancer.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import riftappstudios.finance.budgetbalancer.data.objects.Budgeting
import riftappstudios.finance.budgetbalancer.data.objects.Categories
import riftappstudios.finance.budgetbalancer.data.objects.Transactions

class BudgetViewModel(
    val transactionsRepository: TransactionsRepository
) : ViewModel() {

    // Read-only public state exposed to the UI
    private val _transactionsState = MutableStateFlow(Transactions(emptyList()))
    val transactionsState: StateFlow<Transactions> = _transactionsState.asStateFlow()
    private val _categoriesState = MutableStateFlow(Categories(emptyList()))
    val categoriesState: StateFlow<Categories> = _categoriesState.asStateFlow()

    private val _budgets = MutableStateFlow(Budgeting(emptyList()))
    val budgets: StateFlow<Budgeting> = _budgets.asStateFlow()
    val budgetTotal: Int
        get() = budgets.value.rows.filter { it.budget != "Income" }.sumOf { budget -> budget.target }


    init {
        viewModelScope.launch {
            _transactionsState.update { transactionsRepository.transactions() ?: Transactions(emptyList())}
            _categoriesState.update { transactionsRepository.categories() ?: Categories(emptyList())}
            _budgets.update { transactionsRepository.budgets() ?: Budgeting(emptyList()) }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            transactionsRepository.refresh().also { if (it) {
                _transactionsState.update { transactionsRepository.transactions() ?: Transactions(emptyList()) }
            } }
        }
    }

    fun updateTransactionCategory(id: String, category: String) {
        val newList = _transactionsState.value.rows.toMutableList()
        _transactionsState.value.rows.indexOfFirst { it.id == id }.let {
            newList.set(it, _transactionsState.value.rows[it].copy(category = category))
        }
        _transactionsState.update { Transactions(newList.toList()) }

        viewModelScope.launch {
            transactionsRepository.updateCategory(id,category)
        }
    }
}
