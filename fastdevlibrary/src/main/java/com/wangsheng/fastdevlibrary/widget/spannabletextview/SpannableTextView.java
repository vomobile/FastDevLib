package com.wangsheng.fastdevlibrary.widget.spannabletextview;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Color;
import android.graphics.EmbossMaskFilter;
import android.graphics.Typeface;
import android.support.annotation.ColorInt;
import android.support.annotation.ColorRes;
import android.support.annotation.DimenRes;
import android.support.annotation.FloatRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.AbsoluteSizeSpan;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.MaskFilterSpan;
import android.text.style.ScaleXSpan;
import android.text.style.StrikethroughSpan;
import android.text.style.StyleSpan;
import android.text.style.SubscriptSpan;
import android.text.style.SuperscriptSpan;
import android.text.style.TypefaceSpan;
import android.text.style.UnderlineSpan;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.widget.TextView;

import com.wangsheng.fastdevlibrary.widget.spannabletextview.utils.RegEx;
import com.wangsheng.fastdevlibrary.widget.spannabletextview.utils.SpanLinkMovementMethod;
import com.wangsheng.fastdevlibrary.widget.spannabletextview.utils.SwClickableSpan;
import com.wangsheng.fastdevlibrary.widget.spannabletextview.utils.TextDimenTyped;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Spannable TextView
 */
public class SpannableTextView
    extends TextView {
  public static       int DEFAULT_TEXT_SIZE       = 0;
  public static final int DEFAULT_TEXT_COLOR      = Color.BLACK;
  public static final int DEFAULT_LINK_TEXT_COLOR = -1;
  public static final int DEFAULT_TEXT_BG_COLOR   = -1;

  private static Context         context;
  private        DisplayMetrics  displayMetrics;
  private        ArrayList<Span> spans;

  public SpannableTextView(Context context) {
    super(context);
    initializeView();
  }

  public SpannableTextView(Context context, AttributeSet attrs) {
    super(context, attrs);
    initializeView();
  }

  public SpannableTextView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    initializeView();
  }

  private void initializeView() {
    context = getContext();
    spans = new ArrayList<>();
    displayMetrics = getContext().getResources().getDisplayMetrics();
    SpannableTextView.DEFAULT_TEXT_SIZE = (int) getTextSize();
  }

  public void clearSpans() {
    this.spans.clear();
    setText("");
  }

  public int addSpan(@NonNull Span span) {
    this.spans.add(span);
    updateTextView();
    return spans.size();
  }

  public int addSpan(@NonNull Span span, int insertPosition) {
    if (checkIndex(insertPosition)) {
      this.spans.add(insertPosition, span);
      updateTextView();
      return insertPosition;
    }
    return -1;
  }

  public Span removeSpan(int removeSpanPosition) {
    if (checkIndex(removeSpanPosition)) {
      return this.spans.remove(removeSpanPosition);
    }
    return null;
  }

  public void replaceSpanAt(int replacePosition, @NonNull Span newSpan) {
    if (checkIndex(replacePosition)) {
      this.spans.set(replacePosition, newSpan);
    }
  }

  public Span getSpan(int position) {
    if (checkIndex(position)) {
      return this.spans.get(position);
    }
    return null;
  }

  public int getSpansCount() {
    if (spans == null) return -1;
    return spans.size();
  }

  public void updateTextView() {
    StringBuilder builder = new StringBuilder();
    for (int i = 0; i < spans.size(); i++) {
      Span span = spans.get(i);
      builder.append(span.text);
    }

    int cursor = 0;
    SpannableString spannableString = new SpannableString(builder.toString());
    for (int i = 0; i < spans.size(); i++) {
      Span span = spans.get(i);

      // text size
      int textSize = (int) TypedValue.applyDimension(span.typedValue.getValue(), span.textSize, displayMetrics);
      spannableString.setSpan(
          new AbsoluteSizeSpan(textSize),
          cursor,
          cursor + span.text.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
      );

      // type face (bold, italic)
      spannableString.setSpan(
          new StyleSpan(span.bold && span.italic ? Typeface.BOLD_ITALIC :
                            (span.bold ? Typeface.BOLD :
                                (span.italic ? Typeface.ITALIC : Typeface.NORMAL))),
          cursor,
          cursor + span.text.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
      );

      // text color
      spannableString.setSpan(
          new ForegroundColorSpan(span.textColor),
          cursor,
          cursor + span.text.length(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
      );

      // background color
      if (span.textBackgroundColor != -1) {
        spannableString.setSpan(
            new BackgroundColorSpan(span.textBackgroundColor),
            cursor,
            cursor + span.text.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
      }

      if (span.superScript) {
        spannableString.setSpan(
            new SuperscriptSpan(),
            cursor,
            cursor + span.text.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
      }

      if (span.subScript) {
        spannableString.setSpan(
            new SubscriptSpan(),
            cursor,
            cursor + span.text.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
      }

      if (span.strike) {
        spannableString.setSpan(
            new StrikethroughSpan(),
            cursor,
            cursor + span.text.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
      }

      if (span.underline) {
        spannableString.setSpan(
            new UnderlineSpan(),
            cursor,
            cursor + span.text.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
      }

      setLinkTextColor(Color.BLUE);
      setHighlightColor(span.linkHighlightColor);
      //setMovementMethod(LinkMovementMethod.getInstance());
      setMovementMethod(new SpanLinkMovementMethod());

      // click event
      if (span.clickableSpan != null) {
        spannableString.setSpan(
            span.clickableSpan,
            cursor,
            cursor + span.text.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
      }
      setLinksClickable(span.linkClickEnable);

      if (span.linkTextColor != -1) {
        setLinkTextColor(span.linkTextColor);
      }

      // scale x
      if (span.scaleX > 0) {
        spannableString.setSpan(
            new ScaleXSpan(span.scaleX),
            cursor,
            cursor + span.text.length(),
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
      }

      // blur mask filter
      if (span.blurMaskFilter != null) {
        spannableString.setSpan(
            new MaskFilterSpan(span.blurMaskFilter),
            cursor,
            cursor + span.text.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
      }

      // emboss mask filter
      if (span.embossMaskFilter != null) {
        spannableString.setSpan(
            new MaskFilterSpan(span.embossMaskFilter),
            cursor,
            cursor + span.text.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
      }

      // type face
      if (span.typeFace != null && !span.typeFace.isEmpty()) {
        spannableString.setSpan(
            new TypefaceSpan(span.typeFace),
            cursor,
            cursor + span.text.length(),
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );
      }

      // '#' tags
      if (span.enableSharpTagMatcher) {
        Matcher matcher = findPatternMatcher(RegEx.REGEX_FIND_HASHTAG, span.text);
        if (span.sharpTagClickSpan != null) {
          applyClickSpan(spannableString, matcher, span.sharpTagClickSpan);
        }
        else if (span.sharpTagTextColor != -1) {
          applyForegroundTextColorSpan(spannableString, matcher, span.sharpTagTextColor);
        }
        else {
          // to Default
          applyForegroundTextColorSpan(spannableString, matcher, span.linkTextColor);
        }
      }

      // '@' tags
      if (span.enableAtTagMatcher && span.atTagClickSpan != null) {
        Matcher matcher = findPatternMatcher(RegEx.REGEX_FIND_ATTAG_TEXT, span.text);
        if (span.atTagClickSpan != null) {
          applyClickSpan(spannableString, matcher, span.atTagClickSpan);
        }
        else if (span.atTagTextColor != -1) {
          applyForegroundTextColorSpan(spannableString, matcher, span.atTagTextColor);
        }
        else {
          // to Default
          applyForegroundTextColorSpan(spannableString, matcher, span.linkTextColor);
        }
      }

      // url string
      if (span.enableURLMatcher && span.urlClickSpan != null) {
        Matcher matcher = findPatternMatcher(RegEx.REGEX_VALIDATE_URL, span.text);
        if (span.urlClickSpan != null) {
          applyClickSpan(spannableString, matcher, span.urlClickSpan);
        }
        else if (span.urlStringColor != -1) {
          applyForegroundTextColorSpan(spannableString, matcher, span.urlStringColor);
        }
        else {
          // to Default
          applyForegroundTextColorSpan(spannableString, matcher, span.linkTextColor);
        }
      }

      // custom string regex
      if (!TextUtils.isEmpty(span.customRegExString) && span.customClickSpan != null) {
        Matcher matcher = findPatternMatcher(span.customRegExString, span.text);
        if (span.customClickSpan != null) {
          applyClickSpan(spannableString, matcher, span.customClickSpan);
        }
        else if (span.regExTextColor != -1) {
          applyForegroundTextColorSpan(spannableString, matcher, span.regExTextColor);
        }
        else {
          // to Default
          applyForegroundTextColorSpan(spannableString, matcher, span.linkTextColor);
        }
      }

      cursor = cursor + span.text.length();
    }

    setText(spannableString);
  }

  public boolean checkIndex(int index) {
    if (index < 0 && index > spans.size()) {
      throw new IndexOutOfBoundsException("Invalid index " + index + ", size is " + spans.size());
    }
    return true;
  }

  private void applyForegroundTextColorSpan(@NonNull SpannableString spannableString, @NonNull Matcher m, @ColorInt int textColor) {
    if (textColor == -1) {
      // defence code
      textColor = Color.BLUE;
    }
    while (m.find()) {
      spannableString.setSpan(
          new ForegroundColorSpan(textColor),
          m.start(),
          m.end(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
      );
    }
  }

  private void applyClickSpan(@NonNull SpannableString spannableString, @NonNull Matcher m, @NonNull final SwClickableSpan clickableSpan) {
    while (m.find()) {
      spannableString.setSpan(
          //CharacterStyle.wrap(clickableSpan),
          new SwClickableSpan(clickableSpan) {
            @Override
            public void onClick(View widget) {
              clickableSpan.onClick(widget);
            }
          },
          m.start(),
          m.end(),
          Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
      );
    }
  }

  private Matcher findPatternMatcher(@NonNull String regEx, @NonNull String target) {
    return Pattern.compile(regEx, Pattern.UNICODE_CASE).matcher(target);
  }


  /**
   * Builder `Span` Class
   */
  public static class Span {
    private String           text;
    private int              textSize;
    private TextDimenTyped typedValue;
    private int              textColor;
    private int              linkTextColor;
    private int              textBackgroundColor;
    private boolean          bold;
    private boolean          italic;
    private boolean          underline;
    private boolean          strike;
    private boolean          superScript;
    private boolean          subScript;
    private SwClickableSpan  clickableSpan;
    private int              linkHighlightColor;
    private boolean          linkClickEnable;
    private float            scaleX;
    private BlurMaskFilter   blurMaskFilter;
    private EmbossMaskFilter embossMaskFilter;
    private String           typeFace;
    private int              sharpTagTextColor;
    private int              atTagTextColor;
    private int              urlStringColor;
    private int              regExTextColor;

    // '#' tag
    private boolean         enableSharpTagMatcher;
    private SwClickableSpan sharpTagClickSpan;

    // '@' tag
    private boolean         enableAtTagMatcher;
    private SwClickableSpan atTagClickSpan;

    // url string link
    private boolean         enableURLMatcher;
    private SwClickableSpan urlClickSpan;

    // custom regular expression link
    private String          customRegExString;
    private SwClickableSpan customClickSpan;

    public Span(@NonNull String text) {
      this.text = text;
      init();
    }

    public Span(@StringRes int textResId) {
      if (checkContextInstance()) {
        this.text = context.getString(textResId);
      }
      init();
    }

    private void init() {
      this.textSize = SpannableTextView.DEFAULT_TEXT_SIZE;
      this.typedValue = TextDimenTyped.PX;
      this.textColor = DEFAULT_TEXT_COLOR;
      this.linkTextColor = DEFAULT_LINK_TEXT_COLOR;
      this.textBackgroundColor = DEFAULT_TEXT_BG_COLOR;
      this.bold = false;
      this.italic = false;
      this.underline = false;
      this.strike = false;
      this.superScript = false;
      this.subScript = false;
      this.clickableSpan = null;
      this.linkHighlightColor = DEFAULT_LINK_TEXT_COLOR;
      this.linkClickEnable = true;
      this.scaleX = 0.0f;
      this.blurMaskFilter = null;
      this.embossMaskFilter = null;
      this.typeFace = null;
      this.enableSharpTagMatcher = false;
      this.sharpTagClickSpan = null;
      this.enableAtTagMatcher = false;
      this.atTagClickSpan = null;
      this.enableURLMatcher = false;
      this.urlClickSpan = null;
      this.customRegExString = null;
      this.customClickSpan = null;
      this.sharpTagTextColor = -1;
      this.atTagTextColor = -1;
      this.urlStringColor = -1;
      this.regExTextColor = -1;
    }

    /**
     * Sets the spannable string parts of the SpannableTextView.
     */
    public Span text(@NonNull String text) {
      this.text = text;
      return this;
    }

    /**
     * Set the text size to a SP(Scaled Pixel) value.
     */
    public Span textSizeSP(int textSizeSp) {
      return textSize(TextDimenTyped.SP, textSizeSp);
    }

    /**
     * Set the text size to a DIP value.
     */
    public Span textSizeDIP(int textSizeDIP) {
      return textSize(TextDimenTyped.DIP, textSizeDIP);
    }

    public Span textSizeDIPRes(@DimenRes int textSizeResId) {
      if (checkContextInstance()) {
        return textSize(TextDimenTyped.DIP, context.getResources().getDimensionPixelSize(textSizeResId));
      }
      return this;
    }

    /**
     * Set the text size to a Pixel value.
     */
    public Span textSizePX(int textSizePx) {
      return textSize(TextDimenTyped.PX, textSizePx);
    }

    /**
     * Set the text size to a Point value.
     */
    public Span textSizePT(int textSizePT) {
      return textSize(TextDimenTyped.PT, textSizePT);
    }

    /**
     * Set the text size to a given unit and value.
     * TextDimenTyped is SP, DIP, PX, PT
     */
    public Span textSize(TextDimenTyped typedValue, int textSize) {
      this.textSize = textSize;
      this.typedValue = typedValue;
      return this;
    }

    /**
     * Sets the Text color.
     */
    public Span textColor(@ColorInt int textColor) {
      this.textColor = textColor;
      return this;
    }

    /**
     * Sets the Text color from Color Resource.
     */
    public Span textColorRes(@ColorRes int textColorResId) {
      if (checkContextInstance()) {
        this.textColor = ContextCompat.getColor(context, textColorResId);
      }
      return this;
    }

    /**
     * Sets the Hyper link text color.
     */
    public Span linkTextColor(@ColorInt int linkTextColor) {
      this.linkTextColor = linkTextColor;
      return this;
    }

    /**
     * Sets the Hyper link text color from Color Resource.
     */
    public Span linkTextColorRes(@ColorRes int linkTextColorResId) {
      if (checkContextInstance()) {
        this.linkTextColor = ContextCompat.getColor(context, linkTextColorResId);
      }
      return this;
    }

    /**
     * Sets the text background color.
     */
    public Span textBackgroundColor(@ColorInt int bgColor) {
      this.textBackgroundColor = bgColor;
      return this;
    }

    /**
     * Sets the text background color from Color Resource.
     */
    public Span textBackgroundColorRes(@ColorRes int bgColorResId) {
      if (checkContextInstance()) {
        this.textBackgroundColor = ContextCompat.getColor(context, bgColorResId);
      }
      return this;
    }

    public Span bold() {
      this.bold = true;
      return this;
    }

    public Span unBold() {
      this.bold = false;
      return this;
    }

    public Span italic() {
      this.italic = true;
      return this;
    }

    public Span unItalic() {
      this.italic = false;
      return this;
    }

    public Span underline() {
      this.underline = true;
      return this;
    }

    public Span unUnderline() {
      this.underline = false;
      return this;
    }

    public Span strike() {
      this.strike = true;
      return this;
    }

    public Span unStrike() {
      this.strike = false;
      return this;
    }

    public Span superScript() {
      this.superScript = true;
      return this;
    }

    public Span unSuperScript() {
      this.subScript = false;
      return this;
    }

    public Span subScript() {
      this.subScript = true;
      return this;
    }

    public Span unSubScript() {
      this.subScript = false;
      return this;
    }

    public Span click(@NonNull SwClickableSpan clickableSpan) {
      this.clickableSpan = clickableSpan;
      return this;
    }

    public Span clickHighlightColor(@ColorInt int linkHighlightColor) {
      this.linkHighlightColor = linkHighlightColor;
      return this;
    }

    public Span clickHighlightColorRes(@ColorRes int linkHighlightColorResId) {
      if (checkContextInstance()) {
        this.textBackgroundColor = ContextCompat.getColor(context, linkHighlightColorResId);
      }
      return this;
    }

    public Span linkClickEnable() {
      this.linkClickEnable = true;
      return this;
    }

    public Span linkClickDisable() {
      this.linkClickEnable = false;
      return this;
    }

    public Span scaleX(float scaleX) {
      this.scaleX = scaleX;
      return this;
    }

    public Span blurMaskFilter(@FloatRange(from = 0) float radius) {
      this.blurMaskFilter = new BlurMaskFilter(radius, BlurMaskFilter.Blur.NORMAL);
      return this;
    }

    public Span blurMaskFilter(@FloatRange(from = 0) float radius, BlurMaskFilter.Blur blurStyle) {
      this.blurMaskFilter = new BlurMaskFilter(radius, blurStyle);
      return this;
    }

    public Span embossMaskFilter(@NonNull float[] direction, float ambient, float specular, float blurRadius) {
      this.embossMaskFilter = new EmbossMaskFilter(direction, ambient, specular, blurRadius);
      return this;
    }

    public Span typeFaceFamily(@NonNull String fontFamily) {
      this.typeFace = fontFamily;
      return this;
    }

    public Span findSharpTags(@Nullable SwClickableSpan clickableSpan) {
      this.enableSharpTagMatcher = true;
      this.sharpTagClickSpan = clickableSpan;
      this.sharpTagTextColor = -1;
      return this;
    }

    public Span findSharpTags(@ColorInt int sharpTagTextColor) {
      this.enableSharpTagMatcher = true;
      this.sharpTagTextColor = sharpTagTextColor;
      this.sharpTagClickSpan = null;
      return this;
    }

    public Span findAtTags(@Nullable SwClickableSpan clickableSpan) {
      this.enableAtTagMatcher = true;
      this.atTagClickSpan = clickableSpan;
      this.atTagTextColor = -1;
      return this;
    }

    public Span findAtTags(@ColorInt int atTagTextColor) {
      this.enableAtTagMatcher = true;
      this.atTagTextColor = atTagTextColor;
      this.atTagClickSpan = null;
      return this;
    }

    public Span findURLstrings(@Nullable SwClickableSpan clickableSpan) {
      this.enableURLMatcher = true;
      this.urlClickSpan = clickableSpan;
      return this;
    }

    public Span findURLstrings(@ColorInt int urlStringColor) {
      this.enableURLMatcher = true;
      this.urlStringColor = urlStringColor;
      this.urlClickSpan = null;
      return this;
    }

    public Span findRegExStrings(@NonNull String regEx, @Nullable SwClickableSpan clickableSpan) {
      this.customRegExString = regEx;
      this.customClickSpan = clickableSpan;
      this.regExTextColor = -1;
      return this;
    }

    public Span findRegExStrings(@NonNull String regEx, @ColorInt int regExTextColor) {
      this.customRegExString = regEx;
      this.regExTextColor = regExTextColor;
      this.customClickSpan = null;
      return this;
    }

    public Span build() {
      return this;
    }

    private boolean checkContextInstance() {
      if (context != null) {
        return true;
      }
      else {
        throw new NullPointerException("Context instace is null..");
      }
    }

  } // end of Builder class `Span`

}