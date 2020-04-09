package com.example.testingapp.second

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testingapp.OpenForTest

@OpenForTest
class SecondViewModel(text: String) : ViewModel() {
    val textTitle = MutableLiveData<String>().apply { postValue(text) }
}

