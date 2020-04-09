package com.example.testingapp

import android.content.Context

interface TextUtil {
    fun welcomeText(context: Context): String
}

class TextUtilImpl : TextUtil {
    override fun welcomeText(context: Context): String {
        return "Hello"
    }
}
