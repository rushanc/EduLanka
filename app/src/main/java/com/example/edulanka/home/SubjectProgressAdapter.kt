package com.example.edulanka.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.edulanka.R
import com.example.edulanka.model.SubjectProgress
import android.animation.ObjectAnimator

class SubjectProgressAdapter(
    private var items: List<SubjectProgress>
) : RecyclerView.Adapter<SubjectProgressAdapter.VH>() {

    fun update(newItems: List<SubjectProgress>) {
        items = newItems
        notifyDataSetChanged()
    }

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvSubject: TextView = itemView.findViewById(R.id.tvSubject)
        val tvPercent: TextView = itemView.findViewById(R.id.tvPercent)
        val progressBar: ProgressBar = itemView.findViewById(R.id.progressBar)
        val tvDetail: TextView = itemView.findViewById(R.id.tvDetail)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_subject_progress, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val item = items[position]
        holder.tvSubject.text = item.subject
        holder.progressBar.max = 100
        // Animate from current progress to new percent
        val start = holder.progressBar.progress
        val end = item.percent
        if (start != end) {
            ObjectAnimator.ofInt(holder.progressBar, "progress", start, end).apply {
                duration = 400
                start()
            }
        }
        holder.tvPercent.text = "${item.percent}%"
        holder.tvDetail.text = "${item.watched} of ${item.total} lessons"
    }

    override fun getItemCount(): Int = items.size
}
