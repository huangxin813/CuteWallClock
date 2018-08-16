package huangxin.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import java.util.Calendar;

/**
 * @author huangxin
 */
public class CuteWallClockView extends View {

    private Paint mBgPaint;
    private Paint mPanelPaint;
    private Paint mPinPaint;
    private Paint mTextPaint;

    //// TODO: add custom attributes
    private int mPanelRadius;
    private int mHourPointerLen = 58;
    private int mHourTailLen = 5;
    private int mHourPointerWidth = 8;
    private int mMinPointerLen = 72;
    private int mMinTailLen = 5;
    private int mMinPointerWidth = 5;
    private int mSecPointerLen = 90;
    private int mSecTailLen = 10;
    private int mSecPointerWidth = 2;
    private int mPanelRingWidth = 10;

    private int mDayModeStart = 6;
    private int mDayModeEnd = 19;

    private static final int NIGHT_BG = Color.parseColor("#6a000000");
    private static final int DAY_BG = Color.WHITE;

    private int mCenterX, mCenterY;

    private ClockPointer mHourPointer, mMinPointer, mSecPointer;
    private int mSecPointerColor = Color.RED;

    public CuteWallClockView(Context context) {
        this(context, null);
    }

    public CuteWallClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CuteWallClockView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes // TODO: add custom attributes
//        final TypedArray a = getContext().obtainStyledAttributes(
//                attrs, R.styleable.CuteWallClockView, defStyle, 0);
//        a.recycle();

        mPanelPaint = new Paint();
        mPanelPaint.setStrokeWidth(mPanelRingWidth);
        mPanelPaint.setAntiAlias(true);
        mPanelPaint.setStyle(Paint.Style.STROKE);
        mPanelPaint.setStrokeCap(Paint.Cap.ROUND);

        mBgPaint = new Paint();
        mBgPaint.setAntiAlias(true);
        mBgPaint.setStyle(Paint.Style.FILL);

        mPinPaint = new Paint();
        mPinPaint.setAntiAlias(true);
        mPinPaint.setStyle(Paint.Style.FILL);
        mPinPaint.setStrokeWidth(6);
        mPinPaint.setStrokeCap(Paint.Cap.ROUND);
        mPinPaint.setColor(mSecPointerColor);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setFakeBoldText(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(20);
    }

    public void setPanelRadius(int radius) {
        mPanelRadius = radius;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);
        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);
        int width, height;
        if (modeW == MeasureSpec.EXACTLY) {
            width = w;
        } else {
            width = 300;
        }
        if (modeH == MeasureSpec.EXACTLY) {
            height = h;
        } else {
            height = 300;
        }
        int contentWidth = width - getPaddingLeft() - getPaddingRight();
        int contentHeight = height - getPaddingTop() - getPaddingBottom();
        mCenterX = contentWidth/2 + getPaddingLeft();
        mCenterY = contentHeight/2 + getPaddingTop();
        if (mPanelRadius == 0) {
            mPanelRadius = Math.min(contentWidth, contentHeight)/2 - mPanelRingWidth;
        }
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Calendar calendar = Calendar.getInstance();
        int h = calendar.get(Calendar.HOUR_OF_DAY);
        int m = calendar.get(Calendar.MINUTE);
        int s = calendar.get(Calendar.SECOND);

        if (mHourPointer == null) {
            mHourPointer = new ClockPointer(mCenterX, mCenterY, mHourPointerLen, mHourTailLen);
            mHourPointer.setPointerWidth(mHourPointerWidth);
        }
        mHourPointer.setAngle((h%12 + m/60f)/12f * 360);

        if (mMinPointer == null) {
            mMinPointer = new ClockPointer(mCenterX, mCenterY, mMinPointerLen, mMinTailLen);
            mMinPointer.setPointerWidth(mMinPointerWidth);
        }
        mMinPointer.setAngle((m + s/60f)/60f * 360);
        if (mSecPointer == null) {
            mSecPointer = new ClockPointer(mCenterX, mCenterY, mSecPointerLen, mSecTailLen);
            mSecPointer.setColor(mSecPointerColor);
            mSecPointer.setPointerWidth(mSecPointerWidth);
        }
        mSecPointer.setAngle(s/60f * 360);

        Paint.FontMetrics metrics = mTextPaint.getFontMetrics();
        int gap = 30;
        canvas.drawText("12", mCenterX, mCenterY - mPanelRadius - (metrics.bottom + metrics.top)/2 + gap, mTextPaint);
        canvas.drawText("3", mCenterX + mPanelRadius - gap, mCenterY - (metrics.bottom + metrics.top)/2, mTextPaint);

        mBgPaint.setColor(isInDay(h) ? DAY_BG : NIGHT_BG);
        //draw bg
        canvas.drawCircle(mCenterX, mCenterY, mPanelRadius, mBgPaint);
        //draw out ring
        mPanelPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(mCenterX, mCenterY, mPanelRadius, mPanelPaint);
        //draw pointers
        mHourPointer.drawPointer(canvas);
        mMinPointer.drawPointer(canvas);
        mSecPointer.drawPointer(canvas);
        //draw center pin
        canvas.drawPoint(mCenterX, mCenterY, mPinPaint);

        postInvalidateDelayed(1000);
    }

    private boolean isInDay(int hourOfDay) {
        return hourOfDay >= mDayModeStart && hourOfDay <= mDayModeEnd;
    }

    public void setDayModeScope(int start, int end) {
        mDayModeStart = start;
        mDayModeEnd = end;
    }

    private void playTickSound() {
        //// TODO
    }

}
