package com.example.finalproject

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class FailFragment : Fragment(R.layout.fail) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val resetButton = view.findViewById<Button>(R.id.buttonRestart)
        val exitButton = view.findViewById<Button>(R.id.buttonRestartOut)

        resetButton.setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("RESET")
                .setMessage("정말 모든 데이터를 초기화하고 다시 시작하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    resetAllData()
                    Toast.makeText(requireContext(), "초기화 완료! 다시 시작합니다.", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.home)
                }
                .setNegativeButton("아니요", null)
                .show()
        }

        exitButton.setOnClickListener {
            findNavController().navigate(R.id.home)
        }
    }

    private fun resetAllData() {
        val prefsList = listOf(
            "UserProfile",
            "UserExercise",
            "ExerciseRecords"
        )

        for (prefName in prefsList) {
            val pref = requireActivity().getSharedPreferences(prefName, Context.MODE_PRIVATE)
            pref.edit().clear().apply()
        }
    }
}
