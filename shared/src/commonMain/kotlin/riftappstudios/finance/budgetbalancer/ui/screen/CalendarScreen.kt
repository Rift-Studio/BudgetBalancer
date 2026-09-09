package riftappstudios.finance.budgetbalancer.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.navigation.NavController
import riftappstudios.finance.budgetbalancer.data.BudgetViewModel
import riftappstudios.finance.budgetbalancer.data.objects.Transactions
import riftappstudios.finance.budgetbalancer.ui.components.Calendar
import riftappstudios.finance.budgetbalancer.ui.components.toDateMap

@Composable
internal fun CalendarScreen(navController: NavController, viewModel: BudgetViewModel) {
    // ViewModel
    val transactions: State<Transactions> = viewModel.transactionsState.collectAsState()

    Calendar(transactions.value.toDateMap())
}