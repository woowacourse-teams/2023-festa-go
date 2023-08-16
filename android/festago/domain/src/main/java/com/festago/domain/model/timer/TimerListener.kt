package com.festago.domain.model.timer

interface TimerListener {
    fun onTick(current: Int)
    fun onFinish()
}
