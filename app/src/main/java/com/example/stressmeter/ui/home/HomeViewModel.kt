import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.stressmeter.R

class HomeViewModel : ViewModel() {

    private val imagesPerPage = 16
    private var currentIndex = 0

    private val _imageList = MutableLiveData<List<Int>>()
    val imageList: LiveData<List<Int>> = _imageList

    private val imageResIds = getAllDrawableResourcesWithPrefix("psm_")

    init {
        loadNextImageSet() // Load initial set of images
    }

    fun loadNextImageSet() {
        val endIndex = (currentIndex + imagesPerPage).coerceAtMost(imageResIds.size)
        _imageList.value = imageResIds.subList(currentIndex, endIndex)

        // Update the index to cycle through images
        currentIndex = if (endIndex == imageResIds.size) 0 else endIndex
    }

    private fun getAllDrawableResourcesWithPrefix(prefix: String): List<Int> {
        val drawables = mutableListOf<Int>()
        val fields = R.drawable::class.java.fields

        for (field in fields) {
            val resourceName = field.name
            if (resourceName.startsWith(prefix)) {
                try {
                    val resId = field.getInt(null)
                    drawables.add(resId)
                } catch (e: IllegalAccessException) {
                    e.printStackTrace()
                }
            }
        }
        return drawables
    }
}
