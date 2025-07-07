package com.example.senva

import android.content.Context
import com.example.senva.model.Familiar
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

object FamiliaUtils {
    fun guardarFamiliares(context: Context, familiares: List<Familiar>) {
        val prefs = context.getSharedPreferences("familiares_prefs", Context.MODE_PRIVATE)
        val json = Gson().toJson(familiares)
        prefs.edit().putString("familiares", json).apply()
    }
    fun cargarFamiliares(context: Context): MutableList<Familiar> {
        val prefs = context.getSharedPreferences("familiares_prefs", Context.MODE_PRIVATE)
        val json = prefs.getString("familiares", null)
        return if (json != null) {
            val type = object : TypeToken<MutableList<Familiar>>() {}.type
            Gson().fromJson(json, type)
        } else mutableListOf()
    }
} 