package com.igor.jetpackcompose.achedualCalendar

import android.annotation.SuppressLint
import android.content.Context
import android.icu.util.HebrewCalendar
import android.util.Log
import android.util.SparseArray
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.KeyboardArrowLeft
import androidx.compose.material.icons.outlined.KeyboardArrowRight
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appointmentschedule.R
import net.sourceforge.zmanim.hebrewcalendar.HebrewDateFormatter
import net.sourceforge.zmanim.hebrewcalendar.JewishCalendar
import net.sourceforge.zmanim.hebrewcalendar.JewishDate
import java.util.*


@Composable
fun ScheduleCalendarWrapper() {
    val mCalendar = remember { mutableStateOf(Calendar.getInstance()) }
    // mCalendar.value.set(Calendar.MONTH, 1)
    val mSelectedMonth = remember { mutableStateOf(mCalendar.value.get(Calendar.MONTH)) }

    LaunchedEffect(key1 = mSelectedMonth.value, block = {
        Log.d(
            "IgorTest",
            "Selected month is ${mSelectedMonth.value} \n year is ${mCalendar.value.get(android.icu.util.Calendar.YEAR)}"
        )

        when (mSelectedMonth.value) {
            12 -> {
                val year = mCalendar.value.get(Calendar.YEAR)
                mCalendar.value.set(Calendar.YEAR, year + 1)
                mSelectedMonth.value = 0
            }
            -1 -> {
                val year = mCalendar.value.get(Calendar.YEAR)
                mCalendar.value.set(Calendar.YEAR, year -1)
                mSelectedMonth.value = 11
            }

        }
    })

    MaterialTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ScheduleCalendarMontHeader(mSelectedMonth = mSelectedMonth, mCalendar = mCalendar)
                Spacer(modifier = Modifier.padding(30.dp))
                ScheduleCalendar(month = mSelectedMonth, mCalendar = mCalendar)
            }
        }
    }

}

@OptIn(ExperimentalFoundationApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ScheduleCalendar(month: MutableState<Int>, mCalendar: MutableState<Calendar>) {
    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {

        Log.d("IgorTest", "ScheduleCalendar month is : ${month.value}")
        //mCalendar.value.set(Calendar.MONTH, month.value)
        var monthDaysNumber = mCalendar.value.getActualMaximum(Calendar.DAY_OF_MONTH)

        val mFirstDatOfMonth = getFirstDateOfMonth(month = month.value, mCalendar.value.get(Calendar.YEAR))

        monthDaysNumber += (mFirstDatOfMonth - 1)


        LazyVerticalGrid(
            columns = GridCells.Adaptive(minSize = 50.dp), modifier = Modifier.fillMaxWidth()
        ) {
            items(HebrewDateLetters.values().size) { itemIndex ->

                val heb: HebrewDateLetters = HebrewDateLetters.values()[itemIndex]
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(15.dp)
                ) {
                    Text(text = heb.getHebrewLetter())
                }


            }

            items(monthDaysNumber) { day ->
                if (day < mFirstDatOfMonth - 1) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(15.dp)
                    ) {
                        Text(text = " ")
                    }
                } else {
                    ScheduleCalendarItem(
                        day = (day + 1) - (mFirstDatOfMonth - 1),
                        dae = mCalendar.value,
                        itemState = ScheduleCalendarItemState.ENABLE
                    )
                }
            }
        }
    }


}

@Composable
fun ScheduleCalendarMontHeader(mSelectedMonth: MutableState<Int>, mCalendar: MutableState<Calendar>) {

    val mContext = LocalContext.current
    //mCalendar.value.set(Calendar.MONTH, mSelectedMonth.value)

    var mHebrewMonthName by remember {
        mutableStateOf(
            getMonthName(
                context = mContext,
                month = mSelectedMonth.value
            ) + " ${mCalendar.value.get(Calendar.YEAR)}"
        )
    }

    LaunchedEffect(key1 = mSelectedMonth.value, block = {
        mHebrewMonthName = getMonthName(
            context = mContext,
            month = mSelectedMonth.value
        ) + " ${mCalendar.value.get(Calendar.YEAR)}"
    })

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowLeft,
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    mSelectedMonth.value += 1
                }
        )
        Text(text = mHebrewMonthName, modifier = Modifier.wrapContentWidth(), fontSize = 30.sp)
        Icon(
            imageVector = Icons.Outlined.KeyboardArrowRight,
            contentDescription = "",
            modifier = Modifier
                .size(40.dp)
                .clickable {
                    mSelectedMonth.value -= 1
                }
        )
    }

}


@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ScheduleCalendarItem(day: Int, dae: Calendar, itemState: ScheduleCalendarItemState) {
    Card(modifier = Modifier
        .padding(10.dp)
        .wrapContentSize(),
        backgroundColor = Color.Transparent,
        shape = RoundedCornerShape(if (itemState == ScheduleCalendarItemState.SELECTED) 100 else 0),
        onClick = {
            //TODO.. item click
        }) {
        Box(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = day.toString())
                Text(text = "ב׳")
            }
        }
    }

}


fun getHebrewDate(date: Date) {

    val cal = HebrewCalendar()
    //cal.getDisplayName()
    cal.getDateTimeFormat(1, 1, Locale("he"))
}

@Preview(showBackground = true)
@Preview(device = "spec:width=1920dp,height=1080dp,dpi=960")
@Composable
fun PreviewCalendar() {
    MaterialTheme {
        CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Ltr) {

            ScheduleCalendarWrapper()

        }
    }
}

enum class ScheduleCalendarItemState {
    SELECTED, ENABLE, DISABLE
}


enum class HebrewDateLetters {
    SUNDAY, MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY, SATURDAY;


}

fun HebrewDateLetters.getHebrewLetter(): String {
    return when (this) {
        HebrewDateLetters.SUNDAY -> "א׳"
        HebrewDateLetters.MONDAY -> "ב׳"
        HebrewDateLetters.TUESDAY -> "ג׳"
        HebrewDateLetters.WEDNESDAY -> "ד׳"
        HebrewDateLetters.THURSDAY -> "ה׳"
        HebrewDateLetters.FRIDAY -> "ו׳"
        HebrewDateLetters.SATURDAY -> "ש׳"
    }
}
//
//fun main() {
//    val daysOfMonth : YearMonth = YearMonth.of(2023, 5)
//    println(daysOfMonth.lengthOfMonth())
//}

@SuppressLint("SuspiciousIndentation")
fun main() {
//    CoroutineScope(Dispatchers.IO).launch {
//        val cal = HebrewCalendar.getInstance()
//        launch(Dispatchers.Main) {
//            val sd = SimpleDateFormat("EEEE")
//            // cal.getDisplayName()
//            val hDate = sd.format(cal.time)
//            // val dd = cal.getDateTimeFormat(1, 1, ULocale.Category.FORMAT)
//            println("hDate= $hDate")
//            println(".......******.......")
//        }
//
//    }


    val jd: JewishDate = JewishCalendar() // current date 23 Nissan, 5773
    val hdf = HebrewDateFormatter()
    hdf.isHebrewFormat = true
    jd.setDate(Calendar.getInstance().time) // set the date to 21 Shevat, 5729

    val hDate = getHebrewDay(
        hdf.formatHebrewNumber(jd.jewishDayOfMonth),
        hdf.formatMonth(jd)
    )

    val mCalendar = Calendar.getInstance()
    val monthDaysNumber = mCalendar.getActualMaximum(Calendar.DAY_OF_WEEK_IN_MONTH)
    val currentMonth = mCalendar.get(Calendar.MONTH)
    val dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK)
    val firstDayOfWeek = mCalendar.get(Calendar.DAY_OF_MONTH)
    println("hDate is : $hDate")
    println("currentMonth is : $currentMonth")
    println("monthDaysNumber is : $monthDaysNumber")
    println("dayOfWeek is : $dayOfWeek")
    println("firstDayOfWeek is : ${getFirstDateOfMonth(1, 2023)}")

}

fun formatStringParams(vararg params: String): String? {
    var result = ""
    for (param in params) {
        if (param.isNotEmpty()) {
            result += "$param "
        }
    }
    return result
}

fun getHebrewDay(vararg params: String): String? {

    return params[0]

}

private fun getDayOfWeek(): Int {
    val mCalendar = Calendar.getInstance()
    val dayOfWeek = mCalendar.get(Calendar.DAY_OF_WEEK)
    return dayOfWeek

}

private fun getFirstDateOfMonth(month: Int, year : Int): Int {
    val mCalendar = Calendar.getInstance()
    // mCalendar.set(Calendar.MONTH, if(month==0) 1 else month)
    mCalendar.set(Calendar.MONTH, month)
    mCalendar.set(Calendar.YEAR, year)
    mCalendar.set(Calendar.DAY_OF_MONTH, 1)
    Log.d("IgorTest", "getFirstDayOfWeek is = ${mCalendar.get(Calendar.DAY_OF_WEEK)}")
    return mCalendar.get(Calendar.DAY_OF_WEEK)
}


private fun getMonthDays(monthIndex: Int): Int {
    val mCalendar = Calendar.getInstance()
    mCalendar.set(Calendar.MONTH, monthIndex)
    return mCalendar.getActualMaximum(Calendar.DAY_OF_MONTH)
}

fun getMonthName(context: Context, month: Int?): String? {
    val monthNames = SparseArray<String?>()
    monthNames.put(0, context.getString(R.string.January))
    monthNames.put(1, context.getString(R.string.February))
    monthNames.put(2, context.getString(R.string.March))
    monthNames.put(3, context.getString(R.string.April))
    monthNames.put(4, context.getString(R.string.May))
    monthNames.put(5, context.getString(R.string.June))
    monthNames.put(6, context.getString(R.string.July))
    monthNames.put(7, context.getString(R.string.August))
    monthNames.put(8, context.getString(R.string.September))
    monthNames.put(9, context.getString(R.string.October))
    monthNames.put(10, context.getString(R.string.November))
    monthNames.put(11, context.getString(R.string.December))
    return if (monthNames[month!!] != null) {
        monthNames.valueAt(month)
    } else null
}