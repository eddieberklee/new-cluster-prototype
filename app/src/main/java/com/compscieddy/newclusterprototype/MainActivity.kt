package com.compscieddy.newclusterprototype

import android.content.Context
import android.content.res.Resources
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.dynamicanimation.animation.DynamicAnimation
import androidx.dynamicanimation.animation.SpringAnimation
import androidx.dynamicanimation.animation.SpringForce


val Int.dpToPx: Int
  get() = (this * Resources.getSystem().displayMetrics.density).toInt()

// This one is when you need a float value instead.
val Float.dpToPx: Float
  get() = this * Resources.getSystem().displayMetrics.density

fun Int.withAlpha(alpha: Float): Int {
  require(alpha in 0.0f..1.0f) { "Alpha must be between 0.0 and 1.0" }
  val alphaInt = (alpha * 255).toInt() and 0xFF
  return (this and 0x00FFFFFF) or (alphaInt shl 24)
}

/**
 * Look at DynamicAnimation.* for property constants.
 */
fun runSpringAnimation(
  v: View,
  property: DynamicAnimation.ViewProperty?,
  finalPosition: Float? = null,
  springForce: (() -> SpringForce)? = null,
) {
  val springAnimation = if (finalPosition != null) {
    SpringAnimation(v, property, finalPosition)
  } else {
    SpringAnimation(v, property)
  }

  if (springForce != null) {
    springAnimation.spring = springForce.invoke()
  }
  springAnimation.start()
}

class MainActivity : AppCompatActivity() {

  private lateinit var vibrator: Vibrator

  private lateinit var pogContainers: List<View>
  private lateinit var pogs: List<View>
  private lateinit var names: List<View>

  private lateinit var mapBackground: View
  private lateinit var clusterContainer: View
  private lateinit var newClusterContainer: View
  private lateinit var gradientSpotlight: View
  private lateinit var neighborhoodText: View

  private val x = 0
  private val y = 1

  private var shouldAnimate = true

  private val endOffsets =
    listOf(
      listOf(-79f.dpToPx, 30f.dpToPx),
      listOf(0f.dpToPx, 61f.dpToPx),
      listOf(78f.dpToPx, 24f.dpToPx),
      listOf(54f.dpToPx, -81f.dpToPx),
      listOf(-56f.dpToPx, -70f.dpToPx),
    )

  private val indivAnimationDuration = 600L
  private val offsetScaleFactor = 1.6f

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN
    actionBar?.hide()

    vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    val rootView = findViewById<View>(android.R.id.content)
    setClipChildrenAndPadding(rootView)

    mapBackground = findViewById(R.id.map_background)
    clusterContainer = findViewById(R.id.cluster_container)
    newClusterContainer = findViewById(R.id.new_cluster_container)
    gradientSpotlight = findViewById(R.id.gradient_spotlight)
    neighborhoodText = findViewById<TextView>(R.id.neighborhood_text)

    neighborhoodText.scaleX = 0.1f
    neighborhoodText.scaleY = 0.1f

    pogContainers = listOf(
      findViewById(R.id.pog_container1),
      findViewById(R.id.pog_container2),
      findViewById(R.id.pog_container3),
      findViewById(R.id.pog_container4),
      findViewById(R.id.pog_container5),
    )
    pogs = listOf(
      findViewById(R.id.note1),
      findViewById(R.id.note2),
      findViewById(R.id.note3),
      findViewById(R.id.note4),
      findViewById(R.id.note5),
    )
    names = listOf(
      findViewById(R.id.name_label1),
      findViewById(R.id.name_label2),
      findViewById(R.id.name_label3),
      findViewById(R.id.name_label4),
      findViewById(R.id.name_label5),
    )

    newClusterReset()

    clusterContainer.setOnClickListener {
      triggerHapticFeedback()
      animateCluster(shouldAnimate)
      shouldAnimate = !shouldAnimate
    }
  }

  private fun newClusterReset() {
    pogContainers.forEach { pogContainer ->
      pogContainer.scaleX = 0.01f
      pogContainer.scaleY = 0.01f
    }
  }

  private fun setClipChildrenAndPadding(view: View) {
    if (view is ViewGroup) {
      val viewGroup = view
      viewGroup.clipChildren = false
      viewGroup.clipToPadding = false

      // Iterate through all children and apply the same settings
      for (i in 0 until viewGroup.childCount) {
        val child = viewGroup.getChildAt(i)
        setClipChildrenAndPadding(child)
      }
    }
  }

  private fun animateCluster(shouldAnimate: Boolean) {
    mapBackground.animate()
      .setDuration(800L)
      .scaleX(if (shouldAnimate) 1.3f else 1f)
      .scaleY(if (shouldAnimate) 1.3f else 1f)
      .setInterpolator(CustomBounceInterpolator(0.2, 10.0))
    gradientSpotlight.animate()
      .setDuration(800L)
      .alpha(if (shouldAnimate) 0.7f else 0f)
      .setInterpolator(CustomBounceInterpolator(0.2, 10.0))

    clusterContainer.animate()
      .alpha(if (shouldAnimate) 0f else 1f)
    newClusterContainer.animate()
      .setDuration(indivAnimationDuration)
      .alpha(if (shouldAnimate) 1f else 0f)

    pogContainers.forEachIndexed { index, pogContainer ->
      pogContainer.animate()
        .setDuration(indivAnimationDuration)
        .scaleX(if (shouldAnimate) 1f else 0f)
        .scaleY(if (shouldAnimate) 1f else 0f)
        .translationX(if (shouldAnimate) (endOffsets[index][x] * offsetScaleFactor) else 0f)
        .translationY(if (shouldAnimate) (endOffsets[index][y] * offsetScaleFactor) else 0f)
        .setInterpolator(CustomBounceInterpolator(0.2, 10.0))
    }
    names.forEach { name ->
      name.translationX = (Math.random() * 50f.dpToPx).toFloat()
      name.translationY = (Math.random() * 30f.dpToPx).toFloat()
      name.animate()
        .setDuration(indivAnimationDuration)
        .translationX(0f)
        .translationY(0f)
        .setInterpolator(CustomBounceInterpolator(0.2, 10.0))
    }
    neighborhoodText.animate()
      .scaleX(if (shouldAnimate) 1f else 0f)
      .scaleY(if (shouldAnimate) 1f else 0f)
      .setInterpolator(CustomBounceInterpolator(0.2, 10.0))
  }

  private fun triggerHapticFeedback() {
    if (vibrator != null && vibrator.hasVibrator()) {
      val effect = VibrationEffect.createOneShot(10, VibrationEffect.DEFAULT_AMPLITUDE)
      vibrator.vibrate(effect)
    }
  }

}