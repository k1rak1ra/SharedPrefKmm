import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.unit.dp
import net.k1ra.sharedprefkmm.SharedPreferences
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
@Preview
fun App() {
    val keyboardController = LocalSoftwareKeyboardController.current
    val pref = SharedPreferences("SharedPrefDemoApp")
    val textToStore = remember { mutableStateOf(pref.getSynchronously<String>("demoText") ?: "") }
    val retrievedText = remember { mutableStateOf(pref.getSynchronously<String>("demoText")) }

    MaterialTheme {
        Column(
            Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            DefaultTextField(textToStore, "Text to store", keyboardController)

            Button(onClick = {
                pref.setSynchronously("demoText", textToStore.value)
            }) {
                Text("Store")
            }

            Button(onClick = {
                retrievedText.value = pref.getSynchronously<String>("demoText")
            }) {
                Text("Get")
            }

            retrievedText.value?.let { Text(it) }
        }
    }
}