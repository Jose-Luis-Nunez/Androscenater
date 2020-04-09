package com.example.testingapp.main

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil.setContentView
import com.example.testingapp.BR
import com.example.testingapp.R
import com.example.testingapp.TextUtil
import com.example.testingapp.databinding.ActivityMainBinding
import com.example.testingapp.second.SecondActivity
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    private val viewModel by viewModel<MainViewModel>()
    private val textUtil: TextUtil by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding: ActivityMainBinding = setContentView(
            this,
            R.layout.activity_main
        )
        binding.lifecycleOwner = this
        binding.setVariable(BR.viewmodel, viewModel)
        binding.button.setOnClickListener {
            navigateToSecond()
        }
        textUtil.welcomeText(this)
    }

    private fun navigateToSecond() {
        val intent = Intent(this, SecondActivity::class.java)
        startActivity(intent)
    }
}
