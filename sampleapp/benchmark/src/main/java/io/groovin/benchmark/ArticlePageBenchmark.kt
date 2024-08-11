package io.groovin.benchmark

import android.graphics.Point
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.MacrobenchmarkScope
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Until
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ArticlePageBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = "io.groovin.expandablebox.sampleapp",
        metrics = listOf(FrameTimingMetric()),
        iterations = 5,
        startupMode = StartupMode.WARM,
        setupBlock = {
            navigateArticlePageScreen()
        },
        measureBlock = {
            benchmarkScroll()
        }
    )

    private fun MacrobenchmarkScope.navigateArticlePageScreen() {
        pressHome()
        startActivityAndWait()
        device.wait(Until.hasObject(By.res("ArticlePageMenu")), 5_000)
        val menuButton = device.findObject(By.res("ArticlePageMenu"))
        menuButton.click()
        device.waitForIdle()
    }

    private fun MacrobenchmarkScope.benchmarkScroll() {
        device.wait(Until.hasObject(By.res("ArticlePageExpandableBox")), 5_000)
        val expandableBox = device.findObject(By.res("ArticlePageExpandableBox"))
        expandableBox.setGestureMargin(device.displayWidth / 5)
        expandableBox.drag(
            Point(expandableBox.visibleCenter.x, 0),
            600
        )
        device.waitForIdle()
    }
}
