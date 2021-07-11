/*
Copyright 2020 Matheus Menezes

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.mathsemilio.hiraganalearner.ui.dialog.timepicker

import android.app.Dialog
import android.app.TimePickerDialog
import android.content.DialogInterface
import android.os.Bundle
import android.widget.TimePicker
import com.mathsemilio.hiraganalearner.common.eventbus.EventPublisher
import com.mathsemilio.hiraganalearner.ui.dialog.BaseDialogFragment
import com.mathsemilio.hiraganalearner.ui.common.event.TimePickerDialogEvent
import java.util.*

class TimePickerDialog : BaseDialogFragment(), TimePickerDialog.OnTimeSetListener {

    private lateinit var calender: Calendar

    private lateinit var eventPublisher: EventPublisher

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        eventPublisher = compositionRoot.eventPublisher
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        calender = Calendar.getInstance()
        val hourOfDay = calender.get(Calendar.HOUR_OF_DAY)
        val minute = calender.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(
            requireContext(),
            this,
            hourOfDay,
            minute,
            android.text.format.DateFormat.is24HourFormat(requireContext())
        )

        return timePickerDialog.apply {
            setCancelable(false)
            setCanceledOnTouchOutside(false)
        }
    }

    override fun onTimeSet(view: TimePicker?, hourOfDay: Int, minute: Int) {
        val timeSet = calender.apply {
            set(Calendar.HOUR_OF_DAY, hourOfDay)
            set(Calendar.MINUTE, minute)
        }

        eventPublisher.publish(TimePickerDialogEvent.TimeSet(timeSet.timeInMillis))
    }

    override fun onCancel(dialog: DialogInterface) {
        eventPublisher.publish(TimePickerDialogEvent.Dismissed)
    }
}