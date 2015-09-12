package com.example.burcakdemircioglu.sunshine.app;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;

/**
 * Created by burcakdemircioglu on 31/07/15.
 */
public class CompassView extends View {
    private String direction;
    private String wind;

    public CompassView(Context context) {
        super(context);
    }

    public CompassView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CompassView(Context context, AttributeSet attrs, int defaultStyle) {
        super(context, attrs, defaultStyle);
    }

    public void setDirection(String newDirection) {
        direction = newDirection;
        invalidate();
        requestLayout();
    }

    public void setWind(String newWind) {
        wind = newWind;
        invalidate();
        requestLayout();
    }



    @Override
    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent ev){
        ev.getText().add(wind+direction);
        return true;
    }

    @Override
    protected void onMeasure(int wMeasureSpec, int hMeasureSpec) {
        int hSpecMode = MeasureSpec.getMode(hMeasureSpec);
        int hSpecSize = MeasureSpec.getSize(hMeasureSpec);
        int myHeight = hSpecSize;

        int wSpecMode = MeasureSpec.getMode(wMeasureSpec);
        int wSpecSize = MeasureSpec.getSize(wMeasureSpec);
        int myWidth = wSpecSize;

        if (hSpecMode == MeasureSpec.EXACTLY) {
            myHeight = hSpecSize;
        } else if (hSpecMode == MeasureSpec.AT_MOST) {

        }

        if (wSpecMode == MeasureSpec.EXACTLY) {
            myWidth = wSpecSize;
        } else if (wSpecMode == MeasureSpec.AT_MOST) {

        }
        setMeasuredDimension(myWidth, myHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (wind != null) {
            Paint paintOuterCircle = new Paint();
            paintOuterCircle.setStyle(Paint.Style.STROKE);
            paintOuterCircle.setColor(getResources().getColor(R.color.sunshine_dark_blue));
            paintOuterCircle.setStyle(Paint.Style.FILL);
            paintOuterCircle.setStrokeWidth(4);

            Paint paintInnerCircle = new Paint();
            paintInnerCircle.setStyle(Paint.Style.STROKE);
            paintInnerCircle.setColor(getResources().getColor(R.color.sunshine_light_blue));
            paintInnerCircle.setStyle(Paint.Style.FILL);
            paintInnerCircle.setStrokeWidth(4);

            Paint paintText = new Paint();
            paintText.setStyle(Paint.Style.STROKE);
            paintText.setColor(getResources().getColor(R.color.white));
            paintText.setStyle(Paint.Style.FILL);
            paintText.setStrokeWidth(4);
            paintText.setTextSize(getHeight() / 3);
            paintText.setTextAlign(Paint.Align.CENTER);
            paintText.setTypeface(Typeface.SERIF);

            canvas.drawCircle(getWidth() / 2, getHeight() / 2, (getHeight() - 5) / 2, paintOuterCircle);
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, (getHeight() - 5) / 3, paintInnerCircle);
            RectF rectF = new RectF(getWidth() / 2 - (getHeight() - 5) / 2, 2, getWidth() / 2 + (getHeight() - 5) / 2, getHeight() - 3);


//        /**padding control**/
//        int left = getPaddingLeft();
//        int top = getPaddingTop();
//        int right = getWidth() - getPaddingRight();
//        int bottom = getHeight() - getPaddingBottom();
//        canvas.drawLine(left, top, right, bottom, paint);

            float startDegree = 0;
            float swapDegree = 45;
            float rightAngle = 90;
            float fullAngle = 360;
            float StraightAngle = 180;
            if (direction != null) {
                if (direction.equals("N"))
                    startDegree = rightAngle + (swapDegree / 2);
                else if (direction.equals("NE"))
                    startDegree = rightAngle - (swapDegree / 2);
                else if (direction.equals("E"))
                    startDegree = swapDegree / 2;
                else if (direction.equals("SE"))
                    startDegree = fullAngle - (swapDegree / 2);
                else if (direction.equals("S"))
                    startDegree = fullAngle - (swapDegree / 2) - swapDegree;
                else if (direction.equals("SW"))
                    startDegree = StraightAngle + (swapDegree / 2) + swapDegree;
                else if (direction.equals("W"))
                    startDegree = StraightAngle + (swapDegree / 2);
                else if (direction.equals("NW"))
                    startDegree = StraightAngle - (swapDegree / 2);
                else {
                    startDegree = 0;
                    swapDegree = 0;
                }
            }
            canvas.drawArc(rectF, fullAngle - startDegree, swapDegree, true, paintInnerCircle);


            canvas.drawText(wind, 0, wind.length(), getWidth() / 2, (float) (getHeight() * 1.25) / 2, paintText);


            sendAccessibilityEvent(AccessibilityEvent.TYPE_VIEW_TEXT_CHANGED);
        }
    }
}
