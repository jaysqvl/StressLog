package com.example.stressmeter.ui.results

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stressmeter.R
import java.util.Locale

// Adapter for the summary RecyclerView
class SummaryAdapter(private var stressData: List<Pair<String, Int>>) :
    RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder>() {

    // Called when RecyclerView needs a new ViewHolder to represent an item.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.summary_item, parent, false)
        return SummaryViewHolder(view)
    }

    // Binds the data to the ViewHolder for the item at the given position.
    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        val (timestamp, stressLevel) = stressData[position]
        holder.timestampTextView.text = timestamp
        holder.stressTextView.text = String.format(Locale.getDefault(), "%d", stressLevel)
    }

    // Returns the total number of items in the data set.
    override fun getItemCount(): Int = stressData.size

    // Updates the data displayed in the RecyclerView.
    fun updateData(newStressData: List<Pair<String, Int>>) {
        stressData = newStressData
        notifyDataSetChanged()
    }

    // ViewHolder class that represents each item in the RecyclerView.
    class SummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timestampTextView: TextView = itemView.findViewById(R.id.timestamp_text)
        val stressTextView: TextView = itemView.findViewById(R.id.stress_text)
    }
}
