package com.example.finalproject

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class ExerciseFragment : Fragment(R.layout.exercise) {

    private lateinit var spinnerExercise: Spinner
    private lateinit var imageExercise: ImageView
    private lateinit var textExerciseName: TextView
    private lateinit var editExerciseTime: EditText
    private lateinit var textKcalResult: TextView
    private lateinit var btnCalculateKcal: Button
    private lateinit var btnSubmitExercise: Button

    private var calculatedKcal: Float = 0f
    private var selectedExercise: String = "다이어트"

    private val exerciseList = listOf("걷기", "뛰기", "수영", "요가", "줄넘기", "조깅", "자전거", "클라이밍", "다이어트")
    private val exerciseImages = mapOf(
        "걷기" to R.drawable.walk,
        "뛰기" to R.drawable.run,
        "수영" to R.drawable.swim,
        "요가" to R.drawable.yoga,
        "줄넘기" to R.drawable.jumplope,
        "조깅" to R.drawable.jogging,
        "자전거" to R.drawable.bicycle,
        "클라이밍" to R.drawable.climbing,
        "다이어트" to R.drawable.diet
    )
    private val exerciseMET = mapOf(
        "걷기" to 3.8,
        "뛰기" to 9.8,
        "수영" to 8.0,
        "요가" to 2.5,
        "줄넘기" to 12.3,
        "조깅" to 7.0,
        "자전거" to 6.8,
        "클라이밍" to 8.0,
        "다이어트" to 1.0
    )

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        spinnerExercise = view.findViewById(R.id.spinnerExercise)
        imageExercise = view.findViewById(R.id.imageExercise)
        textExerciseName = view.findViewById(R.id.textExerciseName)
        editExerciseTime = view.findViewById(R.id.editExerciseTime)
        textKcalResult = view.findViewById(R.id.textKcalResult)
        btnCalculateKcal = view.findViewById(R.id.btnCalculateKcal)
        btnSubmitExercise = view.findViewById(R.id.btnSubmitExercise)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, exerciseList)
        spinnerExercise.adapter = adapter

        setExerciseInfo("다이어트")

        spinnerExercise.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                selectedExercise = exerciseList[position]
                setExerciseInfo(selectedExercise)
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        btnCalculateKcal.setOnClickListener {
            calculateKcal()
        }

        btnSubmitExercise.setOnClickListener {
            if (calculatedKcal <= 0f) {
                Toast.makeText(requireContext(), "먼저 '계산하기'를 눌러주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val time = editExerciseTime.text.toString().toIntOrNull() ?: 0
            val date = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val record = ExerciseRecord(date, selectedExercise, time, calculatedKcal.toInt())

            RecordFragment.addRecord(requireContext(), record)

            val sharedPref = requireActivity().getSharedPreferences("UserExercise", Context.MODE_PRIVATE)
            val todayKcal = sharedPref.getFloat("todayKcal", 0f) + calculatedKcal
            val totalKcal = sharedPref.getFloat("totalKcal", 0f) + calculatedKcal

            sharedPref.edit()
                .putFloat("todayKcal", todayKcal)
                .putFloat("totalKcal", totalKcal)
                .apply()

            Toast.makeText(requireContext(), "운동 기록이 저장되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun calculateKcal() {
        val timeInput = editExerciseTime.text.toString().toDoubleOrNull()
        if (timeInput == null || timeInput <= 0) {
            Toast.makeText(requireContext(), "운동 시간을 입력하세요.", Toast.LENGTH_SHORT).show()
            return
        }

        val met = exerciseMET[selectedExercise] ?: 0.0
        val sharedPref = requireActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE)
        val userWeight = sharedPref.getString("weight", "65")?.toDoubleOrNull() ?: 65.0

        val kcalBurned = if (met > 0.0) timeInput * met * userWeight * 0.0175 else 0.0
        calculatedKcal = kcalBurned.toFloat()
        textKcalResult.text = "총 소모 칼로리: %.1f kcal".format(calculatedKcal)
    }

    private fun setExerciseInfo(name: String) {
        textExerciseName.text = name
        val imageRes = exerciseImages[name] ?: R.drawable.diet
        imageExercise.setImageResource(imageRes)
    }
}
