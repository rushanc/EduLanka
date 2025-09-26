package com.example.edulanka.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edulanka.R
import com.example.edulanka.model.Announcement
import com.example.edulanka.model.FeaturedLesson

class FeaturedAdapter(
    private val items: List<FeaturedLesson>,
    private val onClick: (FeaturedLesson) -> Unit
) : RecyclerView.Adapter<FeaturedAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivThumb: ImageView = itemView.findViewById(R.id.ivThumb)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_featured_lesson, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.ivThumb.setImageResource(item.imageRes)
        holder.tvTitle.text = item.title
        holder.itemView.setOnClickListener { onClick(item) }
    }

    override fun getItemCount(): Int = items.size
}

class AnnouncementAdapter(
    private val items: List<Announcement>,
    private val onClick: (Announcement) -> Unit,
    private val onLongClick: ((Announcement) -> Unit)? = null
) : RecyclerView.Adapter<AnnouncementAdapter.VH>() {

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitle: TextView = itemView.findViewById(R.id.tvAnnTitle)
        val tvMessage: TextView = itemView.findViewById(R.id.tvAnnMessage)
        val tvTime: TextView = itemView.findViewById(R.id.tvAnnTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_announcement, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.tvTitle.text = item.title
        holder.tvMessage.text = item.message
        holder.tvTime.text = item.timeLabel
        holder.itemView.setOnClickListener { onClick(item) }
        holder.itemView.setOnLongClickListener {
            onLongClick?.invoke(item)
            onLongClick != null
        }
    }

    override fun getItemCount(): Int = items.size
}
