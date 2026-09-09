package riftappstudios.finance.budgetbalancer.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import riftappstudios.finance.budgetbalancer.data.objects.Transaction
import riftappstudios.finance.budgetbalancer.data.objects.Transactions
import kotlin.math.roundToInt

fun Transactions.toDateMap() : MutableMap<LocalDate, Transactions?> {
    val map = mutableMapOf<LocalDate, Transactions?>()
    var currentDay = rows.first().date
    var currentDayTransactions = mutableListOf<Transaction>()

    // for transactions on day, add to list
    for (row in rows) {
        if (row.date == currentDay) {
            currentDayTransactions.add(row)
        } else {
            // when day changes add to map and reset
            map[currentDay] = Transactions(currentDayTransactions)

            var dayDiff = row.date.minus(currentDay).days
            if (dayDiff > 1) {
                while (dayDiff > 0) {
                    currentDay = currentDay.plus(1, DateTimeUnit.DAY)
                    map[currentDay] = null
                    dayDiff--
                }
            }
            currentDay = row.date
            currentDayTransactions = mutableListOf(row)
        }
    }
    // get last row
    map[currentDay] = Transactions(currentDayTransactions)
    return map
}

@Composable
fun Calendar(transactionsMap: Map<LocalDate, Transactions?>) {
    BoxWithConstraints(
        contentAlignment = Alignment.Center,
        modifier = Modifier.background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        val numColumns = 7
        val numRows = 2
        // 1. You can access the exact window/container size in Dp
        val currentWidth = maxWidth
        val currentHeight = maxHeight

    LazyRow {
        transactionsMap.keys.forEach { date ->
            item(key = date) {
                Day(transactionsMap[date]?.rows,
                    date,
                    DpSize(currentWidth/numColumns, currentHeight/numRows))
            }
        }
    }
}
}

@Composable
fun Day(transactions: List<Transaction>?, day: LocalDate, size: DpSize) {

    val income = transactions?.filter { it.type == "Credit" }?.sumOf {
        it.amount.roundToInt()
    }
    val spend =  transactions?.filter { it.type == "Debit" }?.sumOf {
        it.amount.roundToInt()
    }
    val total = income?.minus(spend ?: 0)

    Column(
            Modifier.size(size).border(2.dp, color = MaterialTheme.colorScheme.outline)
        ) {
            Row(
                modifier = Modifier.padding(all = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "${day.month.toString().slice(0..2)} ${day.dayOfWeek.toString().slice(0..2)} - ${day.dayOfMonth}")
                // Price
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.weight(.5f)) {
                    Text(text = total?.toFinance() ?: "$0")
                }
            }
        LazyColumn {


            transactions?.forEach { transaction ->
                item {DayTxnRow(transaction)}
            }
        }
        }

}


@Composable
fun DayTxnRow(transaction: Transaction) {
    ///////////////////////
    //+ Desc            $44
    ///////////////////////
    Row(modifier = Modifier.padding(all = 4.dp).fillMaxWidth()) {
        // Color indicator for budget
        Column {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {

                // Short Desc
                Text(modifier = Modifier.weight(.5f), text = transaction.description)



                // Price
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.weight(.5f)) {
                    Text(text = "$${transaction.amount.roundToInt()}",
                        fontWeight = FontWeight.Bold,
                        color = getColor(transaction.account,transaction.type,transaction.amount))
                }
            }
            Surface(
                shape = RectangleShape,
                modifier = Modifier.height(2.dp).fillMaxWidth(),
                content = {

                },
                color = transaction.category.toStableColor()
            )
        }

    }
}