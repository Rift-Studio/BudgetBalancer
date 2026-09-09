package riftappstudios.finance.budgetbalancer.data.objects

import kotlinx.datetime.LocalDate
import kotlinx.serialization.Serializable
import riftappstudios.finance.budgetbalancer.util.CustomLocalDateSerializer


//"id": str(row.get('ID', '')),
//"date": str(row.get('Date', '')),
//"description": str(row.get('Description', '')),
//"originalDescription": str(row.get('Original_Description', '')),
//"amount": amount_val,
//"category": str(row.get('Category', '')),
//"parentCategory": str(row.get('parentCategory', '')),
//"originalCategory": str(row.get('Original_Category','')),
//"account": str(row.get('Account', '')),
//"tags": str(row.get("Tags")),
//"memo": str(row.get("Memo")),
//"pending": bool(row.get("Pending"))
/*
Transaction definition
 */
@Serializable
data class Transaction(
    val id: String = "blank",
    @Serializable(with = CustomLocalDateSerializer::class)
    val date: LocalDate = LocalDate(9999,2,31),
    val description: String = "Blank",
    val originalDescription: String = "blank",
    val category: String = "blank",
    val parentCategory: String = "blank",
    val originalCategory: String = "blank",
    val amount: Float = 0.0f,
    val type: String = "blank",
    val account: String = "blank",
//    val tags: List<String>,
    val memo: String = "blank",
    val pending: Boolean = false
) {

}

@Serializable
data class Transactions(
    val rows: List<Transaction>
)