package riftappstudios.finance.budgetbalancer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import kotlinx.serialization.Serializable
import org.koin.compose.viewmodel.koinViewModel
import riftappstudios.finance.budgetbalancer.data.BudgetViewModel
import riftappstudios.finance.budgetbalancer.ui.screen.BudgetScreen
import riftappstudios.finance.budgetbalancer.ui.screen.CalendarScreen

// 1. Define pure Kotlin targets/routes
@kotlinx.serialization.Serializable
object HomeScreen
@Serializable
object BudgetScreen

@Serializable
object CalendarScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: BudgetViewModel = koinViewModel()


    Row(modifier = Modifier.fillMaxSize()) {

        NavigationRail(navController, Modifier.weight(.05f))
        // 2. Set up the multiplatform NavHost
        NavHost(
            modifier = Modifier.weight(.9f),
            navController = navController,
            startDestination = BudgetScreen
        ) {
            composable<HomeScreen> {
                Text("HOME")
            }
            composable<BudgetScreen> { backStackEntry ->
                BudgetScreen(navController, viewModel)
            }
            composable<CalendarScreen> { backStackEntry ->
                CalendarScreen(navController, viewModel)
            }
        }
    }
}

@Composable
fun NavigationRail(navController: NavController, modifier: Modifier) {
    Column(modifier = modifier.fillMaxHeight().background(MaterialTheme.colorScheme.tertiaryContainer),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Button(onClick = { navController.navigate(BudgetScreen) }) {
            Text("Budget")
        }
        Button(onClick = { navController.navigate(CalendarScreen) }) {
            Text("Calendar")
        }
    }
}