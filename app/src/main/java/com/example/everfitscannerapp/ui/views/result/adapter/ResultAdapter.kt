package com.example.everfitscannerapp.ui.views.result.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.everfitscannerapp.databinding.LayoutResultItemBinding
import com.example.everfitscannerapp.domain.model.ResultModel

class ResultAdapter(private val onItemClick: (ResultModel) -> Unit) :
    ListAdapter<ResultModel, ResultAdapter.ResultView>(ResultDiffUtilCallback()) {

    class ResultDiffUtilCallback : DiffUtil.ItemCallback<ResultModel>() {
        override fun areItemsTheSame(oldItem: ResultModel, newItem: ResultModel): Boolean =
            oldItem.serviceName == newItem.serviceName

        override fun areContentsTheSame(oldItem: ResultModel, newItem: ResultModel): Boolean =
            oldItem == newItem
    }

    inner class ResultView(private val binding: LayoutResultItemBinding) :
        ViewHolder(binding.root) {
        fun bind(resultModel: ResultModel, showDivider: Boolean) {
            binding.txtServiceName.text = "Service - ${resultModel.serviceName}"
            val results = resultModel.resultData.size
            val text = when (results) {
                0 -> "$results results"
                1 -> "$results result. Click to view details"
                else -> "$results results. Click to view details"
            }
            binding.txtNumberOfResult.text = text
            binding.llDetail.setOnClickListener {
                if (results > 0) {
                    onItemClick.invoke(resultModel)
                }
            }
            binding.divider.visibility = if (showDivider) View.VISIBLE else View.GONE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ResultView {
        return ResultView(
            binding = LayoutResultItemBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ResultView, position: Int) {
        holder.bind(
            resultModel = getItem(position),
            showDivider = position < itemCount - 1
        )
    }

}