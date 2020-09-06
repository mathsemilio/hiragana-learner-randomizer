package com.mathsemilio.hiraganalearner

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.mathsemilio.hiraganalearner.util.AlarmManagerHelper
import com.mathsemilio.hiraganalearner.util.SharedPreferencesTimeSet
import java.util.*

class AlarmBootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent != null) {
            if (intent.action == "android.intent.action.BOOT_COMPLETED") {
                if (context != null) {
                    val hourSet = SharedPreferencesTimeSet(context).retrieveHourSet()
                    val minuteSet = SharedPreferencesTimeSet(context).retrieveMinuteSet()
                    val timeSet = Calendar.getInstance().apply {
                        timeInMillis = System.currentTimeMillis()
                        set(Calendar.HOUR_OF_DAY, hourSet)
                        set(Calendar.MINUTE, minuteSet)
                    }

                    AlarmManagerHelper().setAlarmManager(context, timeSet)
                }
            }
        }
    }
}