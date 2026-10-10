package riftappstudios.finance.budgetbalancer.ui.screen
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import riftappstudios.finance.budgetbalancer.data.BudgetViewModel
import riftappstudios.finance.budgetbalancer.data.objects.Account
import riftappstudios.finance.budgetbalancer.ui.components.BalanceHeader

@Composable
internal fun BalanceScreen(
    navController: NavController,
    transactionsViewModel: BudgetViewModel,
    onAccountClick: (Account) -> Unit = {}
) {
    val accounts = transactionsViewModel.accountsState.collectAsState()
    val totalDebt = accounts.value.accounts.filter{it.type == "Debt"}.sumOf {
        it.balance.toDouble()
    }.toFloat()
    val totalAsset = accounts.value.accounts.filter{it.type == "Deposit"}.sumOf {
        it.balance.toDouble()
    }.toFloat()

    val totalBalance = totalAsset - totalDebt

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(16.dp)
    ) {
        Text(
            text = "Accounts",
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
        )

        Spacer(Modifier.height(20.dp))

        FlowRow(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            BalanceHeader("Deposit", totalAsset, accounts.value.accounts.filter { it.type == "Deposit" })
            BalanceHeader("Debt Short-Term", totalDebt, accounts.value.accounts.filter { it.type == "Debt" })
            BalanceHeader("Net", totalBalance, accounts.value.accounts)

            BalanceHeader("Start of next month (remain inc and spend - budget", totalBalance+2700-1700-5400, emptyList())
            BalanceHeader("Start of next month (remain inc and spend - trend", totalBalance+2700-4600-7500, emptyList())

        }


        Spacer(Modifier.height(24.dp))

        Text(
            text = "Your accounts",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.SemiBold,
        )

        Spacer(Modifier.height(12.dp))

        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(bottom = 24.dp),
            modifier = Modifier.fillMaxWidth(),
        ) {
            items(
                items = accounts.value.accounts.sortedBy { it.balance },
                key = { it.id }
            ) { account ->
                AccountRow(
                    account = account,
                    onClick = { onAccountClick(account) },
                )
            }
        }
    }
}

@Composable
fun AccountRow(
    account: Account,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        ),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Surface(
                shape = CircleShape,
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.size(48.dp),
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = account.name.firstOrNull()
                            ?.uppercase() ?: "?",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                    )
                }
            }

            Spacer(Modifier.width(12.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(3.dp))

                Text(
                    text = account.entity,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )

                Spacer(Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(6.dp),
                    color = MaterialTheme.colorScheme.tertiaryContainer,
                ) {
                    Text(
                        text = account.type,
                        modifier = Modifier.padding(
                            horizontal = 8.dp,
                            vertical = 4.dp,
                        ),
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            }

            Spacer(Modifier.width(8.dp))

            Text(
                text = formatBalance(account.balance),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (account.balance < 0f) {
                    MaterialTheme.colorScheme.error
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
            )
        }
    }
}

/**
 * Common Kotlin implementation without java.text.NumberFormat.
 * Example: 1234567.89 -> $1,234,567.89
 */
fun formatBalance(balance: Float): String {
    val cents = (balance.toDouble() * 100)
        .let { kotlin.math.round(it).toLong() }

    val negative = cents < 0
    val absoluteCents = if (negative) -cents else cents
    val whole = absoluteCents / 100
    val fraction = absoluteCents % 100

    val digits = whole.toString()
    val grouped = buildString {
        digits.forEachIndexed { index, char ->
            if (index > 0 && (digits.length - index) % 3 == 0) {
                append(',')
            }
            append(char)
        }
    }

    return "${if (negative) "-$" else "$"}$grouped." +
            fraction.toString().padStart(2, '0')
}
