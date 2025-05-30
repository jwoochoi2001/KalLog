package com.example.finalproject

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class InformationFragment : Fragment() {

    private lateinit var nameEditText: EditText
    private lateinit var birthEditText: EditText
    private lateinit var sexSpinner: Spinner
    private lateinit var heightEditText: EditText
    private lateinit var weightEditText: EditText
    private lateinit var goalWeightEditText: EditText
    private lateinit var kcalResultTextView: TextView
    private lateinit var calculateButton: Button
    private lateinit var saveButton: Button
    private lateinit var exitButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.information, container, false)

        nameEditText = view.findViewById(R.id.Name)
        birthEditText = view.findViewById(R.id.Birth)
        sexSpinner = view.findViewById(R.id.Sex)
        heightEditText = view.findViewById(R.id.Height)
        weightEditText = view.findViewById(R.id.Weight)
        goalWeightEditText = view.findViewById(R.id.GoalWeight)
        kcalResultTextView = view.findViewById(R.id.Goalkacl)
        calculateButton = view.findViewById(R.id.kcalCount)
        saveButton = view.findViewById(R.id.buttonSave)
        exitButton = view.findViewById(R.id.buttonout) // 🔹 나가기 버튼 초기화

        val sexOptions = arrayOf("남성", "여성")
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, sexOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sexSpinner.adapter = adapter

        calculateButton.setOnClickListener {
            calculateTargetKcal()
        }

        saveButton.setOnClickListener {
            saveUserInfo()
            findNavController().navigate(R.id.action_informationFragment_to_mypageFragment)
        }

        exitButton.setOnClickListener {
            findNavController().navigate(R.id.action_informationFragment_to_mypageFragment)
        }

        return view
    }

    private fun calculateTargetKcal() {
        val height = heightEditText.text.toString().toDoubleOrNull()
        val weight = weightEditText.text.toString().toDoubleOrNull()
        val goalWeight = goalWeightEditText.text.toString().toDoubleOrNull()

        if (height != null && weight != null && goalWeight != null) {
            val weightDiff = weight - goalWeight
            val kcalPerKg = 7700
            val targetKcal = (weightDiff * kcalPerKg).toInt()
            kcalResultTextView.text = "목표 kcal : $targetKcal kcal"
        } else {
            Toast.makeText(requireContext(), "모든 항목을 올바르게 입력하세요.", Toast.LENGTH_SHORT).show()
        }
    }

    private fun saveUserInfo() {
        val sharedPref = requireActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString("name", nameEditText.text.toString())
            putString("birth", birthEditText.text.toString())
            putString("sex", sexSpinner.selectedItem.toString())
            putString("height", heightEditText.text.toString())
            putString("weight", weightEditText.text.toString())
            putString("goalWeight", goalWeightEditText.text.toString())
            putString("goalKcal", kcalResultTextView.text.toString().replace("목표 kcal : ", "").replace(" kcal", ""))
            apply()
        }
        Toast.makeText(requireContext(), "저장 완료!", Toast.LENGTH_SHORT).show()
    }
}
