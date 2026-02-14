package com.skye.wallpapers.wallpapers

import android.service.wallpaper.WallpaperService
import android.view.SurfaceHolder

class GravityBallWallpaperService: WallpaperService() {

    override fun onCreateEngine(): Engine {
        return WallpaperEngine()
    }

    inner class WallpaperEngine : Engine(),
        android.hardware.SensorEventListener {

        private var isVisible = false
        private val handler = android.os.Handler()
        private val frameDelay = 16L

        private lateinit var sensorManager: android.hardware.SensorManager
        private var accelerometer: android.hardware.Sensor? = null

        // Physics
        private var ax = 0f
        private var ay = 0f

        private var vx = 0f
        private var vy = 0f

        private var x = 0f
        private var y = 0f

        private val gravityScale = 0.6f
        private val damping = 0.98f
        private val bounce = 0.7f

        private val bgPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
        }

        private val circlePaint = android.graphics.Paint().apply {
            color = android.graphics.Color.DKGRAY
            style = android.graphics.Paint.Style.STROKE
            strokeWidth = 8f
            isAntiAlias = true
        }

        private val ballPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            isAntiAlias = true
        }

        override fun onCreate(holder: SurfaceHolder) {
            super.onCreate(holder)

            sensorManager = getSystemService(SENSOR_SERVICE) as android.hardware.SensorManager
            accelerometer =
                sensorManager.getDefaultSensor(android.hardware.Sensor.TYPE_ACCELEROMETER)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            isVisible = visible

            if (visible) {
                accelerometer?.also {
                    sensorManager.registerListener(
                        this,
                        it,
                        android.hardware.SensorManager.SENSOR_DELAY_GAME
                    )
                }
                handler.post(drawRunnable)
            } else {
                sensorManager.unregisterListener(this)
                handler.removeCallbacks(drawRunnable)
            }
        }

        private val drawRunnable = object : Runnable {
            override fun run() {
                drawFrame()
            }
        }

        private fun drawFrame() {

            val canvas = surfaceHolder.lockCanvas()
            if (canvas != null) {

                canvas.drawRect(
                    0f, 0f,
                    canvas.width.toFloat(),
                    canvas.height.toFloat(),
                    bgPaint
                )

                val centerX = canvas.width / 2f
                val centerY = canvas.height / 2f
                val outerRadius = canvas.width / 3f
                val ballRadius = outerRadius / 8f

                // Apply gravity acceleration
                vx += -ax * gravityScale
                vy += ay * gravityScale

                // Apply damping
                vx *= damping
                vy *= damping

                // Update position
                x += vx
                y += vy

                // Constrain inside circle
                val dx = x
                val dy = y
                val distance = kotlin.math.sqrt(dx * dx + dy * dy)
                val maxDistance = outerRadius - ballRadius

                if (distance > maxDistance) {
                    val nx = dx / distance
                    val ny = dy / distance

                    // Move ball back to edge
                    x = nx * maxDistance
                    y = ny * maxDistance

                    // Reflect velocity
                    val dot = vx * nx + vy * ny
                    vx -= 2 * dot * nx
                    vy -= 2 * dot * ny

                    vx *= bounce
                    vy *= bounce
                }

                canvas.drawCircle(centerX, centerY, outerRadius, circlePaint)

                canvas.drawCircle(
                    centerX + x,
                    centerY + y,
                    ballRadius,
                    ballPaint
                )

                surfaceHolder.unlockCanvasAndPost(canvas)
            }

            handler.removeCallbacks(drawRunnable)
            if (isVisible) handler.postDelayed(drawRunnable, frameDelay)
        }

        override fun onSensorChanged(event: android.hardware.SensorEvent?) {
            event?.let {
                ax = it.values[0]
                ay = it.values[1]
            }
        }

        override fun onAccuracyChanged(
            sensor: android.hardware.Sensor?,
            accuracy: Int
        ) {}
    }


}