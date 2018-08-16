package huangxin.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

/**
 * Created by huangxin
 */

public class ClockPointer {

    private float mPinnedX, mPinnedY;
    private int mHeaderLen;
    private int mTailLen;
    private float mAngle;
    private Paint mPaint;

    public ClockPointer(float pinnedX, float pinnedY, int headLen, int tailLen) {
        mPinnedX = pinnedX;
        mPinnedY = pinnedY;
        mHeaderLen = headLen;
        mTailLen = tailLen;
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setPointerWidth(float width) {
        mPaint.setStrokeWidth(width);
    }

    public void setColor(int color) {
        mPaint.setColor(color);
    }

    public void setAngle(float angle) {
        mAngle = angle;
    }

    public void drawPointer(Canvas canvas) {
        float endX = (float) (mPinnedX + mHeaderLen * Math.cos(Math.toRadians(mAngle - 90)));
        float endY = (float) (mPinnedY + mHeaderLen * Math.sin(Math.toRadians(mAngle - 90)));
        canvas.drawLine(mPinnedX, mPinnedY, endX, endY, mPaint);
        drawTail(canvas);
    }

    private void drawTail(Canvas canvas) {
        float endX = (float) (mPinnedX - mTailLen * Math.cos(Math.toRadians(mAngle - 90)));
        float endY = (float) (mPinnedY - mTailLen * Math.sin(Math.toRadians(mAngle - 90)));
        canvas.drawLine(mPinnedX, mPinnedY, endX, endY, mPaint);
    }
}
