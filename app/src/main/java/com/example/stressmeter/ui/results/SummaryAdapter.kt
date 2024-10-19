package com.example.stressmeter.ui.results

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.stressmeter.R
import java.util.Locale

class SummaryAdapter(private var stressData: List<Pair<String, Int>>) :
    RecyclerView.Adapter<SummaryAdapter.SummaryViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SummaryViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.summary_item, parent, false)
        return SummaryViewHolder(view)
    }

    override fun onBindViewHolder(holder: SummaryViewHolder, position: Int) {
        val (timestamp, stressLevel) = stressData[position]
        holder.timestampTextView.text = timestamp
        holder.stressTextView.text = String.format(Locale.getDefault(), "%d", stressLevel)
    }

    override fun getItemCount(): Int = stressData.size

    fun updateData(newStressData: List<Pair<String, Int>>) {
        stressData = newStressData
        notifyDataSetChanged()
    }

    class SummaryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val timestampTextView: TextView = itemView.findViewById(R.id.timestamp_text)
        val stressTextView: TextView = itemView.findViewById(R.id.stress_text)
    }
}
