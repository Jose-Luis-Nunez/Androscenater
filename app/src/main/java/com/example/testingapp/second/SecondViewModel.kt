package com.example.testingapp.second

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SecondViewModel : ViewModel() {
    val textTitle = MutableLiveData<String>()
    val textSubTitle = MutableLiveData<String>()
}
