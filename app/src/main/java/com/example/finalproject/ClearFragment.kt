package com.example.finalproject

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class ClearFragment : Fragment(R.layout.clear) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buttonRestart = view.findViewById<Button>(R.id.buttonRestart)

        buttonRestart.setOnClickListener {

            AlertDialog.Builder(requireContext())
                .setTitle("새로 시작")
                .setMessage("새롭게 다시 시작하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    clearAllUserData()

                    findNavController().navigate(R.id.home)
                }
                .setNegativeButton("아니요") { dialog, _ ->
                    dialog.dismiss()
                }
                .show()
        }
    }

    private fun clearAllUserData() {
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
