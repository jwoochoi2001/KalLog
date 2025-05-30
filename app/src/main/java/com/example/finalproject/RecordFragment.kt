package com.example.finalproject

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.text.SimpleDateFormat
import java.util.*

class RecordFragment : Fragment(R.layout.record) {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ExerciseRecordAdapter
    private val recordList = mutableListOf<ExerciseRecord>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        adapter = ExerciseRecordAdapter(recordList) { record ->
            deleteRecord(record)
        }
        recyclerView.adapter = adapter

        loadRecords()
    }

    private fun loadRecords() {
        val sharedPref = requireActivity().getSharedPreferences("ExerciseRecords", Context.MODE_PRIVATE)
        val json = sharedPref.getString("recordList", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<ExerciseRecord>>() {}.type
            val savedList: MutableList<ExerciseRecord> = Gson().fromJson(json, type)
            recordList.clear()
            recordList.addAll(savedList)
            adapter.notifyDataSetChanged()
        }
    }

    private fun deleteRecord(record: ExerciseRecord) {
        recordList.remove(record)
        adapter.notifyDataSetChanged()

        val recordPref = requireActivity().getSharedPreferences("ExerciseRecords", Context.MODE_PRIVATE)
        val json = Gson().toJson(recordList)
        recordPref.edit().putString("recordList", json).apply()

        val today = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
        val exercisePrefs = requireActivity().getSharedPreferences("UserExercise", Context.MODE_PRIVATE)
        val totalKcal = exercisePrefs.getFloat("totalKcal", 0f) - record.kcal.toFloat()
        exercisePrefs.edit().putFloat("totalKcal", totalKcal.coerceAtLeast(0f)).apply()

        if (record.date == today) {
            val todayKcal = exercisePrefs.getFloat("todayKcal", 0f) - record.kcal.toFloat()
            exercisePrefs.edit().putFloat("todayKcal", todayKcal.coerceAtLeast(0f)).apply()
        }
    }

    companion object {
        fun addRecord(context: Context, record: ExerciseRecord) {
            val sharedPref = context.getSharedPreferences("ExerciseRecords", Context.MODE_PRIVATE)
            val json = sharedPref.getString("recordList", null)
            val type = object : TypeToken<MutableList<ExerciseRecord>>() {}.type
            val list: MutableList<ExerciseRecord> =
                if (json != null) Gson().fromJson(json, type) else mutableListOf()
            list.add(record)
            val updatedJson = Gson().toJson(list)
            sharedPref.edit().putString("recordList", updatedJson).apply()
        }
    }
}
