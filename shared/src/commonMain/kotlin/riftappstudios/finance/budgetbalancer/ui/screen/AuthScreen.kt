package riftappstudios.finance.budgetbalancer.ui.screen
import androidx.compose.runtime.Composable
import androidx.navigation.NavController
import riftappstudios.finance.budgetbalancer.data.UserViewModel
import riftappstudios.finance.budgetbalancer.ui.components.LoginForm

@Composable
internal fun AuthScreen(navController: NavController, userViewModel: UserViewModel) {

    // ViewModel
    LoginForm(
        onSubmit = { email, password -> userViewModel.login(email, password) },
        onCreateAccount = { email, password -> userViewModel.createUser(email, password) }
    )
}