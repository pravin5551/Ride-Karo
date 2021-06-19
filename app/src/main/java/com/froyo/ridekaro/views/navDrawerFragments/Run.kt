package com.froyo.ridekaro.views.navDrawerFragments

import android.app.Activity
import android.os.Handler

class Run {
    companion object {
        fun afterOnMain(delay: Long, activity: Activity, process: () -> Unit) {
            Handler().postDelayed({
                activity.runOnUiThread {
                    Runnable {
                        process()
                    }
                }
            }, delay)
        }
    }
}