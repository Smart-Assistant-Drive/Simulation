package components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.loadImageBitmap
import androidx.compose.ui.res.useResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import model.domain.TrafficSign

@Suppress("ktlint:standard:function-naming", "DEPRECATION")
@Composable
fun TrafficSignCanvas(trafficSigns: Set<TrafficSign>) {
    val signSize = 80f // All signs will be this size

    Canvas(modifier = Modifier.fillMaxSize()) {
        for (sign in trafficSigns) {
            val bitmap =
                useResource(
                    when (sign.signType) {
                        "stop" -> "stop.jpg"
                        "yield" -> "yield.png"
                        // "speed_limit" -> "images/speed_limit_sign.png"
                        else -> "stop.jpg"
                    },
                ) { loadImageBitmap(it) }
            drawIntoCanvas { canvas ->
                // Calculate position
                val left = sign.position.x.toFloat() - signSize / 2
                val top = size.height - (sign.position.y.toFloat() + signSize / 2)
                val right = sign.position.x.toFloat() + signSize / 2
                val bottom = size.height - (sign.position.y.toFloat() - signSize / 2)

                // Draw image scaled to signSize x signSize
                canvas.drawImageRect(
                    image = bitmap,
                    dstOffset = IntOffset(left.toInt(), top.toInt()),
                    dstSize = IntSize(signSize.toInt(), signSize.toInt()),
                    paint = Paint(),
                )

                // Draw border
                val borderPaint =
                    Paint().apply {
                        style = androidx.compose.ui.graphics.PaintingStyle.Stroke
                        strokeWidth = 4f
                    }
                canvas.drawRect(
                    left = left,
                    top = top,
                    right = right,
                    bottom = bottom,
                    paint = borderPaint,
                )
            }
        }
    }
}
