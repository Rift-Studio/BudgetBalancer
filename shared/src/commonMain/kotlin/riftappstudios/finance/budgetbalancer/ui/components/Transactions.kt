package riftappstudios.finance.budgetbalancer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import riftappstudios.finance.budgetbalancer.data.objects.Transaction
import riftappstudios.finance.budgetbalancer.util.asString
import riftappstudios.finance.budgetbalancer.util.getColor
import riftappstudios.finance.budgetbalancer.util.toFinance
import kotlin.math.abs

@Composable
fun TransactionHistory(
    transactions: List<Transaction>,
    categories: List<String>,
    modifier: Modifier = Modifier,
    updateCategory: (String, String) -> Unit
) {
    val listState = rememberLazyListState()

    // Keying this effect to 'transactions' means it fires
    // EVERY time the data list changes its contents or order
    LaunchedEffect(key1 = transactions) {
        // Option A: Instantly snap back to the very top
        listState.scrollToItem(index = 0)

        // Option B: Smoothly glide back to the top (uncomment if preferred)
        // listState.animateScrollToItem(index = 0)
    }

    LazyColumn(
        modifier = modifier.fillMaxSize(),
        state = listState,
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        // uniquely identify rows using the transaction id for smooth animations
        items(
            items = transactions,
//            key = { transaction -> transaction.id }
        ) { transaction ->
            TransactionItem(transaction = transaction, categories = categories) {id, category ->
                updateCategory(id, category)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun TransactionItem(
    transaction: Transaction,
    categories: List<String>,
    modifier: Modifier = Modifier,
    updateCategory: (String, String) -> Unit
) {
    // Dynamic color indicator based on transaction category
    val amountColor = getColor(transaction.account, transactionType = transaction.type, transaction.amount)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp),

        ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Left Content: Date, Description, and Account
            Column(
                modifier = Modifier.weight(.3f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "${transaction.date.asString()} - ${transaction.account}",
                        // style = MaterialTheme.typography.caption,
                        // color = Color.Gray
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                }
                Text(
                    text = "${transaction.description} [${transaction.originalDescription}]",
//                        text = transaction.account.uppercase(),
                    // style = MaterialTheme.typography.caption,
                     fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    // color = MaterialTheme.colors.primary,
//                    modifier = Modifier
//                        // .background(MaterialTheme.colors.primary.copy(alpha = 0.1f), RoundedCornerShape(4.dp))
//                        .padding(horizontal = 6.dp, vertical = 2.dp)
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = transaction.category,
                        color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(transaction.category.toStableColor())
                            .padding(2.dp),
                        // style = MaterialTheme.typography.body1,
                        // fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = transaction.originalCategory,
                        color = Color.White,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(transaction.originalCategory.toStableColor())
                            .padding(2.dp),
                        // style = MaterialTheme.typography.body1,
                        // fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            // Right Content: Amount
            Column(modifier = Modifier.weight(.1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center) {
            Text(
                text = transaction.amount.toFinance(),
                fontWeight = FontWeight.Bold,
                // style = MaterialTheme.typography.body1,
                // fontWeight = FontWeight.Bold,
                color = amountColor
            )
            }
            FlowRow(
                modifier = Modifier.weight(1f),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                categories.forEach {

                    Button(onClick = {
                        updateCategory(transaction.id,it)
                    },
                        shape = ButtonDefaults.extraSmallPressedShape,
                        colors = ButtonDefaults.buttonColors(
                            containerColor = it.toStableColor()
                        )
                        ) {
                        Text(it)
                    }
                }
            }
        }
    }
}


@Composable
@Preview
fun Transactions() {
    MaterialTheme {

        LazyColumn(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primaryContainer)
                .safeContentPadding()
                .fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

        }
    }
}


fun String.toStableColor(): Color {
    val hash = abs(this.hashCode())
    val hue = (hash % 360).toFloat()
    val saturation = 0.60f
    val lightness = 0.50f

    return Color.hsl(hue, saturation, lightness)
}