package com.mathsemilio.hiraganalearner.others

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.mathsemilio.hiraganalearner.data.repository.PreferencesRepository

/**
 * Worker class for handling the work to be done with the WorkManager.
 */
class NotificationWorker(appContext: Context, workerParams: WorkerParameters) :
    Worker(appContext, workerParams) {

    override fun doWork(): Result {
        NotificationUtil.notifyUser(applicationContext)
        PreferencesRepository(applicationContext).saveNotificationSwitchState(false)

        return Result.success()
    }
}