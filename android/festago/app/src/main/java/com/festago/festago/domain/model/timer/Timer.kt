package com.festago.festago.domain.model.timer

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.coroutines.coroutineContext

class Timer {

    private lateinit var job: Job

    var timerListener: TimerListener = object : TimerListener {
        override fun onTick(current: Int) = Unit
        override fun onFinish() = Unit
    }

    suspend fun start(period: Int) {
        if (::job.isInitialized) job.cancel()

        job = CoroutineScope(coroutineContext).launch {
            val startTime = System.currentTimeMillis()
            repeat(period) { usedTime ->
                timerListener.onTick(period - usedTime)
                val adjustment = System.currentTimeMillis() - startTime - INTERVAL * usedTime
                delay(INTERVAL - adjustment)
            }
            timerListener.onFinish()
        }
    }

    fun cancel() {
        if (::job.isInitialized) job.cancel()
    }

    companion object {
        private const val INTERVAL = 1000L
    }
}
