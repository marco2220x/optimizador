package com.example.schedule

import android.graphics.Typeface
import android.os.Bundle
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.schedule.algorithms.ScheduledClass
import com.example.schedule.algorithms.executeBranchingSearch
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // Orden de los días de la semana para mostrar el horario
    private val daysOfWeekOrder = listOf("MONDAY", "TUESDAY", "WEDNESDAY", "THURSDAY", "FRIDAY")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Contenedor donde se mostrará el horario
        val scheduleContainer: LinearLayout = findViewById(R.id.scheduleContainer)

        // Generar el horario y agruparlo por días
        val scheduleOutput = generateSchedule()
        val groupedSchedule = groupScheduleByDay(scheduleOutput)

        // Mostrar el horario estilizado
        displaySchedule(groupedSchedule, scheduleContainer)
    }

    /**
     * Genera el horario usando el algoritmo de búsqueda definido.
     */
    private fun generateSchedule(): List<ScheduledClass> {
        executeBranchingSearch()
        return ScheduledClass.all.sortedBy { it.start }
    }

    /**
     * Agrupa las clases por día de la semana.
     */
    private fun groupScheduleByDay(schedule: List<ScheduledClass>): Map<String, List<ScheduledClass>> {
        val dailySchedule = mutableMapOf<String, MutableList<ScheduledClass>>()

        // Separar clases por día individual
        schedule.forEach { scheduledClass ->
            scheduledClass.daysOfWeek.forEach { day ->
                dailySchedule.computeIfAbsent(day.name) { mutableListOf() }.add(scheduledClass)
            }
        }

        return dailySchedule
    }

    /**
     * Muestra el horario agrupado por días en un contenedor `LinearLayout`.
     */
    private fun displaySchedule(schedule: Map<String, List<ScheduledClass>>, container: LinearLayout) {
        // Ordenar los días según el orden predefinido
        val sortedSchedule = schedule.toSortedMap(compareBy { daysOfWeekOrder.indexOf(it) })

        sortedSchedule.forEach { (day, classes) ->
            // Título del día
            val dayTextView = TextView(this).apply {
                text = day.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                textSize = 20f
                setTextColor(getColor(R.color.black))
                setTypeface(null, Typeface.BOLD)
                setPadding(0, 16, 0, 8)
            }
            container.addView(dayTextView)

            // Mostrar las clases para ese día
            classes.sortedBy { it.start }.forEach { scheduledClass ->
                val classTextView = TextView(this).apply {
                    text = getString(
                        R.string.class_schedule,
                        scheduledClass.name,
                        scheduledClass.start.toLocalTime().toString(),
                        scheduledClass.end.toLocalTime().toString()
                    )
                    textSize = 16f
                    setTextColor(getColor(R.color.gray))
                    setPadding(0, 8, 0, 8)
                }
                container.addView(classTextView)
            }
        }
    }
}
