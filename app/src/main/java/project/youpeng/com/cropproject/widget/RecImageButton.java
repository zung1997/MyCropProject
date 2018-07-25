package project.youpeng.com.cropproject.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;

import project.youpeng.com.cropproject.R;


public class RecImageButton extends android.support.v7.widget.AppCompatImageButton {

    private String text = "";
    private Paint paint;
    private int defineScale = 25;
    private int width = 0;
    private int height = 0;
    private RecImageButton lastClick = null;

    public RecImageButton(Context context) {
        super(context);
        init();
    }

    public RecImageButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RecImageButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint = new Paint(TEXT_ALIGNMENT_CENTER);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setColor(Color.BLACK);
        paint.setTextSize(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, getResources().getDisplayMetrics()));
        paint.setTypeface(Typeface.DEFAULT_BOLD);
    }

    public void setText(String s) {
        this.text = s;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (width == 0 && height == 0) {
            paint.setColor(getResources().getColor(R.color.colorAccent));
            text = "自由";
        } else {
            text = width + ":" + height;
        }
        canvas.drawText(text, getWidth() / 2, getHeight() / 2, paint);
    }

    public void resetType(int width, int height) {
        this.height = height;
        this.width = width;
        if (width == 0 && height == 0)
            setType(R.drawable.bt_free);
        if (width == 1 && height == 1)
            setType(R.drawable.bt_one_one);
        if (width == 2 && height == 3)
            setType(R.drawable.bt_two_three);
        if (width == 3 && height == 2)
            setType(R.drawable.bt_three_two);
        if (width == 3 && height == 4)
            setType(R.drawable.bt_three_four);
        if (width == 4 && height == 3)
            setType(R.drawable.bt_four_three);
        if (width == 9 && height == 16)
            setType(R.drawable.bt_nine_sixteen);
        if (width == 16 && height == 9)
            setType(R.drawable.bt_sixteen_nine);
    }

    private void setType(int draw) {
        this.setImageResource(draw);
        invalidate();
    }

    public void setColor() {
        GradientDrawable gradientDrawable = (GradientDrawable) getDrawable();
        gradientDrawable.setStroke(getWidth() / defineScale, Color.BLACK);
        paint.setColor(Color.BLACK);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float event_x = event.getX();
        float event_y = event.getY();

        if (lastClick != this) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    GradientDrawable gradientDrawable = (GradientDrawable) getDrawable();
                    gradientDrawable.setStroke(getWidth() / defineScale, Color.RED);
                    paint.setColor(Color.RED);
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (event_x < 0 || event_x > getWidth() || event_y < 0 || event_y > getHeight()) {
                        GradientDrawable gradientDrawable = (GradientDrawable) getDrawable();
                        gradientDrawable.setStroke(getWidth() / defineScale, Color.BLACK);
                        paint.setColor(Color.BLACK);
                    } else {
                        performClick();
                    }
                    break;
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean performClick() {
        //确定按钮
        lastClick = this;
        return super.performClick();
    }

    public void setLastClick(RecImageButton lastClick) {
        this.lastClick = lastClick;
    }

}
