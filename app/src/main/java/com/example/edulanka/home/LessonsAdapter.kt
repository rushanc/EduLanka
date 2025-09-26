package com.example.edulanka.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.edulanka.R
import com.example.edulanka.model.Lesson

class LessonsAdapter(
    private var items: List<Lesson>,
    private val onClick: (Lesson) -> Unit
) : RecyclerView.Adapter<LessonsAdapter.VH>() {

    fun update(newItems: List<Lesson>) {
        items = newItems
        notifyDataSetChanged()
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivThumb: ImageView = itemView.findViewById(R.id.ivThumb)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val tvMeta: TextView = itemView.findViewById(R.id.tvMeta)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_lesson, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvMeta.text = "${item.grade} • ${item.subject}"
        Glide.with(holder.itemView).load(item.thumbUrl).placeholder(R.mipmap.ic_launcher).into(holder.ivThumb)
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size
}
