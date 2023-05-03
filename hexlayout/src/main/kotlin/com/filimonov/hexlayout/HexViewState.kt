package com.filimonov.hexlayout

import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.view.animation.LinearInterpolator
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.Snapshot
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.round

/**
 * Represents drag state of hex layout
 */
internal class HexViewState {
    var dragged by mutableStateOf(IntOffset.Zero)
        private set

    var centerHexPosition = HexPosition.Zero
        private set

    private var centerHexCoordinates = IntOffset.Zero

    val modifier: Modifier = Modifier.pointerInput(Unit) {
        detectDragGestures(
            onDrag = onDrag,
            onDragEnd = onDragEnd,
            onDragCancel = onDragEnd
        )
    }

    private val onDrag: (PointerInputChange, Offset) -> Unit = { change, dragAmount ->
        change.consume()
        Snapshot.withoutReadObservation { dragged += dragAmount.round() }
    }

    private val onDragEnd: () -> Unit = {
        ValueAnimator.ofObject(IntOffsetEvaluator, dragged, -centerHexCoordinates).apply {
            duration = 200L
            interpolator = LinearInterpolator()

            addUpdateListener {
                val animated = it.animatedValue as IntOffset
                if (dragged != animated) {
                    dragged = animated
                }
            }
        }.also { it.start() }
    }

    /**
     * Updates information about center hex after each measure
     */
    @JvmName("setCenterHex") // todo: rename to applyMeasureResult
    fun setCenter(centerHexPosition: HexPosition, centerHexCoordinates: IntOffset) {
        if (this.centerHexPosition != centerHexPosition) {
            this.centerHexPosition = centerHexPosition
            this.centerHexCoordinates = centerHexCoordinates
        }
    }
}

private val IntOffsetEvaluator = TypeEvaluator<IntOffset> { fraction, startValue, endValue ->
    val x = startValue.x + fraction * (endValue.x - startValue.x)
    val y = startValue.y + fraction * (endValue.y - startValue.y)

    return@TypeEvaluator Offset(x, y).round()
}
