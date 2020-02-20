package com.example.testingapp.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testingapp.OpenForTest

@OpenForTest
class MainViewModel : ViewModel() {
    val textTitle = MutableLiveData<String>()
    val textSubTitle = MutableLiveData<String>()
}