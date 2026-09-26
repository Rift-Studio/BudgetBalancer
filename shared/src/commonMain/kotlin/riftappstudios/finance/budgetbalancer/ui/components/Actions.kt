package riftappstudios.finance.budgetbalancer.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp

data class RowAction(
    val icon: ImageVector? = null,
    val contentDescription: String,
    val onClick: () -> Unit
)


@Composable
fun DynamicRowWithActions(
    actions: List<RowAction>,
    modifier: Modifier = Modifier,
    primaryContent: @Composable () -> Unit = {}
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Main Content (Takes up all remaining space on the left)
        Column(
            modifier = Modifier.weight(1f)
        ) {
            primaryContent()
        }

        Spacer(modifier = Modifier.width(16.dp))

        // Dynamic Vertical Action Buttons
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            actions.forEach { action ->
                action.icon?.let { icon ->
                    IconButton(onClick = action.onClick) {
                        Icon(
                            imageVector = icon,
                            contentDescription = action.contentDescription
                        )
                    }
                } ?: Button(onClick = action.onClick) {
                    Text(action.contentDescription)
                }
            }
        }
    }
}
