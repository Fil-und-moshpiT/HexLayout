package com.filimonov.benchmark

import android.content.Intent
import androidx.benchmark.macro.CompilationMode
import androidx.benchmark.macro.FrameTimingMetric
import androidx.benchmark.macro.StartupMode
import androidx.benchmark.macro.StartupTimingMetric
import androidx.benchmark.macro.junit4.MacrobenchmarkRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.uiautomator.By
import androidx.test.uiautomator.Direction
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class HexLayoutBenchmark {
    @get:Rule
    val benchmarkRule = MacrobenchmarkRule()

    @Test
    fun startup() = benchmarkRule.measureRepeated(
        packageName = PACKAGE_NAME,
        metrics = listOf(StartupTimingMetric()),
        iterations = DEFAULT_ITERATIONS,
        startupMode = StartupMode.COLD,
        setupBlock = { pressHome() }
    ) {
        startActivityAndWait()
    }

    @Test
    fun drag() = benchmarkRule.measureRepeated(
        packageName = PACKAGE_NAME,
        metrics = listOf(FrameTimingMetric()),
        compilationMode = CompilationMode.None(),
        startupMode = StartupMode.WARM, // restarts activity each iteration
        iterations = DEFAULT_ITERATIONS,
        setupBlock = {
            // Before starting to measure, navigate to the UI to be measured
            startActivityAndWait(Intent("$packageName.COMPOSE_ACTIVITY"))
        }
    ) {
        /*
        * Compose does not have view IDs so we cannot directly access composables from UiAutomator
        * To access a composable we need to set:
        * 1) Modifier.semantics { testTagsAsResourceId = true } once, high in the compose hierarchy
        * 2) Add Modifier.testTag("someIdentifier") to all of the composables you want to access
        *
        * Once done that, we can access the composable using By.res("someIdentifier")
        */
        device.findObject(By.res("hexLayout")).let { layout ->
            // Scroll down several times
            repeat(3) { layout.scroll(Direction.DOWN, 50f) }
        }

        // BEFORE OPTIMISATIONS:
        // emulator frameDurationCpuMs   P50   32.3,   P90   61.5,   P95   84.5,   P99  156.6
        // device   frameDurationCpuMs   P50   28.0,   P90   51.5,   P95   60.2,   P99   75.4
    }
}
