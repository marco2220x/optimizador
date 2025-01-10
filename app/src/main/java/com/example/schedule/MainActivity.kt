package com.example.schedule

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.schedule.algorithms.ScheduledClass
import com.example.schedule.algorithms.executeBranchingSearch

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Generar el horario
        val scheduleOutput = generateSchedule()

        // Mostrar el horario en el TextView
        val textView: TextView = findViewById(R.id.scheduleTextView)
        textView.text = scheduleOutput
    }

    // Función para generar el horario en formato String
    private fun generateSchedule(): String {
        val output = StringBuilder()

        executeBranchingSearch() // Ejecuta tu lógica de búsqueda

        // Ordenar y formatear las clases
        ScheduledClass.all.sortedBy { it.start }.forEach {
            output.append("${it.name}- ${it.daysOfWeek.joinToString("/")} ${it.start.toLocalTime()}-${it.end.toLocalTime()}\n")
        }

        return output.toString()
    }
}
