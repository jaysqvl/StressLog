package com.example.stressmeter.ui.results

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
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet

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
        _binding = FragmentResultsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Initialize ViewModel
        resultsViewModel = ViewModelProvider(this).get(ResultsViewModel::class.java)

        // Set up LineChart
        lineChart = binding.lineChart

        // Set up RecyclerView
        binding.summaryRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SummaryAdapter(emptyList())
        binding.summaryRecyclerView.adapter = adapter

        // Load data through ViewModel and observe it
        resultsViewModel.loadStressData(requireContext())
        resultsViewModel.stressData.observe(viewLifecycleOwner) { stressData ->
            setupLineChart(stressData)
            adapter.updateData(stressData)
        }

        return root
    }

    private fun setupLineChart(stressData: List<Pair<String, Int>>) {
        val entries = stressData.mapIndexed { index, data ->
            Entry(index.toFloat(), data.second.toFloat())
        }
        val dataSet = LineDataSet(entries, "Stress Levels").apply {
            color = resources.getColor(R.color.teal_200, null)
            valueTextSize = 10f
            setCircleColor(resources.getColor(R.color.teal_200, null))
            lineWidth = 2f
        }

        val lineData = LineData(dataSet)
        lineChart.data = lineData
        lineChart.setDrawGridBackground(false)
        lineChart.animateY(1000)

        val description = Description().apply {
            text = ""
        }
        lineChart.description = description
        lineChart.invalidate() // Refresh the chart
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
