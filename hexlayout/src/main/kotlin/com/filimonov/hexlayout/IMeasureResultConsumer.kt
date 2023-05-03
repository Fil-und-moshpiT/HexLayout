package com.filimonov.hexlayout

interface IMeasureResultConsumer {

    /**
     * Updates information about center hex after measure
     */
    fun applyMeasureResult(measureResult: HexLayoutMeasureResult)
}
