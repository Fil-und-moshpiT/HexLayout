package com.filimonov.test

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import com.filimonov.hexlayout.HexLayout
import com.filimonov.hexlayout.items

@Composable
fun HexLayoutTest() {
    val circles = 3
    val list = listOf(0, 1, 2, 3, 4, 5, 6, 7, 8, 9)

    HexLayout(circles = circles) {
        items(list) {
            Box(
                modifier = Modifier.fillMaxSize().clip(CircleShape).background(Color.Cyan),
                contentAlignment = Alignment.Center,
                content = { Text("$it") }
            )
        }
    }
}
