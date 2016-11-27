package com.wangsheng.fastdevlibrary.widget.spannabletextview.utils;

import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.style.CharacterStyle;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.widget.TextView;

/**
 * @author KangSung-Woo
 * @since 2016/07/18
 */
public class SpanLinkMovementMethod
    extends LinkMovementMethod {
  private SwClickableSpan pressedSpan;

  private SwClickableSpan getPressedSpan(TextView textView, Spannable spannable, MotionEvent event) {
    int x = (int) event.getX();
    int y = (int) event.getY();

    x -= textView.getTotalPaddingLeft();
    y -= textView.getTotalPaddingTop();

    x += textView.getScrollX();
    y += textView.getScrollY();

    Layout layout = textView.getLayout();
    int line = layout.getLineForVertical(y);
    int off = layout.getOffsetForHorizontal(line, x);

    SwClickableSpan[] link = spannable.getSpans(off, off, SwClickableSpan.class);
    SwClickableSpan touchedSpan = null;
    if (link.length > 0) {
      touchedSpan = link[0];
    }
    return touchedSpan;
  }

  @Override
  public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
    int action = event.getAction();

    if (action == MotionEvent.ACTION_UP ||
        action == MotionEvent.ACTION_DOWN ||
        action == MotionEvent.ACTION_MOVE) {
      int x = (int) event.getX();
      int y = (int) event.getY();

      x -= widget.getTotalPaddingLeft();
      y -= widget.getTotalPaddingTop();

      x += widget.getScrollX();
      y += widget.getScrollY();

      Layout layout = widget.getLayout();
      int line = layout.getLineForVertical(y);
      int off = layout.getOffsetForHorizontal(line, x);

      CharacterStyle[] candidates = buffer.getSpans(off, off, CharacterStyle.class);
      ClickableSpan clickableSpan = null;
      for (CharacterStyle characterStyle : candidates) {
        if (characterStyle.getUnderlying() instanceof ClickableSpan) {
          clickableSpan = (ClickableSpan) characterStyle.getUnderlying();
          break;
        }
      }

      if (clickableSpan != null) {
        if (action == MotionEvent.ACTION_UP) {
          clickableSpan.onClick(widget);
          if (pressedSpan != null) {
            pressedSpan.setPressed(false);
            super.onTouchEvent(widget, buffer, event);
          }
          pressedSpan = null;
          Selection.removeSelection(buffer);
        }

        else if (action == MotionEvent.ACTION_DOWN) {
          pressedSpan = getPressedSpan(widget, buffer, event);
          if (pressedSpan != null) {
            pressedSpan.setPressed(true);
          }
          Selection.setSelection(buffer,
                                 buffer.getSpanStart(clickableSpan),
                                 buffer.getSpanEnd(clickableSpan));
        }

        else {
          // action == MotionEvent.ACTION_MOVE)
          SwClickableSpan swClickableSpan = getPressedSpan(widget, buffer, event);
          if (swClickableSpan != null && pressedSpan != null) {
            if (swClickableSpan != pressedSpan) {
              pressedSpan.setPressed(false);
              pressedSpan = null;
              Selection.removeSelection(buffer);
            }
          }
        }
        return true;
      }
      else {
        if (pressedSpan != null) {
          pressedSpan.setPressed(false);
          super.onTouchEvent(widget, buffer, event);
        }
        pressedSpan = null;
        Selection.removeSelection(buffer);
      }
    }
    return super.onTouchEvent(widget, buffer, event);
  }
}
