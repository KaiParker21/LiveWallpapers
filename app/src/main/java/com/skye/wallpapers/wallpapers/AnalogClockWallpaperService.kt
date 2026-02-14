package com.skye.wallpapers.wallpapers

import android.service.wallpaper.WallpaperService

class AnalogClockWallpaperService : WallpaperService() {

    override fun onCreateEngine(): Engine {
        return WallpaperEngine()
    }

    inner class WallpaperEngine : Engine() {

        private var isVisible = false
        private val handler = android.os.Handler()
        private val frameDelay = 16L

        private val bgPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.BLACK
        }

        private val circlePaint = android.graphics.Paint().apply {
            color = android.graphics.Color.argb(40, 255, 255, 255)
            style = android.graphics.Paint.Style.STROKE
            strokeWidth = 4f
            isAntiAlias = true
        }

        private val hourPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            strokeWidth = 10f
            strokeCap = android.graphics.Paint.Cap.ROUND
            isAntiAlias = true
        }

        private val minutePaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            strokeWidth = 6f
            strokeCap = android.graphics.Paint.Cap.ROUND
            isAntiAlias = true
        }

        private val secondPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            strokeWidth = 3f
            strokeCap = android.graphics.Paint.Cap.ROUND
            isAntiAlias = true
        }

        private val centerDotPaint = android.graphics.Paint().apply {
            color = android.graphics.Color.WHITE
            isAntiAlias = true
        }

        override fun onVisibilityChanged(visible: Boolean) {
            isVisible = visible
            if (visible) handler.post(drawRunnable)
            else handler.removeCallbacks(drawRunnable)
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
                val radius = canvas.width / 3f

                canvas.drawCircle(centerX, centerY, radius, circlePaint)

                val calendar = java.util.Calendar.getInstance()

                val hour = calendar.get(java.util.Calendar.HOUR)
                val minute = calendar.get(java.util.Calendar.MINUTE)
                val second = calendar.get(java.util.Calendar.SECOND)
                val millisecond = calendar.get(java.util.Calendar.MILLISECOND)

                val secondProgress = second + millisecond / 1000f
                val minuteProgress = minute + secondProgress / 60f
                val hourProgress = hour + minuteProgress / 60f

                val secondAngle = secondProgress * 6f
                val minuteAngle = minuteProgress * 6f
                val hourAngle = hourProgress * 30f

                drawHand(canvas, centerX, centerY, radius * 0.5f, hourAngle, hourPaint)
                drawHand(canvas, centerX, centerY, radius * 0.75f, minuteAngle, minutePaint)
                drawHand(canvas, centerX, centerY, radius * 0.9f, secondAngle, secondPaint)

                canvas.drawCircle(centerX, centerY, 12f, centerDotPaint)

                surfaceHolder.unlockCanvasAndPost(canvas)
            }

            handler.removeCallbacks(drawRunnable)
            if (isVisible) handler.postDelayed(drawRunnable, frameDelay)
        }

        private fun drawHand(
            canvas: android.graphics.Canvas,
            cx: Float,
            cy: Float,
            length: Float,
            angle: Float,
            paint: android.graphics.Paint
        ) {
            val radians = Math.toRadians(angle.toDouble() - 90)
            val x = (cx + length * kotlin.math.cos(radians)).toFloat()
            val y = (cy + length * kotlin.math.sin(radians)).toFloat()

            canvas.drawLine(cx, cy, x, y, paint)
        }
    }



}


