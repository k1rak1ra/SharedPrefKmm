import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import net.k1ra.sharedprefkmm.SharedPreferences
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val keyboardController = LocalSoftwareKeyboardController.current
    val pref = SharedPreferences("SharedPrefDemoApp")
    val textToStore = remember { mutableStateOf("") }
    val retrievedText = remember { mutableStateOf(null as String?) }
    val scope = rememberCoroutineScope()

    scope.launch {
        textToStore.value = pref.get<String>("demoText") ?: ""
        retrievedText.value = pref.get<String>("demoText")
    }

    MaterialTheme {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DefaultTextField(textToStore, "Text to store", keyboardController)

            Button(onClick = {
                scope.launch {
                    pref.set("demoText", textToStore.value)
                }
            }) {
                Text("Store")
            }

            Button(onClick = {
                scope.launch {
                    retrievedText.value = pref.get<String>("demoText")
                }
            }) {
                Text("Get")
            }

            retrievedText.value?.let { Text(it) }
        }
    }
}