package com.rasen.leapyearnotice

import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Build
import android.os.CountDownTimer
import android.widget.TextView
import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import java.util.Calendar

class MainActivity : AppCompatActivity() {

    private var notificacion: ReminderNotification = ReminderNotification(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textViewResult: TextView = findViewById(R.id.textViewMessage)

        // Inicializa el contador
        startCountdown(textViewResult)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

    }

    private fun startCountdown(textView: TextView) {
        val nextLeapYearDate = getNextLeapYearDate()

        val timer = object : CountDownTimer(nextLeapYearDate.timeInMillis - Calendar.getInstance().timeInMillis, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val seconds = millisUntilFinished / 1000 % 60
                val minutes = millisUntilFinished / (60 * 1000) % 60
                val hours = millisUntilFinished / (60 * 60 * 1000) % 24
                val days = millisUntilFinished / (24 * 60 * 60 * 1000)
                val years = days / 365
                val daysRemaining = days % 365

                textView.text =
                    "Faltan $years años, $daysRemaining días, $hours horas, $minutes minutos, $seconds segundos para el próximo año bisiesto."
            }

            override fun onFinish() {
                textView.text = "¡Es año bisiesto!"
            }
        }

        timer.start()
    }

    private fun getNextLeapYearDate(): Calendar {
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        var nextLeapYear = currentYear

        // Encontrar el próximo año bisiesto
        while (!isLeapYear(nextLeapYear)) {
            nextLeapYear++
        }

        // Crear la fecha del próximo año bisiesto
        return Calendar.getInstance().apply {
            set(nextLeapYear, Calendar.JANUARY, 1, 0, 0, 0)
            set(Calendar.MILLISECOND, 0)
        }
    }

    private fun isLeapYear(year: Int): Boolean {
        return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)
    }
}
