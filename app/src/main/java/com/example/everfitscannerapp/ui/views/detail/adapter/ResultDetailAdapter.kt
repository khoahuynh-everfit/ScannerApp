package com.example.everfitscannerapp.ui.views.detail.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.everfitscannerapp.databinding.LayoutDetailResultItemBinding
import com.google.gson.GsonBuilder
import com.google.gson.JsonParser

class ResultDetailAdapter : ListAdapter<String, ResultDetailAdapter.ResultDetailView>(
    ResultDiffUtilCallback()
) {

    class ResultDiffUtilCallback : DiffUtil.ItemCallback<String>() {
        override fun areItemsTheSame(oldItem: String, newItem: String): Boolean = oldItem == newItem

        override fun areContentsTheSame(oldItem: String, newItem: String): Boolean =
            oldItem == newItem
    }

    inner class ResultDetailView(private val binding: LayoutDetailResultItemBinding) :
        ViewHolder(binding.root) {
        fun bind(result: String, showDivider: Boolean) {
            val gson = GsonBuilder().setPrettyPrinting().create()
            val je = JsonParser.parseString(result)
            binding.tvResult.text = gson.toJson(je)
            binding.divider.visibility = if (showDivider) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultDetailView {
        return ResultDetailView(
            binding = LayoutDetailResultItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ResultDetailView, position: Int) {
        holder.bind(
            result = getItem(position),
            showDivider = position < itemCount - 1
        )
    }

}