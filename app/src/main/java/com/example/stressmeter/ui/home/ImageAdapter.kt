package com.example.stressmeter.ui.home

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import com.example.stressmeter.R

class ImageAdapter(
    private val context: Context, // Context of the activity or fragment
    private val images: List<Int> // List of image resource IDs
) : BaseAdapter() {

    override fun getCount(): Int {
        // Return the number of image resource ids
        return images.size
    }

    override fun getItem(position: Int): Any {
        // Return the image resource id at the given position
        return images[position]
    }

    override fun getItemId(position: Int): Long {
        // Return a unique identifier for the item at the given position
        return position.toLong()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        // If the view is not recycled, inflate the layout
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.grid_item, parent, false)

        // Find the ImageView in the layout and set the image resource
        val imageView: ImageView = view.findViewById(R.id.imageView)
        imageView.setImageResource(images[position])

        return view
    }
}
