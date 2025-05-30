package com.example.finalproject

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExerciseRecordAdapter(
    private val recordList: MutableList<ExerciseRecord>,
    private val onDeleteClick: (ExerciseRecord) -> Unit
) : RecyclerView.Adapter<ExerciseRecordAdapter.RecordViewHolder>() {

    inner class RecordViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textDate: TextView = itemView.findViewById(R.id.textDate)
        val textCategory: TextView = itemView.findViewById(R.id.textCategory)
        val textTime: TextView = itemView.findViewById(R.id.textTime)
        val textKcal: TextView = itemView.findViewById(R.id.textKcal)
        val buttonDelete: ImageButton = itemView.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_exercise_record, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = recordList[position]
        holder.textDate.text = record.date
        holder.textCategory.text = "운동: ${record.category}"
        holder.textTime.text = "시간: ${record.time}분"
        holder.textKcal.text = "소모: ${record.kcal} kcal"

        holder.buttonDelete.setOnClickListener {
            onDeleteClick(record)
        }
    }

    override fun getItemCount(): Int = recordList.size
}
