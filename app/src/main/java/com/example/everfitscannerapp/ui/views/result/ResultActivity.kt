package com.example.everfitscannerapp.ui.views.result

import android.app.Activity
import android.content.Intent
import android.view.MenuItem
import androidx.activity.viewModels
import com.example.everfitscannerapp.databinding.ActivityResultBinding
import com.example.everfitscannerapp.domain.model.ScanService
import com.example.everfitscannerapp.ui.BaseActivity
import com.example.everfitscannerapp.ui.views.detail.DetailActivity
import com.example.everfitscannerapp.ui.views.result.adapter.ResultAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ResultActivity : BaseActivity<ActivityResultBinding>() {

    companion object {

        private const val SERVICES = "SERVICES"
        private const val MODE = "MODE"
        private const val INPUT = "INPUT"

        fun startResultScreen(activity: Activity, services: List<ScanService>, mode: Mode, input: String) {
            val intent = Intent(activity, ResultActivity::class.java).apply {
                val listServices = arrayListOf<ScanService>().apply { addAll(services) }
                putParcelableArrayListExtra(SERVICES, listServices)
                putExtra(MODE, mode)
                putExtra(INPUT, input)
            }
            activity.startActivity(intent)
        }
    }

    private val viewModel : ResultViewModel by viewModels()

    private val adapter by lazy {
        ResultAdapter { resultModel ->
            DetailActivity.startDetailScreen(
                activity = this@ResultActivity,
                result = resultModel
            )
        }
    }

    override fun getViewBinding(): ActivityResultBinding = ActivityResultBinding.inflate(layoutInflater)

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        item.let {
            if (it.itemId == android.R.id.home) {
                onBackPressed()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onReady() {
        (intent.getSerializableExtra(MODE) as? Mode)?.let { mode ->
            supportActionBar?.title = "Result (${mode.name})"
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
            val services = intent.extras?.getParcelableArrayList<ScanService>(SERVICES)?: arrayListOf()
            val input = intent.getStringExtra(INPUT) ?: ""
            viewModel.initMode(services = services, mode = mode, input = input)
        }
    }

    override fun observeReady() {
        viewModel.result.observe(this) { results ->
            adapter.submitList(results)
        }
    }

    override fun eventReady() {
        binding.recyclerView.adapter = adapter
    }
}