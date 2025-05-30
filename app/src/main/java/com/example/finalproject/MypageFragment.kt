package com.example.finalproject

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import java.util.Locale
import java.text.NumberFormat

class MypageFragment : Fragment(R.layout.mypage) {

    private lateinit var textGreeting: TextView
    private lateinit var textName: TextView
    private lateinit var textBirth: TextView
    private lateinit var textSex: TextView
    private lateinit var textHeight: TextView
    private lateinit var textWeight: TextView
    private lateinit var textGoalWeight: TextView
    private lateinit var textGoalKcal: TextView

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        textGreeting = view.findViewById(R.id.textViewGreeting)
        textName = view.findViewById(R.id.name)
        textBirth = view.findViewById(R.id.birth)
        textSex = view.findViewById(R.id.sex)
        textHeight = view.findViewById(R.id.height)
        textWeight = view.findViewById(R.id.weight)
        textGoalWeight = view.findViewById(R.id.goal)
        textGoalKcal = view.findViewById(R.id.goal_kcal)

        view.findViewById<View>(R.id.buttonEditProfile).setOnClickListener {
            findNavController().navigate(R.id.action_mypage_to_information)
        }

        view.findViewById<View>(R.id.buttonEditDelete).setOnClickListener {
            AlertDialog.Builder(requireContext())
                .setTitle("초기화 확인")
                .setMessage("개인정보를 모두 초기화하시겠습니까?")
                .setPositiveButton("예") { _, _ ->
                    val sharedPref = requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE)
                    sharedPref.edit().clear().apply()
                    updateUserInfo("", "", "", "", "", "", "")
                }
                .setNegativeButton("아니요", null)
                .show()
        }

        loadUserInfo()
    }

    private fun loadUserInfo() {
        val sharedPref = requireContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE)

        val name = sharedPref.getString("name", "") ?: ""
        val birth = sharedPref.getString("birth", "") ?: ""
        val sex = sharedPref.getString("sex", "") ?: ""
        val height = sharedPref.getString("height", "") ?: ""
        val weight = sharedPref.getString("weight", "") ?: ""
        val goalWeight = sharedPref.getString("goalWeight", "") ?: ""
        val goalKcal = sharedPref.getString("goalKcal", "") ?: ""

        updateUserInfo(name, birth, sex, height, weight, goalWeight, goalKcal)
    }

    private fun updateUserInfo(
        name: String,
        birth: String,
        sex: String,
        height: String,
        weight: String,
        goalWeight: String,
        goalKcal: String
    ) {
        val formattedKcal = try {
            val kcalInt = goalKcal.replace(",", "").toInt()
            NumberFormat.getNumberInstance(Locale.US).format(kcalInt)
        } catch (e: NumberFormatException) {
            goalKcal
        }

        textGreeting.text = "${name}님, 안녕하세요!"
        textName.text = "이름: $name"
        textBirth.text = "생년월일: $birth"
        textSex.text = "성별: $sex"
        textHeight.text = "신장 (cm): $height"
        textWeight.text = "체중 (kg): $weight"
        textGoalWeight.text = "목표 체중 (kg): $goalWeight"
        textGoalKcal.text = "목표 kcal: $formattedKcal kcal"
    }
}
