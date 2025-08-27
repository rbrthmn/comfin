package br.com.rbrthmn.ui.utils

import android.content.Context
import androidx.annotation.StringRes

interface StringProvider {
    fun getString(@StringRes stringId: Int): String
}

class ResourceStringProvider(private val context: Context) : StringProvider {
    override fun getString(@StringRes stringId: Int): String {
        return context.getString(stringId)
    }
}
