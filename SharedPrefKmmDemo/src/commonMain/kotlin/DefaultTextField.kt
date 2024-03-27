import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction

@Composable
fun DefaultTextField(text: MutableState<String>, label: String, keyboardController: SoftwareKeyboardController?) {
    TextField(
        value = text.value,
        onValueChange = {
            text.value = it
        },
        label = { Text(label) },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
        keyboardActions = KeyboardActions(
            onDone = { keyboardController?.hide() })
    )
}