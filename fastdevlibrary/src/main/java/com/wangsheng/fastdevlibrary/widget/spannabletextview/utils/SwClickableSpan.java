package com.wangsheng.fastdevlibrary.widget.spannabletextview.utils;

import android.graphics.Color;
import android.support.annotation.ColorInt;
import android.text.TextPaint;
import android.text.style.ClickableSpan;

/**
 * @author KangSung-Woo
 * @since 2016/07/18
 */
public abstract class SwClickableSpan
    extends ClickableSpan {
  private boolean isShowingUnderLine;
  private boolean isPressed;
  private int     normalTextColor;
  private int     pressedTextColor;
  private int     pressedBgColor;

  public SwClickableSpan() {
    defaultSettings();
  }

  public SwClickableSpan(SwClickableSpan c) {
    this.isShowingUnderLine = c.isShowingUnderLine();
    this.isPressed = false;
    this.normalTextColor = c.getNormalTextColor();
    this.pressedTextColor = c.getPressedTextColor();
    this.pressedBgColor = c.getPressedBgColor();
  }

  public SwClickableSpan(boolean isShowingUnderLine) {
    defaultSettings();
    this.isShowingUnderLine = isShowingUnderLine;
  }

  public SwClickableSpan(@ColorInt int normalTextColor, @ColorInt int pressedTextColor) {
    defaultSettings();
    this.normalTextColor = normalTextColor;
    this.pressedTextColor = pressedTextColor;
    this.isShowingUnderLine = true;
  }

  public SwClickableSpan(@ColorInt int normalTextColor, @ColorInt int pressedTextColor, int pressedBgColor) {
    defaultSettings();
    this.normalTextColor = normalTextColor;
    this.pressedTextColor = pressedTextColor;
    this.pressedBgColor = pressedBgColor;
    this.isShowingUnderLine = true;
  }

  public SwClickableSpan(@ColorInt int normalTextColor, @ColorInt int pressedTextColor, int pressedBgColor, boolean isShowingUnderLine) {
    defaultSettings();
    this.normalTextColor = normalTextColor;
    this.pressedTextColor = pressedTextColor;
    this.pressedBgColor = pressedBgColor;
    this.isShowingUnderLine = isShowingUnderLine;
  }

  private void defaultSettings() {
    this.normalTextColor = Color.BLUE;
    this.pressedTextColor = Color.RED;
    this.pressedBgColor = Color.TRANSPARENT;
    this.isShowingUnderLine = true;
  }

  public void setPressed(boolean isPressed) {
    this.isPressed = isPressed;
  }

  public void setShowingUnderLine(boolean showing) {
    this.isShowingUnderLine = showing;
  }

  public boolean isShowingUnderLine() {
    return isShowingUnderLine;
  }

  public int getNormalTextColor() {
    return normalTextColor;
  }

  public int getPressedTextColor() {
    return pressedTextColor;
  }

  public int getPressedBgColor() {
    return pressedBgColor;
  }

  @Override
  public void updateDrawState(TextPaint ds) {
    super.updateDrawState(ds);
    ds.setColor(isPressed ? pressedTextColor : normalTextColor);
    ds.bgColor = (isPressed ? pressedBgColor : Color.TRANSPARENT);
    ds.setUnderlineText(isShowingUnderLine);
  }
}
