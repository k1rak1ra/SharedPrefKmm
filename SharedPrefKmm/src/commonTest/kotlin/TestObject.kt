import kotlinx.serialization.Serializable

@Serializable
data class TestObject(
    val param1: String,
    val param2: Int
)