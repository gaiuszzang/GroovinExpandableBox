package io.groovin.benchmark

import android.graphics.Point
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * This is an example startup benchmark.
 *
 * It navigates to the device's home screen, and launches the default activity.
 *
 * Before running this benchmark:
 * 1) switch your app's active build variant in the Studio (affects Studio runs only)
 * 2) add `<profileable android:shell="true" />` to your app's manifest, within the `<application>` tag
 *
 * Run this benchmark from Studio to see startup measurements, and captured system traces
 * for investigating your app's performance.
 */
@RunWith(AndroidJUnit4::class)
class MusicPlayerBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "io.groovin.expandablebox.sampleapp",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.WARM,
        setupBlock = {
            navigateMusicScreen()
        },
        measureBlock = {
            benchmarkScroll()
        }
    )

    private fun MacrobenchmarkScope.navigateMusicScreen() {
        pressHome()
        startActivityAndWait()
        device.wait(Until.hasObject(By.res("MusicPlayerMenu")), 5_000)
        val menuButton = device.findObject(By.res("MusicPlayerMenu"))
        menuButton.click()
        device.waitForIdle()
    }

    private fun MacrobenchmarkScope.benchmarkScroll() {
        device.wait(Until.hasObject(By.res("MusicExpandableBox")), 5_000)
        val expandableBox = device.findObject(By.res("MusicExpandableBox"))
        expandableBox.setGestureMargin(device.displayWidth / 5)
        expandableBox.drag(
            Point(expandableBox.visibleCenter.x, 0),
            300
        )
        device.waitForIdle()
    }
}
