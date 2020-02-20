package com.example.testingapp.testing.unit

import android.content.SharedPreferences

/**
 * Mocked [SharedPreferenceService] to be used in unit tests without framework dependencies.
 */
class FakeSharedPreferences : SharedPreferences {

    private var values = mutableMapOf<String, Any?>()

    override fun getBoolean(
        key: String,
        defaultValue: Boolean
    ): Boolean {
        return values[key] as? Boolean ?: defaultValue
    }

    override fun getString(
        key: String,
        defaultValue: String?
    ): String? {
        return values[key] as? String ?: defaultValue
    }

    override fun getLong(key: String, defaultValue: Long): Long {
        return values[key] as? Long ?: defaultValue
    }

    override fun contains(key: String?): Boolean {
        return values[key] != null
    }

    override fun unregisterOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener?
    ) {
    }

    override fun getInt(key: String?, defValue: Int): Int {
        return values[key] as? Int ?: defValue
    }

    override fun getAll(): MutableMap<String, *> {
        return values
    }

    override fun edit(): SharedPreferences.Editor {
        return Editor()
    }

    override fun getFloat(key: String?, defValue: Float): Float {
        return values[key] as? Float ?: defValue
    }

    override fun getStringSet(key: String?, defValues: MutableSet<String>?): MutableSet<String>? {
        return null
    }

    fun putBoolean(key: String, value: Boolean) {
        values[key] = value
    }

    fun putString(key: String, value: String?) {
        values[key] = value
    }

    fun putLong(key: String, value: Long) {
        values[key] = value
    }

    fun putInt(key: String, value: Int) {
        values[key] = value
    }

    fun putFloat(key: String, value: Float) {
        values[key] = value
    }

    fun isEmpty(): Boolean {
        return values.isEmpty()
    }

    override fun registerOnSharedPreferenceChangeListener(
        listener: SharedPreferences.OnSharedPreferenceChangeListener?
    ) {
    }

    inner class Editor : SharedPreferences.Editor {

        private var tempValues = HashMap(values)

        override fun clear(): SharedPreferences.Editor {
            tempValues.clear()
            return this
        }

        override fun putBoolean(key: String, value: Boolean): SharedPreferences.Editor {
            tempValues[key] = value
            return this
        }

        override fun putString(key: String, value: String?): SharedPreferences.Editor {
            tempValues[key] = value
            return this
        }

        override fun putLong(key: String, value: Long): SharedPreferences.Editor {
            tempValues[key] = value
            return this
        }

        override fun putInt(key: String, value: Int): SharedPreferences.Editor {
            tempValues[key] = value
            return this
        }

        override fun putFloat(key: String, value: Float): SharedPreferences.Editor {
            tempValues[key] = value
            return this
        }

        override fun putStringSet(
            key: String?,
            values: MutableSet<String>?
        ): SharedPreferences.Editor {
            return this
        }

        override fun commit(): Boolean {
            return false
        }

        override fun remove(key: String): SharedPreferences.Editor {
            tempValues.remove(key)
            return this
        }

        override fun apply() {
            values = tempValues
        }
    }
}
