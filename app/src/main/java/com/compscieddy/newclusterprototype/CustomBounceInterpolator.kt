package com.compscieddy.newclusterprototype

import android.view.animation.Interpolator

class CustomBounceInterpolator(private val amplitude: Double, private val frequency: Double) :
  Interpolator {

  override fun getInterpolation(input: Float): Float {
    return (-1 * Math.pow(Math.E, -input / amplitude) * Math.cos(
      frequency * input) + 1).toFloat()
  }
}