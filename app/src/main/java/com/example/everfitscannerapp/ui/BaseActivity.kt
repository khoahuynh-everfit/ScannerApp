package com.example.everfitscannerapp.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.viewbinding.ViewBinding

abstract class BaseActivity<B : ViewBinding> : AppCompatActivity() {

    abstract fun getViewBinding() : B

    protected lateinit var binding: B

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = getViewBinding()
        setContentView(binding.root)
        onReady()
        observeReady()
        eventReady()
    }

    abstract open fun onReady()

    abstract open fun observeReady()

    abstract open fun eventReady()


}