package com.sky.alphabemodule;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Checkable;

import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.core.content.res.ResourcesCompat;

public class AlphabetView extends View implements Checkable {
    private String sourceText;
    private int length;
    private boolean isRandomBackColor;
    private boolean isChecked;
    private boolean checkableByClick;
    private Drawable checkedDrawable;
    private int checkedBackColor;
    private int backgroundColor;
    public static final int SCALE_TYPE_CENTER = 0;
    public static final int SCALE_TYPE_FIT_CENTER = 1;
    private int scaleType = SCALE_TYPE_CENTER;

    private final Paint textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private final Paint shapePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public AlphabetView(Context context) {
        super(context);
        init();

        length = 1;
        isRandomBackColor = true;
        checkedDrawable = ResourcesCompat.getDrawable(getResources(), R.drawable.ic_baseline_check_24, null);
        checkedBackColor = Color.BLUE;
        textPaint.setTextSize(TypedValue.complexToDimensionPixelSize(20, getResources().getDisplayMetrics()));
        textPaint.setColor(Color.BLACK);
    }

    public AlphabetView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initialization(context, attrs);
    }

    public AlphabetView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialization(context, attrs);
    }

    private void init() {
        setClickable(true);
        setFocusable(true);

        setSelectableBackground();

        textPaint.setStyle(Paint.Style.FILL_AND_STROKE);
    }

    private void setSelectableBackground() {
        TypedValue output = new TypedValue();
        getContext().getTheme().resolveAttribute(R.attr.selectableItemBackgroundBorderless,
                output, true);
        setBackgroundResource(output.resourceId);
    }

    private void initialization(Context context, AttributeSet attrs) {
        init();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.AlphabetView, 0,R.style.AlphabetView);

        isRandomBackColor = a.getBoolean(R.styleable.AlphabetView_av_randomBackgroundColor, true);
        isChecked = a.getBoolean(R.styleable.AlphabetView_av_checked, false);
        checkableByClick = a.getBoolean(R.styleable.AlphabetView_av_checkableByClick, true);

        checkedDrawable = a.getDrawable(R.styleable.AlphabetView_av_checkedDrawable);

        checkedBackColor = a.getInt(R.styleable.AlphabetView_av_checkedBackgroundColor, Color.BLACK);

        if (isRandomBackColor) {
            backgroundColor = getRandomColor(10, 190);
        } else {
            Drawable background = a.getDrawable(R.styleable.AlphabetView_android_background);
            if (background instanceof ColorDrawable) {
                backgroundColor = ((ColorDrawable) background).getColor();
            }
        }
        shapePaint.setColor(backgroundColor);

        scaleType = a.getInt(R.styleable.AlphabetView_av_scaleType, SCALE_TYPE_CENTER);

        length = a.getInt(R.styleable.AlphabetView_av_length, 1);
        sourceText = a.getString(R.styleable.AlphabetView_av_sourceText);

        checkLengths();

        float strokeWidth = a.getFloat(R.styleable.AlphabetView_av_textStrokeWidth, 5);
        int textSize = a.getDimensionPixelSize(R.styleable.AlphabetView_av_textSize, 20);
        int textColor = a.getColor(R.styleable.AlphabetView_av_textColor, Color.BLACK);

        textPaint.setStrokeWidth(strokeWidth);
        textPaint.setTextSize(textSize);
        textPaint.setColor(textColor);

        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != oldw && h != oldh) {
            if (checkedDrawable != null) {
                setDrawableBounds();
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // setBackgroundColor(Color.TRANSPARENT);

        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(250, 250);

        width = height = Math.min(width, height) + MeasureSpec.EXACTLY;

        setMeasuredDimension(width, height);
    }

    @Override
    public void toggle() {
        isChecked = !isChecked;
        invalidate();
        scaleAnimate();
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean performClick() {
        if (checkableByClick) {
            toggle();
        }
        return super.performClick();
    }

    private static int getRandomColor(int min, int max) {

        if (max > 255)
            max = 255;

        if (min < -255)
            min = 0;

        int diff = max - min;

        int r = (int) (Math.random() * diff) + min;
        int g = (int) (Math.random() * diff) + min;
        int b = (int) (Math.random() * diff) + min;

        return Color.rgb(r, g, b);
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public Drawable getCheckedDrawable() {
        return checkedDrawable;
    }

    public void setCheckedDrawable(Drawable checkedDrawable) {
        this.checkedDrawable = checkedDrawable;
    }

    public int getLength() {
        return length;
    }

    public void setLength(int length) {
        this.length = length;
        checkLengths();
    }

    private void checkLengths() {
        if (sourceText != null && !TextUtils.isEmpty(sourceText)) {
            int l = sourceText.length();
            if (length > l) {
                length = l;
            }
        }
    }

    public int getCheckedBackColor() {
        return checkedBackColor;
    }

    public void setTextColor(int color) {
        textPaint.setColor(color);
        invalidate();
    }

    public void setTextSize(@Px int textSize) {
        textPaint.setTextSize(textSize);
        invalidate();
    }

    public void setCheckedBackColor(int checkedBackColor) {
        this.checkedBackColor = checkedBackColor;
    }

    public boolean isRandomBackColor() {
        return isRandomBackColor;
    }

    public void setRandomBackColor(boolean randomBackColor) {
        isRandomBackColor = randomBackColor;
    }

    public void setTextStrokeWidth(float strokeWidth) {
        textPaint.setStrokeWidth(strokeWidth);
        invalidate();
    }

    private void scaleAnimate() {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1, 1.1F, 1);
        valueAnimator.setDuration(250);
        valueAnimator.addUpdateListener(animation -> {
            final float scale = (float) animation.getAnimatedValue();
            setScaleX(scale);
            setScaleY(scale);
        });
        valueAnimator.setInterpolator(new OvershootInterpolator());
        valueAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        drawBackgroundCircle(canvas);
        drawText(canvas);

        if (isChecked) {
            drawCheckedCircle(canvas);
            drawCheckedDrawable(canvas);
        }
    }

    private void drawCheckedCircle(Canvas canvas) {
        shapePaint.setColor(checkedBackColor);
        drawCircle(canvas);
    }

    private void setDrawableBounds() {
        int left, top, right, bottom;

        if (scaleType == SCALE_TYPE_CENTER) {
            int w = checkedDrawable.getMinimumWidth();
            int h = checkedDrawable.getMinimumHeight();

            int viewWidth = getWidth();
            int viewHeight = getHeight();

            if (w > viewWidth) {
                left = -(w / 2 - viewWidth / 2);
            } else {
                left = (viewWidth - w) / 2;
            }
            if (h > viewHeight) {
                top = -(h / 2 - viewHeight / 2);
            } else {
                top = (viewHeight - h) / 2;
            }
            right = left + w;
            bottom = top + h;
        } else {
            left = getPaddingLeft();
            top = getPaddingTop();
            right = getWidth() - getPaddingRight();
            bottom = getHeight() - getPaddingBottom();
        }

        checkedDrawable.setBounds(left, top, right, bottom);
    }

    @Override
    public void setPadding(int left, int top, int right, int bottom) {
        super.setPadding(left, top, right, bottom);
        setDrawableBounds();
    }

    private void drawCheckedDrawable(Canvas canvas) {
        if (checkedDrawable != null) {
            checkedDrawable.draw(canvas);
        }
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
        checkLengths();
    }


    private void drawText(Canvas canvas) {
        final String letters = sourceText.substring(0, length);

        float textWidth = textPaint.measureText(letters);

        Paint.FontMetrics metrics = textPaint.getFontMetrics();

        float y = getHeight() / 2F + (Math.abs(metrics.top) - metrics.bottom - 2) / 2F;

        canvas.drawText(letters, getWidth() / 2F - textWidth / 2F, y, textPaint);
    }

    public int getScaleType() {
        return scaleType;
    }

    public void setScaleType(int scaleType) {
        if (this.scaleType != scaleType) {
            post(this::setDrawableBounds);
        }
        this.scaleType = scaleType;
    }

    @Override
    public void setBackgroundColor(int color) {
        this.backgroundColor = color;
        // shapePaint.setColor(color);
    }

    private void drawCircle(Canvas canvas) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            canvas.drawOval(0, 0, getWidth(), getHeight(), shapePaint);
        } else {
            canvas.drawCircle(getWidth() / 2F, getHeight() / 2F,
                    getWidth() / 2F, shapePaint);
        }
    }

    private void drawBackgroundCircle(Canvas canvas) {
        shapePaint.setColor(backgroundColor);
        drawCircle(canvas);
    }

    public interface OnCheckedChangeListener {
        void onCheckedChange(AlphabetView v, boolean isChecked);
    }

    private OnCheckedChangeListener onCheckedChangeListener;

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }
}