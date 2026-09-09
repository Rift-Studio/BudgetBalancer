package riftappstudios.finance.budgetbalancer.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.datetime.LocalDate
import riftappstudios.finance.budgetbalancer.util.asString
import riftappstudios.finance.budgetbalancer.util.customDateFormat

@Composable
fun DateRangeInputField(
    modifier: Modifier = Modifier,
    startDate: LocalDate?,
    endDate: LocalDate?,
    onStartDateChanged: (LocalDate) -> Unit,
    onEndDateChanged: (LocalDate) -> Unit,
    onSubmit: () -> Unit,
) {
    Row(
        modifier = modifier.fillMaxWidth(1f),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Start Date Input
        DateInputField(
            label = "Start Date",
            date = startDate,
            onDateChanged = onStartDateChanged,
            modifier = Modifier.weight(1f)
        )
        // End Date Input
        DateInputField(
            label = "End Date",
            date = endDate,
            onDateChanged = onEndDateChanged,
            modifier = Modifier.weight(1f)
        )
        Button(onClick = onSubmit) {
            Text("Dates \\/")
        }
    }
}

@Composable
private fun DateInputField(
    label: String,
    date: LocalDate?,
    onDateChanged: (LocalDate) -> Unit,
    modifier: Modifier = Modifier
) {
    var textValue by remember(date) {
        mutableStateOf(date?.asString() ?: "")
    }
    var isError by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = textValue,
        onValueChange = { input ->
            // Keep text changes inside the local compose scope instantly responsive
            textValue = input
            if (input.isBlank()) {
                isError = false
//                onDateChanged(null)
            } else {
                try {
                    val parsed = LocalDate.parse(input, customDateFormat)
                    isError = false
                    onDateChanged(parsed)
                } catch (e: IllegalArgumentException) {
                    isError = true
                }
            }
        },
        label = { Text(label) },
        placeholder = { Text("mm/dd/yyyy") },
        isError = isError,
        singleLine = true,
        modifier = modifier
    )
}
