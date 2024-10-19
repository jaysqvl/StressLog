import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader

class ResultsViewModel : ViewModel() {

    // LiveData to hold the time and stress data
    private val _stressData = MutableLiveData<List<Pair<String, Int>>>()
    val stressData: LiveData<List<Pair<String, Int>>> = _stressData

    fun loadStressData(context: Context) {
        // IO Coroutine to load data from the file
        CoroutineScope(Dispatchers.IO).launch {
            val loadedData = mutableListOf<Pair<String, Int>>()

            try {
                val inputStream = context.openFileInput("stress_timestamp.csv")
                val reader = BufferedReader(InputStreamReader(inputStream))
                var line: String?

                // Skip the header line
                while (reader.readLine().also { line = it } != null) {
                    // Split each line into timestamp and stress level
                    val tokens = line?.split(",") ?: continue

                    // Check if the line has the correct number of tokens (timestamp and stress)
                    if (tokens.size == 2) {
                        val timestamp = tokens[0]
                        val stressLevel = tokens[1].toIntOrNull() ?: continue
                        loadedData.add(timestamp to stressLevel)
                    }
                }

                reader.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            // Update LiveData on the main thread
            withContext(Dispatchers.Main) {
                _stressData.value = loadedData
            }
        }
    }
}
