package com.example.appointmentschedule

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key.Companion.Calendar
import androidx.compose.ui.tooling.preview.Preview
import com.example.appointmentschedule.ui.theme.AppointmentScheduleTheme
import java.time.YearMonth
import java.util.Calendar

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        java.util.Calendar.getInstance().getActualMaximum(java.util.Calendar.DAY_OF_MONTH)
        setContent {
            AppointmentScheduleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String) {
    Text(text = "Hello $name!")
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AppointmentScheduleTheme {
        Greeting("Android")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun main() {
    val daysOfMonth: YearMonth = YearMonth.of(2023, 2)
    val mCalendar = java.util.Calendar.getInstance()
    println(daysOfMonth.lengthOfMonth())
    println(mCalendar.getActualMaximum(java.util.Calendar.DAY_OF_MONTH))
}