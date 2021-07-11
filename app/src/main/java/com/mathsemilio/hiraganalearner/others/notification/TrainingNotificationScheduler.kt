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

package com.mathsemilio.hiraganalearner.others.notification

import android.content.Context
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import com.mathsemilio.hiraganalearner.common.TRAINING_NOTIFICATION_WORK_TAG
import com.mathsemilio.hiraganalearner.common.observable.BaseObservable
import java.util.concurrent.TimeUnit

class TrainingNotificationScheduler(
    private val context: Context
) : BaseObservable<TrainingNotificationScheduler.Listener>() {

    interface Listener {
        fun onTrainingNotificationScheduledSuccessfully(timeSetByUser: Long)

        fun onTrainingNotificationInvalidTimeSet()
    }

    fun checkTimeSetAndSchedule(timeSetInMillis: Long) {
        if (timeSetInMillis > System.currentTimeMillis()) {
            scheduleNotification(timeSetInMillis - System.currentTimeMillis())
            notifyTrainingNotificationScheduled(timeSetInMillis)
        } else {
            notifyTrainingNotificationInvalidTimeSet()
        }
    }

    fun cancelNotification() {
        WorkManager.getInstance(context).apply {
            cancelAllWorkByTag(TRAINING_NOTIFICATION_WORK_TAG)
        }
    }

    private fun scheduleNotification(initialDelay: Long) {
        val notifyUserWorkRequest = OneTimeWorkRequestBuilder<TrainingNotificationWorker>()
            .setInitialDelay(initialDelay, TimeUnit.MILLISECONDS)
            .addTag(TRAINING_NOTIFICATION_WORK_TAG)
            .build()

        WorkManager.getInstance(context).apply { enqueue(notifyUserWorkRequest) }
    }

    private fun notifyTrainingNotificationScheduled(timeSetInMillis: Long) {
        notifyListener { listener ->
            listener.onTrainingNotificationScheduledSuccessfully(timeSetInMillis)
        }
    }

    private fun notifyTrainingNotificationInvalidTimeSet() {
        notifyListener { listener ->
            listener.onTrainingNotificationInvalidTimeSet()
        }
    }
}