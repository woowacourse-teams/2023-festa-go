package com.festago.festago.model.timer

interface TimerListener {
    fun onTick(current: Int)
    fun onFinish()
}
