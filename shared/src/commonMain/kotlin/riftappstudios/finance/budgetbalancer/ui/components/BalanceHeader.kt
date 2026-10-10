package riftappstudios.finance.budgetbalancer.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import riftappstudios.finance.budgetbalancer.data.objects.Account
import riftappstudios.finance.budgetbalancer.ui.screen.formatBalance

@Composable
fun BalanceHeader(
    name: String,
    balance: Float,
    accounts: List<Account>,
    ) {

    Card(
//    modifier = Modifier.fillMaxWidth(),
    shape = RoundedCornerShape(20.dp),
    colors = CardDefaults.cardColors(
    containerColor = MaterialTheme.colorScheme.primaryContainer
    )
    ) {
        Column(Modifier.padding(20.dp)) {
            Text(
                text = name,
                style = MaterialTheme.typography.titleMedium,
            )

            Spacer(Modifier.height(6.dp))

            Text(
                text = formatBalance(balance),
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
            )

            Spacer(Modifier.height(4.dp))

            Text(
                text = "${accounts.size} accounts",
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}