package com.example.groupproject

import java.util.TimerTask

class GameTimerTask : TimerTask {
    lateinit var activity: MainActivity

    constructor(activity: MainActivity) {
        this.activity = activity
    }

    override fun run() {
        activity.updateModel()
        activity.updateView()
    }
}