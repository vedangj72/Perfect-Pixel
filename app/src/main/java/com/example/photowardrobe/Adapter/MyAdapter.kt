package com.example.photowardrobe.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.photowardrobe.Dataclass.Item
import com.example.photowardrobe.Models.Images
import com.example.photowardrobe.Models.Photo
import com.example.photowardrobe.R

class MyAdapter(private var listdataClass: List<Photo>, private val onItemClick: (Photo) -> Unit) : RecyclerView.Adapter<MyAdapter.HomeViewHolder>() {

    // ViewHolder class to hold references to each item view
    inner class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val itemImage: ImageView = view.findViewById(R.id.cardImageView) // Replace with your actual ImageView ID
        val itemText: TextView = view.findViewById(R.id.cardTextView)

        // Bind method to update views with data
        fun bind(photo: Photo) {
            Glide.with(itemImage.context)
                .load(photo.src.portrait)
                .into(itemImage)
            itemText.text = photo.alt

        itemView.setOnClickListener { onItemClick(photo) }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_layout_preview, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val item = listdataClass[position]
        holder.bind(item)
    }

    // Returns the total number of items in the dataset
    override fun getItemCount(): Int {
        return listdataClass.size
    }
    fun updateData(newData: List<Photo>) {
        listdataClass = newData
        notifyDataSetChanged()
    }

}
