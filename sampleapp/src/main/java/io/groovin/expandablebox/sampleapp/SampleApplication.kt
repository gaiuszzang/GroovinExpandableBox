package io.groovin.expandablebox.sampleapp

import android.app.Application
import androidx.compose.foundation.ComposeFoundationFlags
import androidx.compose.foundation.ExperimentalFoundationApi

class SampleApplication: Application() {
    override fun onCreate() {
        initComposeFlags()
        super.onCreate()
    }



    @OptIn(ExperimentalFoundationApi::class)
    private fun initComposeFlags() {
        /**
         * isAdjustPointerInputChangeOffsetForVelocityTrackerEnabled has issue that
         * nested composable scroll events are sometimes delivered in the opposite direction unintentionally.
         */
        ComposeFoundationFlags.isAdjustPointerInputChangeOffsetForVelocityTrackerEnabled = false
    }
}
