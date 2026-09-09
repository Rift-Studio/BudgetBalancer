package riftappstudios.finance.budgetbalancer

import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import riftappstudios.finance.budgetbalancer.ui.App
import riftappstudios.finance.budgetbalancer.util.initKoin


fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        state = rememberWindowState(
            size = DpSize(1600.dp, 1200.dp)
        ),
        title = "BudgetBalancer",
    ) {
        initKoin()
        App()
    }
}

