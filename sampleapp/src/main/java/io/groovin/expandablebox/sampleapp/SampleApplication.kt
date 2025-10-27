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
         * Note :
         * isAdjustPointerInputChangeOffsetForVelocityTrackerEnabled has issue that
         * nested composable scroll events are sometimes delivered in the opposite direction unintentionally.
         *
         * This issue is fixed on androidx.compose.foundation version 1.10.0-alpha02.
         */
        ComposeFoundationFlags.isAdjustPointerInputChangeOffsetForVelocityTrackerEnabled = false
    }
}
