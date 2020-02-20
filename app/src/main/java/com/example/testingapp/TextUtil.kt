package com.example.testingapp

interface TextUtil {
    fun welcomeText(): String
}

class TextUtilImpl : TextUtil {
    override fun welcomeText(): String {
        return "Hello"
    }
}
