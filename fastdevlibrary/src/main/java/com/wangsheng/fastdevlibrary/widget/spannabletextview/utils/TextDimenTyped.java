package com.wangsheng.fastdevlibrary.widget.spannabletextview.utils;

import android.util.TypedValue;

/**
 * @author KangSung-Woo
 * @since 2016/07/15
 */
public enum TextDimenTyped {
  DIP(TypedValue.COMPLEX_UNIT_DIP),
  SP(TypedValue.COMPLEX_UNIT_SP),
  PT(TypedValue.COMPLEX_UNIT_PT),
  PX(TypedValue.COMPLEX_UNIT_PX);

  private int value;

  TextDimenTyped(int v) {
    this.value = v;
  }

  public int getValue() {
    return value;
  }

  public static TextDimenTyped parseFromValue(int v) {
    switch (v) {
      case TypedValue.COMPLEX_UNIT_DIP:
        return DIP;
      case TypedValue.COMPLEX_UNIT_SP:
        return SP;
      case TypedValue.COMPLEX_UNIT_PT:
        return PT;
      case TypedValue.COMPLEX_UNIT_PX:
        return PX;
    }
    return null;
  }

}
