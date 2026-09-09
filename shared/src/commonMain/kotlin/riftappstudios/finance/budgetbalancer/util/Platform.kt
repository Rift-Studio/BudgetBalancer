package riftappstudios.finance.budgetbalancer.util

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform