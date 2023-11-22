package com.example.everfitscannerapp.ui.views.detail

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import com.example.everfitscannerapp.databinding.ActivityDetailBinding
import com.example.everfitscannerapp.domain.model.ResultModel
import com.example.everfitscannerapp.ui.BaseActivity
import com.example.everfitscannerapp.ui.views.detail.adapter.ResultDetailAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailActivity : BaseActivity<ActivityDetailBinding>() {

    companion object {
        private const val DATA = "DATA"
        fun startDetailScreen(activity: Activity, result: ResultModel) {
            activity.startActivity(
                Intent(activity, DetailActivity::class.java).apply {
                    putExtra(DATA, result)
                }
            )
        }
    }

    private val adapter : ResultDetailAdapter by lazy {
        ResultDetailAdapter()
    }

    override fun getViewBinding(): ActivityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.let {
            if (it.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onReady() {
        supportActionBar?.title = "Detail"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.rvResult.adapter = adapter
        intent.getParcelableExtra<ResultModel>(DATA)?.let { data ->
            binding.tvService.text = "Service - ${data.serviceName}"
            adapter.submitList(data.resultData)
        }
    }

    override fun observeReady() {
    }

    override fun eventReady() {
    }
}