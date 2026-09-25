package riftappstudios.finance.budgetbalancer.ui.components
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp

// Cross-platform regular expression for basic email validation
private val EMAIL_REGEX = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}\$".toRegex()

@Composable
fun LoginForm(
    modifier: Modifier = Modifier,
    onSubmit: (String, String) -> Unit,
    onCreateAccount: (String, String) -> Unit
) {
    var email by remember { mutableStateOf("carlsonjordan14@gmail.com") }
    var password by remember { mutableStateOf("password") }
    var passwordVisible by remember { mutableStateOf(false) }

    val focusManager = LocalFocusManager.current

    // Pure Kotlin Multiplatform email validation
    val isEmailValid by remember {
        derivedStateOf {
            email.matches(EMAIL_REGEX)
        }
    }

    // Password rules (e.g., minimum 6 characters)
    val isPasswordValid by remember {
        derivedStateOf {
            password.length >= 4
        }
    }

    // Enable submit button only if both fields pass validation
    val isFormValid by remember {
        derivedStateOf {
            isEmailValid && isPasswordValid
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Email Input Field
        OutlinedTextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email Address") },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions(
                onNext = { focusManager.moveFocus(FocusDirection.Down) }
            ),
            isError = email.isNotEmpty() && !isEmailValid,
            supportingText = {
                if (email.isNotEmpty() && !isEmailValid) {
                    Text("Please enter a valid email address", color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        // Password Input Field
        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            trailingIcon = {
                // Replaced graphic icon with a plain text toggle button
                TextButton(onClick = { passwordVisible = !passwordVisible }) {
                    Text(if (passwordVisible) "Hide" else "Show")
                }
            },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {
                    if (isFormValid) {
                        focusManager.clearFocus()
                        onSubmit(email, password)
                    }
                }
            ),
            isError = password.isNotEmpty() && !isPasswordValid,
            supportingText = {
                if (password.isNotEmpty() && !isPasswordValid) {
                    Text("Password must be at least 6 characters", color = MaterialTheme.colorScheme.error)
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Submit Button
        Button(
            onClick = {
                focusManager.clearFocus()
                onSubmit(email, password)
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }

        // Submit Button
        Button(
            onClick = {
                focusManager.clearFocus()
                onCreateAccount(email, password)
            },
            enabled = isFormValid,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Create Account")
        }
    }
}
