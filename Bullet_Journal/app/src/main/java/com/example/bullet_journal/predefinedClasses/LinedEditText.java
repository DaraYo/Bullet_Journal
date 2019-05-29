package com.example.bullet_journal.predefinedClasses;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.EditText;

public class LinedEditText extends android.support.v7.widget.AppCompatEditText {
    private Rect mRect;
    private Paint mPaint;

    public LinedEditText(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);

        mRect= new Rect();
        mPaint= new Paint();
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.parseColor("#C0C0C0"));
    }

    @Override
    protected void onDraw(Canvas canvas){
//        int count= getLineCount();
        int height = getHeight();
        int line_height= getLineHeight();
        int count= height/line_height;
        Rect r= mRect;
        Paint paint= mPaint;

        if (getLineCount() > count)
            count = getLineCount();//for long text with scrolling

        int baseline = getLineBounds(0, r);

        for (int i= 0; i< count; i++){
            canvas.drawLine(r.left, baseline+1, r.right, baseline+1, paint);
            baseline += getLineHeight();
        }
        super.onDraw(canvas);
    }
}
