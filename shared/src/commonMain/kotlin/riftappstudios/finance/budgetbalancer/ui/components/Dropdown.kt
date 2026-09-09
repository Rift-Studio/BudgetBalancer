package riftappstudios.finance.budgetbalancer.ui.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeTextFilterDropdown(
    modifier: Modifier = Modifier,
    label: String,
    options: List<String>,
    onOptionSelected: (String?) -> Unit,
) {
    val optionsWithAll = options.toMutableList().apply { add("Any") }
    // 1. Local state holding the typed text, selection state, and dropdown visibility
    var searchQuery by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf<String?>(null) }
    var expanded by remember { mutableStateOf(false) }

    // 2. Dynamically filter options based on user input
    val filteredOptions by remember(optionsWithAll) {
        derivedStateOf {
            if (searchQuery.isEmpty()) {
                optionsWithAll
            } else {
                optionsWithAll.filter { it != searchQuery }
            }
        }
    }

    // 3. Material 3 Dropdown Container
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
        modifier = modifier.padding(horizontal = 8.dp)
    ) {
        // Editable input field acting as a search bar and value display
        OutlinedTextField(
            value = searchQuery,
            onValueChange = {
                searchQuery = it
                expanded = true // Keep menu open while typing
            },
            label = { Text(label) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(),
            modifier = Modifier
                .menuAnchor() // Ties the dropdown menu position to this field
        )

        // 4. Dropdown Menu List
        if (filteredOptions.isNotEmpty()) {
            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                filteredOptions.forEach { option ->
                    DropdownMenuItem(
                        text = { Text(text = option) },
                        onClick = {
                            selectedOption = option
                            searchQuery = option // Fill the text field with selection
                            expanded = false
                            onOptionSelected(if (option == "Any")  null  else option)
                        },
                        contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                    )
                }
            }
        }
    }
}
