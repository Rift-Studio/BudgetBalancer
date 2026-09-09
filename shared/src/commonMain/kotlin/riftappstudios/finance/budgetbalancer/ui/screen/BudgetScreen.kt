package riftappstudios.finance.budgetbalancer.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import kotlinx.datetime.LocalDate
import riftappstudios.finance.budgetbalancer.data.BudgetViewModel
import riftappstudios.finance.budgetbalancer.data.objects.*
import riftappstudios.finance.budgetbalancer.ui.components.ComposeTextFilterDropdown
import riftappstudios.finance.budgetbalancer.ui.components.DateRangeInputField
import riftappstudios.finance.budgetbalancer.ui.components.Header
import riftappstudios.finance.budgetbalancer.ui.components.TransactionHistory

@Composable
internal fun BudgetScreen(navController: NavController, viewModel: BudgetViewModel) {
    // ViewModel

    // Transactions
    val transactions: State<Transactions> = viewModel.transactionsState.collectAsState()
    val categories: State<Categories> = viewModel.categoriesState.collectAsState()
    val budgeting: State<Budgeting> = viewModel.budgets.collectAsState()
    // Date
    var startRange by remember { mutableStateOf(LocalDate(2026, 8, 1)) }
    var endRange by remember { mutableStateOf<LocalDate>(LocalDate(2026,8,30)) }
    // Category
    var dateRange: Pair<LocalDate, LocalDate> by remember { mutableStateOf(startRange to endRange) }
    var categoryFilter by remember {mutableStateOf<String?>(null)}
    var originalCategoryFilter by remember {mutableStateOf<String?>(null)}
    val originalCategories = remember { derivedStateOf {
        transactions.value.rows.map { it.originalCategory }.toSet()
    }}
    // Vendors
    val vendors = remember { derivedStateOf {
        transactions.value.rows.map { it.description }.toSet()
    } }
    var vendorFilter by remember {mutableStateOf<String?>(null)}

    // Budgets
    val budgets: Budgets = mutableMapOf<String, Float>()

    val budgetTotal: Int = viewModel.budgetTotal
    val processedTransactions = transactions.value.rows
        // Date
        .asSequence()
        .filter { it.date in dateRange.first..dateRange.second }
        // Category
        .filter { categoryFilter == it.category || categoryFilter == null }
        .filter { originalCategoryFilter == it.originalCategory || originalCategoryFilter == null }
        .filter { vendorFilter == it.description || vendorFilter == null }
        .sortedWith(
            compareByDescending<Transaction> { it.category == "Unknown" }
        )
        .toList()

    processedTransactions.forEach { txn ->
        val current = budgets[txn.category]
        // Credit - is good + bad
        budgets[txn.category] =
            (current ?: 0f) + when (txn.type) {
                "Debit" -> {
                    txn.amount
                }

                "Credit" -> {
                    txn.amount.times(-1f)
                }

                else -> {
                    777f
                }
            }
        // Debit - is bad _ good

    }

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.primaryContainer)
            .safeContentPadding()
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Header(budgets = budgets, budgeting = budgeting.value, budgetTotal = budgetTotal)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = {viewModel.refresh()}) {
                    Text("Refresh")
                }
                ComposeTextFilterDropdown(
                    options = categories.value.rows.sorted(),
                    label = "Category"
                ) { option ->
                    categoryFilter = option
                }
                ComposeTextFilterDropdown(
                    options = originalCategories.value.sorted(),
                    label = "Original"
                ) { option ->
                    originalCategoryFilter = option
                }
                ComposeTextFilterDropdown(
                    options = vendors.value.sorted(),
                    label = "Vendor"
                ) { option ->
                    vendorFilter = option
                }
                DateRangeInputField(
                    startDate = startRange,
                    endDate = endRange,
                    onStartDateChanged = { startRange = it },
                    onEndDateChanged = { endRange = it }
                ) {
                    dateRange = startRange to endRange
                }
            }
            TransactionHistory(processedTransactions, categories.value) { id, category ->
                viewModel.updateTransactionCategory(
                    id, category
                )
            }
        }
    }
}