package com.compscieddy.newclusterprototype

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.Shader
import android.util.AttributeSet
import android.view.View


class GradientSpotlightView : View {
  private var gradientPaint: Paint? = null
  private var width = 0
  private var height = 0
  private var spotlightRadius = 0f

  constructor(context: Context?) : super(context) {
    init()
  }

  constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
    init()
  }

  constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
    context,
    attrs,
    defStyleAttr) {
    init()
  }

  private fun init() {
    gradientPaint = Paint()
    gradientPaint!!.isAntiAlias = true
    spotlightRadius = 0.5f // Default radius (50% of the smallest dimension)
  }

  fun setSpotlightRadius(radius: Float) {
    spotlightRadius = radius
    invalidate() // Redraw the view with the new radius
  }

  override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
    super.onSizeChanged(w, h, oldw, oldh)
    width = w
    height = h

    // Create a radial gradient with transparency in the center
    val gradient = RadialGradient(
      (
          width / 2).toFloat(),
      (
          height / 2).toFloat(),
      Math.min(width, height) * spotlightRadius,
      intArrayOf(0x00FFFFFF, 0x00FFFFFF, -0x1),
      floatArrayOf(0.0f, 0.6f, 1.0f),  // Mostly transparent, then solid in the last 25%
      Shader.TileMode.CLAMP)
    gradientPaint!!.setShader(gradient)
  }

  override fun onDraw(canvas: Canvas) {
    super.onDraw(canvas)

    // Draw the gradient with the spotlight effect
    canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), gradientPaint!!)
  }
}

