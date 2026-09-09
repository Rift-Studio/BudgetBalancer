package riftappstudios.finance.budgetbalancer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import riftappstudios.finance.budgetbalancer.data.objects.Budgeting
import riftappstudios.finance.budgetbalancer.data.objects.Budgets

fun isBudget(budget: String):Boolean {
    return when (budget) {
        "Income","Transfer","CC Payment", "CC Settlement" -> false
        else -> true
    }
}
@Composable
fun Header(
    modifier: Modifier = Modifier,
    budgets: Budgets,
    budgeting: Budgeting,
    budgetTotal: Int,
) {

    val budgetUsed = budgets.filter { isBudget(it.key) }.let {
        var total = 0f
        for (value in it.values) {
            total += value
        }
        return@let total
    }

    // budgeting = target value
    // budgets = actual value
    var over = 0f
    var under = 0f
        budgeting.rows.filter { it.budget != "Contracts"
                && it.budget != "Bills"
                && it.budget != "Subscriptions"}.forEach { budget ->
                    println("JORDAN - Budget: ${budget.budget}")
            budgets[budget.budget]?.let {

                    if (it < budget.target) {
                        under += (budget.target - it)
                    } else if (it > budget.target) {
                        over += (it - budget.target)
                    }

            }
        }
    val overUnder: Pair<Float,Float> = over to under


    Row(modifier = modifier.fillMaxWidth()) {
        FlowRow(modifier = modifier.weight(.9f)) {
            budgets.forEach { action ->
                val budgetName = action.key

                val annotatedString = buildAnnotatedString {
                    append("$budgetName: ") // Regular text

                    // This block bolds only the username portion
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(action.value.toFinance())
                        append("/")
                        append(budgeting.rows.find { it.budget == action.key }?.target?.toFinance())
                    }
                }

                Text(
                    text = annotatedString,
                    color = Color.White,
                    modifier = modifier
                        .clip(RoundedCornerShape(8.dp)) // Gives smooth, rounded corners
                        .background(budgetName.toStableColor())
                        .padding(horizontal = 12.dp, vertical = 6.dp) // Space inside the background)
                )
            }
        }
        Row(modifier = modifier.weight(.1f), horizontalArrangement = Arrangement.SpaceBetween) {
            Column(modifier = modifier.weight(.33f)) {
                Text(overUnder.first.toFinance())
                Text("-----")
                Text(overUnder.second.toFinance(), style = MaterialTheme.typography.bodyLargeEmphasized)
            }
            Column(modifier = modifier.weight(.33f)) {
                Text(budgetUsed.toFinance())
                Text("-----")
                Text(budgetTotal.toFinance(), style = MaterialTheme.typography.bodyLargeEmphasized)
            }
            Column(modifier = modifier.weight(.33f)) {
                Text(budgets["Income"]?.toFinance() ?: "$0.00")
                Text("-----")
                Text(budgeting.rows.find { it.budget == "Income" }?.target?.toFinance() ?: "$0.00", style = MaterialTheme.typography.bodyLargeEmphasized)
            }
        }
    }
}