package com.example.stressmeter

import android.content.Context
import android.os.Bundle
import android.view.Menu
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.stressmeter.databinding.ActivityMainBinding
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.Handler
import android.os.Looper
import android.media.MediaPlayer
import android.view.MenuItem
import android.widget.Toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private var mediaPlayer: MediaPlayer? = null // MediaPlayer instance

    // (deprecated in Android API 31+, but using API 28 as testing device (Samsung S9+))
    private var vibrator: Vibrator? = null // Vibrator instance

    private val handler = Handler(Looper.getMainLooper())

    // Runnable for repeating vibration
    private val vibrationRunnable = object : Runnable {
        override fun run() {
            if (vibrator?.hasVibrator() == true) {
                val vibrationEffect = VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE)
                vibrator?.vibrate(vibrationEffect)
            }
            handler.postDelayed(this, 1000) // Repeat every 1 second
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Bind the layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        // Start vibration and sound using coroutine main
        CoroutineScope(Dispatchers.Main).launch {
            startVibrationAndSound()
        }

        // Set up nav drawer (given by Android Studio)
        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home, R.id.nav_results
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // Stop vibration and sound when navigating to a different fragment
        navController.addOnDestinationChangedListener { _, _, _ ->
            stopVibrationAndSound()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    private fun startVibrationAndSound() {
        // Start vibration and sound using coroutine IO
        // (background thread because we're using MediaPlayer)
        CoroutineScope(Dispatchers.IO).launch {
            // Initialize Vibrator
            // Deprecated in Android API 31+, but using API 28 as testing device (Samsung S9+)
            // This maintains compatibility with older Android versions
            vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator

            // Start repeating vibration in main thread
            withContext(Dispatchers.Main) {
                handler.post(vibrationRunnable)
            }

            // Initialize MediaPlayer and start looping sound
            mediaPlayer = MediaPlayer.create(this@MainActivity, R.raw.ps2sound).apply {
                isLooping = true
                start()
            }
        }
    }

    fun stopVibrationAndSound() {
        // Stop vibration
        handler.removeCallbacks(vibrationRunnable)

        // Stop MediaPlayer
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
    }

    override fun onPause() {
        super.onPause()
        // Stop vibration and sound when activity goes into background
        stopVibrationAndSound()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Ensure the resources are properly released when activity is destroyed
        stopVibrationAndSound()
    }

    // Optional clear results button handler that clears the CSV file
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_clear_results -> {
                clearResultsFile() // Call the function to clear the CSV
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Helper function to clear the CSV file for the drop down menu option
    private fun clearResultsFile() {
        // Delete the contents of "stress_timestamp.csv"
        try {
            val fileOutputStream = openFileOutput("stress_timestamp.csv", Context.MODE_PRIVATE)
            fileOutputStream.write("".toByteArray()) // Write an empty string to clear the file
            fileOutputStream.close()
            // Show a message to confirm the action
            runOnUiThread {
                Toast.makeText(this, "Results cleared", Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
