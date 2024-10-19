package com.example.stressmeter.ui.home

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.stressmeter.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class ImageResponseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_response)

        // Get the selected image resource ID from the intent passed by the home fragment
        // The setupGridView function passes this ID to this activity
        val imageResId = intent.getIntExtra("image_res_id", -1)

        // Check if a valid image resource ID was passed
        if (imageResId == -1) {
            finish() // Exit the activity if no valid image was passed
            return
        }

        // Display the selected image
        val imageView: ImageView = findViewById(R.id.selectedImageView)
        imageView.setImageResource(imageResId)

        val cancelButton: Button = findViewById(R.id.cancelButton)
        cancelButton.setOnClickListener {
            // Simply close this activity
            finish()
        }

        // Set up Submit button
        val submitButton: Button = findViewById(R.id.submitButton)
        submitButton.setOnClickListener {
            // Save the timestamp and stress score to CSV in a background thread
            CoroutineScope(Dispatchers.IO).launch {
                saveStressData(imageResId)
            }

            // Close the entire app
            finishAffinity() // Closes all activities and exits the app
        }
    }

    // Coroutine IO thread job to save timestamp and stress score to CSV
    private fun saveStressData(imageResId: Int) {
        // Assign a "stress score" based on the image resource ID position
        val stressScore = imageResId % 16 + 1

        // Get the current timestamp
        val timestamp = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                                        Locale.getDefault()).format(Date())

        // Write to CSV file (stress_timestamp.csv)
        val data = "$timestamp,$stressScore\n"
        val fileName = "stress_timestamp.csv"

        try {
            val fileOutputStream: FileOutputStream = openFileOutput(fileName, Context.MODE_APPEND)
            fileOutputStream.write(data.toByteArray())
            fileOutputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
