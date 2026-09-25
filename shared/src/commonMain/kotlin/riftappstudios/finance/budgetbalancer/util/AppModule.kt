package riftappstudios.finance.budgetbalancer.util

import io.ktor.client.*
import io.ktor.client.engine.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import riftappstudios.finance.budgetbalancer.data.BudgetViewModel
import riftappstudios.finance.budgetbalancer.data.TransactionsRepository
import riftappstudios.finance.budgetbalancer.data.UserRepository
import riftappstudios.finance.budgetbalancer.data.UserViewModel
import riftappstudios.finance.budgetbalancer.data.objects.UserSingle
import riftappstudios.finance.budgetbalancer.network.BudgetService
import riftappstudios.finance.budgetbalancer.network.UserService

// This will be implemented by each platform
expect fun getPlatformEngine(): HttpClientEngine
val appModule = module {
    // Provide HTTP Client
    single {
            HttpClient(getPlatformEngine()) { // Injects the platform's specific engine
                install(ContentNegotiation) {
                    json()
                }
            }
    }

    // Global
    singleOf(::UserSingle)

    // Provide Service
    singleOf(::BudgetService)
    singleOf(::UserService)

    // Provide Repository
    singleOf(::TransactionsRepository)
    singleOf(::UserRepository)

    // Provide ViewModel
    viewModelOf(::BudgetViewModel)
    viewModelOf(::UserViewModel)
}

fun initKoin(config: KoinAppDeclaration? = null) {
    startKoin {
        config?.invoke(this)
        modules(
            appModule
        )
    }
}
