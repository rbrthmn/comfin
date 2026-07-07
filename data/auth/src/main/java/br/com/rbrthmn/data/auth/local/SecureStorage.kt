package br.com.rbrthmn.data.auth.local

interface SecureStorage {
    fun putString(key: String, value: String)
    fun getString(key: String): String?
    fun remove(vararg keys: String)
}
