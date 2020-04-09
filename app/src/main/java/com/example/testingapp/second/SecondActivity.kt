package com.example.testingapp.second

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.testingapp.R


class SecondActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        supportFragmentManager.beginTransaction()
            .replace(R.id.details_container, SecondFragment.createFragment("extra"), "TAG")
            .commit()
    }

}
