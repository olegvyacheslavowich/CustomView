package ru.simaland.customview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val view = findViewById<StatsView>(R.id.statsView)
        view.data = listOf(
            40F,
            40F,
            40F,
            40F
        )
    }
}