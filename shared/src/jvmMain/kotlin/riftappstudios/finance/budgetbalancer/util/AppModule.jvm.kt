package riftappstudios.finance.budgetbalancer.util

import io.ktor.client.engine.*
import io.ktor.client.engine.cio.*

actual fun getPlatformEngine(): HttpClientEngine = CIO.create()