package com.example.ensuretrackapplication

import android.content.Context
import android.content.SharedPreferences

class SessionClass(var context: Context) {
    private var pref: SharedPreferences =
        context.getSharedPreferences("PREF_NAME", Context.MODE_PRIVATE)


    fun getTheme(): Boolean {
        return pref.getBoolean("theme", false)
    }

    fun setTheme(value: Boolean) {
        pref.edit().putBoolean("theme", value).apply()
    }

    fun getLogin(): String? {
        return pref.getString("login", "")
    }

    fun setLogin(value: String) {
        pref.edit().putString("login", value).apply()
    }

    fun getname(): String? {
        return pref.getString("name", "")
    }

    fun setname(value: String) {
        pref.edit().putString("name", value).apply()
    }
    fun getemail(): String? {
        return pref.getString("email", "")
    }

    fun setemail(value: String) {
        pref.edit().putString("email", value).apply()
    }
    fun getnumber(): String? {
        return pref.getString("number", "")
    }

    fun setnumber(value: String) {
        pref.edit().putString("number", value).apply()
    }
    fun getpassword(): String? {
        return pref.getString("password", "")
    }

    fun setpassword(value: String) {
        pref.edit().putString("password", value).apply()
    }
    fun getUser(): String? {
        return pref.getString("user", "")
    }

    fun setUser(value: String) {
        pref.edit().putString("user", value).apply()
    }


}