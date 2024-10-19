package com.example.stressmeter.ui.results

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.BufferedReader
import java.io.InputStreamReader

class ResultsViewModel : ViewModel() {

    private val _stressData = MutableLiveData<List<Pair<String, Int>>>()
    val stressData: LiveData<List<Pair<String, Int>>> = _stressData

    fun loadStressData(context: Context) {
        val loadedData = mutableListOf<Pair<String, Int>>()
        try {
            val inputStream = context.openFileInput("stress_timestamp.csv")
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?

            while (reader.readLine().also { line = it } != null) {
                val tokens = line?.split(",") ?: continue
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

        // Update LiveData
        _stressData.value = loadedData
    }
}
