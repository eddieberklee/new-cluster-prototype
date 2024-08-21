package com.compscieddy.newclusterprototype;

import android.graphics.Paint;
import android.text.TextPaint;
import android.text.style.CharacterStyle;
import android.text.style.UpdateAppearance;

public class StrokeSpan extends CharacterStyle implements UpdateAppearance {

  private final int strokeColor;
  private final float strokeWidth;

  public StrokeSpan(int strokeColor, float strokeWidth) {
    this.strokeColor = strokeColor;
    this.strokeWidth = strokeWidth;
  }

  @Override
  public void updateDrawState(TextPaint tp) {
    tp.setStyle(Paint.Style.STROKE);
    tp.setStrokeWidth(strokeWidth);
    tp.setColor(strokeColor);
  }
}
