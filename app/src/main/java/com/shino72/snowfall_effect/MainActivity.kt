package com.shino72.snowfall_effect

import android.os.Bundle
import android.util.FloatMath
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.tooling.preview.Preview
import com.shino72.snowfall_effect.ui.theme.SnowfalleffectTheme
import kotlin.random.Random
import androidx.compose.runtime.*
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SnowfalleffectTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    androidx.compose.foundation.layout.Box(
                        modifier = Modifier
                            .fillMaxSize()
                    ) {
                        val configuration = LocalConfiguration.current
                        val density = LocalDensity.current
                        val screenHeight = with(density) { configuration.screenHeightDp.dp.toPx() }
                        com.shino72.snowfall_effect.SnowFallEffect(screenHeight = screenHeight)
                    }
                }
            }
        }
    }
}

data class Snow(
    var x : Float,
    var y : Float,
    var radius : Float,
    var speed : Float
)

private fun makeRandomSnow(screenHeight: Float) : Snow{
    return Snow(
        x = Random.nextFloat(),
        y = Random.nextFloat() * screenHeight * -1f,
        radius = Random.nextFloat() * 3f + 2f,
        speed = Random.nextFloat() * 1.2f + 1f
    )
}

@Composable
private fun SnowFallEffect(modifier: Modifier = Modifier , screenHeight : Float) {
    val snows = remember {
        List(100) { makeRandomSnow(screenHeight) }
    }
    val infiniteTransition = rememberInfiniteTransition()
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = screenHeight,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 10000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
    )

    val gradient = androidx.compose.ui.graphics.Brush.Companion.linearGradient(
        colors = listOf(Color.White, Color.Blue.copy(alpha = 0.3f), Color.Red.copy(alpha = 0.3f)),
        start = Offset.Zero,
        end = Offset.Infinite
    )

    Canvas(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
    ){
        snows.forEach{ snow ->
            drawSnow(snow,offsetY,screenHeight)
        }
    }

}

fun DrawScope.drawSnow(snow: Snow, offsetY: Float, screenHeight: Float) {
    var newOffsetY = snow.y + offsetY * snow.speed
    if (newOffsetY > size.height) {
        snow.y = Random.nextFloat() * - screenHeight
        newOffsetY = snow.y
    }
    drawCircle(Color.White, radius = snow.radius, center = Offset(snow.x * size.width, newOffsetY))
}