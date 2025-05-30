package com.example.finalproject

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class ResetKcalReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val sharedPref = context.getSharedPreferences("UserExercise", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putFloat("todayKcal", 0f)
            apply()
        }
    }
}
