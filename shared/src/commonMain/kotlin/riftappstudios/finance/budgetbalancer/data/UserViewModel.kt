package riftappstudios.finance.budgetbalancer.data

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import riftappstudios.finance.budgetbalancer.data.objects.User
import riftappstudios.finance.budgetbalancer.data.objects.UserSingle

class UserViewModel(
        val userRepository: UserRepository,
        val userSingle: UserSingle
) : ViewModel() {

        fun login(email: String, password: String) {
                viewModelScope.launch(Dispatchers.IO) {
                        userSingle.user = User(userRepository.login(email, password))
                }
        }

        fun createUser(email: String, password: String) {
                viewModelScope.launch(Dispatchers.IO) {
                        userSingle.user = User(userRepository.createUser(email, password))
                }
        }
}
