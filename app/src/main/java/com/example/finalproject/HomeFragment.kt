package com.example.finalproject

import android.app.AlarmManager
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.text.NumberFormat
import java.util.*

class HomeFragment : Fragment(R.layout.home) {

    private lateinit var textTodayKcal: TextView
    private lateinit var textAlert: TextView
    private lateinit var textGoalKcal: TextView
    private lateinit var textCurrentKcal: TextView
    private lateinit var textGreeting: TextView
    private lateinit var buttonClear: Button
    private lateinit var buttonReset: Button

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textTodayKcal = view.findViewById(R.id.todaykcal)
        textAlert = view.findViewById(R.id.textViewAlert)
        textGoalKcal = view.findViewById(R.id.textViewGoalKcal)
        textCurrentKcal = view.findViewById(R.id.textViewCurrentKcal)
        textGreeting = view.findViewById(R.id.textViewGreeting)
        buttonClear = view.findViewById(R.id.buttonClearGoal)
        buttonReset = view.findViewById(R.id.buttonReset)

        updateKcalData()
        updateName()

        buttonClear.setOnClickListener {
            val exercisePref = requireActivity().getSharedPreferences("UserExercise", Context.MODE_PRIVATE)
            val totalKcal = exercisePref.getFloat("totalKcal", 0f)

            val userInfoPref = requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
            val goalKcalStr = userInfoPref.getString("goalKcal", "0") ?: "0"
            val goalKcal = goalKcalStr.replace(",", "").replace("[^0-9]".toRegex(), "").toIntOrNull() ?: 0

            if (totalKcal.toInt() >= goalKcal) {
                findNavController().navigate(R.id.action_homeFragment_to_clearFragment)
            } else {
                AlertDialog.Builder(requireContext())
                    .setTitle("아직이에요")
                    .setMessage("조금만 더 힘내보세요! 목표까지 ${goalKcal - totalKcal.toInt()} kcal 남았어요.")
                    .setPositiveButton("확인", null)
                    .show()
            }
        }


        buttonReset.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_failFragment)
        }

        scheduleMidnightReset()
    }

    private fun updateName() {
        val profilePref = requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val name = profilePref.getString("name", "사용자")
        textGreeting.text = "${name}님,\n오늘도 화이팅!"
    }

    private fun updateKcalData() {
        val sharedPref = requireActivity().getSharedPreferences("UserExercise", Context.MODE_PRIVATE)
        val todayKcal = sharedPref.getFloat("todayKcal", 0f)
        val totalKcal = sharedPref.getFloat("totalKcal", 0f)

        val nf = NumberFormat.getNumberInstance(Locale.US)
        textTodayKcal.text = nf.format(todayKcal.toInt())
        textCurrentKcal.text = "누적 소모: ${nf.format(totalKcal.toInt())} kcal"

        val goalKcalPref = requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        val goalKcal = goalKcalPref.getString("goalKcal", "0")?.replace("[^0-9]".toRegex(), "")?.toIntOrNull() ?: 0
        textGoalKcal.text = "목표: ${nf.format(goalKcal)} kcal"

        when {
            todayKcal <= 500 -> {
                textAlert.text = "오늘 kcal 소비가 너무 적어요! 더 움직여요!"
                textAlert.setTextColor(resources.getColor(R.color.red, null))
            }
            todayKcal in 501.0..1499.9 -> {
                textAlert.text = "조금 더 힘내볼까요?"
                textAlert.setTextColor(resources.getColor(R.color.green, null))
            }
            todayKcal >= 1500 -> {
                textAlert.text = "완벽해요! 오늘 목표 달성!"
                textAlert.setTextColor(resources.getColor(R.color.light_blue, null))
            }
        }
    }


    private fun scheduleMidnightReset() {
        val intent = Intent(requireContext(), ResetKcalReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(), 0, intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            add(Calendar.DAY_OF_YEAR, 1)
        }

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }
}
