package com.example.stressmeter.ui.results

import ResultsViewModel
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.stressmeter.R
import com.example.stressmeter.databinding.FragmentResultsBinding
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.ceil

class ResultsFragment : Fragment() {

    private var _binding: FragmentResultsBinding? = null
    private val binding get() = _binding!!

    private lateinit var resultsViewModel: ResultsViewModel
    private lateinit var lineChart: LineChart
    private lateinit var adapter: SummaryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Binds the layout file, results_fragment.xml, to this fragment
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize ViewModel
        resultsViewModel = ViewModelProvider(this)[ResultsViewModel::class.java]

        // Set up LineChart
        lineChart = binding.lineChart

        // Set up RecyclerView
        binding.summaryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SummaryAdapter(emptyList())
        binding.summaryRecyclerView.adapter = adapter

        // Load data through ViewModel and observe it
        resultsViewModel.loadStressData(requireContext())
        resultsViewModel.stressData.observe(viewLifecycleOwner) { stressData ->
            // Process data and setup the chart in a coroutine
            CoroutineScope(Dispatchers.Main).launch {
                setupLineChartAsync(stressData)
                adapter.updateData(stressData)
            }
        }

        return root
    }

    private suspend fun setupLineChartAsync(stressData: List<Pair<String, Int>>) {
        // Move data preparation to Default background thread
        val entries = withContext(Dispatchers.Default) {
            stressData.mapIndexed { index, data ->
                Entry(index.toFloat(), data.second.toFloat())
            }
        }

        // Update the chart on the main thread
        setupLineChart(entries)
    }

    private fun setupLineChart(entries: List<Entry>) {
        // Configure the LineChart properties
        val dataSet = LineDataSet(entries, "").apply {
            color = resources.getColor(R.color.teal_200, null) // Line color
            valueTextSize = 10f
            setCircleColor(resources.getColor(R.color.teal_700, null)) // Data point color
            lineWidth = 2f
            circleRadius = 4f
            setDrawFilled(true) // Area under the line
            mode = LineDataSet.Mode.CUBIC_BEZIER // Rounded lines
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString() // Display only whole numbers
                }
            }
        }

        // Set the LineDataSet to the LineChart
        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.setDrawGridBackground(false) // Remove grid background

        // Configure the X-axis
        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM // X-axis on the bottom
            setDrawGridLines(false) // Remove grid lines
            isGranularityEnabled = true
            granularity = 1f // Set point labels to whole numbers

            // Axis scaling
            axisMinimum = 0f
            axisMaximum = (entries.size - 1).toFloat()
            val labelInterval = 2
            labelCount = ceil((entries.size.toFloat() / labelInterval)).toInt() + 1

            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return if (value.toInt() <= entries.size - 1) {
                        value.toInt().toString()
                    } else {
                        ""
                    }
                }
            }
        }

        // Configure the Y-axis
        lineChart.axisLeft.apply {
            granularity = 1f
            axisMinimum = 0f
            valueFormatter = object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toInt().toString()
                }
            }
        }

        lineChart.axisRight.isEnabled = false
        lineChart.description = Description().apply { text = "" }
        lineChart.legend.isEnabled = false
        lineChart.invalidate() // Refresh the chart
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
