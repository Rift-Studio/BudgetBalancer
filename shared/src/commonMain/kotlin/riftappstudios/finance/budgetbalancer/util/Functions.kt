package riftappstudios.finance.budgetbalancer.util

import androidx.compose.ui.graphics.Color
import riftappstudios.finance.budgetbalancer.data.objects.Accounts
import riftappstudios.finance.budgetbalancer.data.objects.Transaction
import kotlin.math.absoluteValue


fun Transaction.budgetValue(): Float {
    return when (type.lowercase()) {
            "credit" -> -amount
            "debit" -> amount
            else -> 0f
    }
}

fun Transaction.valueByAccType(accounts: Accounts): Float {
    val matchedAccount = accounts.accounts.find {
        it.name == account
    } ?: return 0f

    val res = when (matchedAccount.type.lowercase()) {
        "deposit" -> when (type.lowercase()) {
            "credit" -> amount
            "debit" -> -amount
            else -> 0f
        }

        "debt" -> when (type.lowercase()) {
            "credit" -> -amount
            "debit" -> amount
            else -> 0f
        }

        else -> 0f
    }
    println("$matchedAccount $res")
    return res
}


fun String.toFinance(): Finance {
    return if (lowercase().startsWith("credit")) {
        Finance.Credit
    } else if (lowercase().startsWith("debit")) {
        Finance.Debit
    } else {
        Finance.Error
    }
}
sealed class Finance {
    object Debit: Finance()
    object Credit: Finance()
    object Error: Finance()
}

fun getColor(accountName: String, transactionType: String, amount: Float): Color {
    return when (transactionType) {

        "Debit" -> Color(0xFFC62828) // Red for expense
        "Credit" -> Color(0xFF2E7D32) // Green for income

//        "Debit" -> if (amount < 0f)
//            Color(0xFFC62828) // Red for expense
//        else
//            Color(0xFF2E7D32) // Green for income
//
//        "Credit" -> if (amount <= 0f)
//            Color(0xFF2E7D32) // Green for income
//        else
//            Color(0xFFC62828) // Red for expense
        else -> Color.Gray
    }
}

fun Float.toFinance(): String {
    return if (this < 0f)
        "-$${String.format("%.2f", absoluteValue)}"
    else
        "$${String.format("%.2f", absoluteValue)}"
}

fun Int.toFinance(): String {
    return if (this < 0f)
        "-$${this*-1}"
    else
        "$${this}"
}