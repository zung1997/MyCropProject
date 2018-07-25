package project.youpeng.com.cropproject.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;

import project.youpeng.com.cropproject.util.PicturePositionUtil;


public class ChooseView extends android.support.v7.widget.AppCompatImageView {

    public static final int DEFAULT_IMAGE = 1;
    public static final int CHANGE_IMAGE = 2;

    private Paint paint;
    private Paint textPaint;
    private Paint paintRect;
    private final int RECT_SIZE = 11;

    private int flag = 1;
    private int flag_slide = 0;
    private float lastX;
    private float lastY;
    private float recLeft;
    private float recTop;
    private float recRight;
    private float recBottom;
    private String sType;
    private float maxWidth;
    private float maxHeight;
    private float minWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getContext().getResources().getDisplayMetrics());
    private float minHeight = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 100, getContext().getResources().getDisplayMetrics());

    private int recScaleX = 0;
    private int recScaleY = 0;
    //蒙版图层
    private Xfermode xfermode;
    private PorterDuff.Mode mode = PorterDuff.Mode.SRC_OUT;

    //边界的四角坐标
    private float borderTop;
    private float borderBottom;
    private float borderLeft;
    private float borderRight;

    //保存初始图片
    private Bitmap baseImage;
    private int flagBase = 1;

    //添加裁剪坐标监听
    private onCropListener onCropListener;

    public ChooseView(Context context) {
        super(context);
        init();
    }

    public ChooseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ChooseView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public interface onCropListener {
        void onCrop(int cropX, int cropY, int cropWidth, int cropHeight);
    }

    public void setOnCropListener(ChooseView.onCropListener onCropListener) {
        this.onCropListener = onCropListener;
    }


    private void init() {
        paint = new Paint();
        paint.setFilterBitmap(false);
        xfermode = new PorterDuffXfermode(mode);

        textPaint = new Paint();
        textPaint.setColor(Color.WHITE);
        textPaint.setTextAlign(Paint.Align.CENTER);
        textPaint.setTextSize(20);

        paintRect = new Paint();
        paintRect.setColor(Color.WHITE);

    }

    @Override
    public void setImageBitmap(Bitmap bm) {
        super.setImageBitmap(bm);
        if (flagBase == 1) {
            this.baseImage = bm;
            flagBase = 0;
        }
    }

    public Bitmap getBaseImage() {
        return baseImage;
    }

    private void countMaxXY() {
        float rotNow = (float) (getWidth()) / (float) (getHeight());
        float rotOri = (float) (getDrawable().getMinimumWidth()) / (float) (getDrawable().getMinimumHeight());
        float rotate;
        if (rotNow < rotOri) {
            //X占满
            rotate = (float) (getWidth()) / (float) (getDrawable().getMinimumWidth());
            maxWidth = getWidth();
            maxHeight = rotate * getDrawable().getMinimumHeight();
        } else {
            rotate = (float) (getHeight()) / (float) (getDrawable().getMinimumHeight());
            maxHeight = getHeight();
            maxWidth = rotate * getDrawable().getMinimumWidth();
        }
        borderLeft = (getWidth() - maxWidth) / 2;
        borderRight = borderLeft + maxWidth;
        borderTop = (getHeight() - maxHeight) / 2;
        borderBottom = borderTop + maxHeight;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        countMaxXY();
        if (flag != 0) {
            if (flag == 1 || flag == 2) {
                recLeft = borderLeft;
                recRight = borderRight;
                recTop = borderTop;
                recBottom = borderBottom;
            }
            flag = 0;
        }
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        paint.setAlpha(160);
        canvas.drawRect(0, 0, getWidth(), getHeight(), paint);//蒙版

        paint.setXfermode(xfermode);
        paint.setAlpha(0);
        canvas.drawRect(recLeft, recTop, recRight, recBottom, paint);
        paint.setXfermode(null);
        sType = (int) (recRight - recLeft) + " * " + (int) (recBottom - recTop);
        canvas.drawText(sType, (recLeft + recRight) / 2, (recTop + recBottom) / 2, textPaint);

        //draw 边角的rect
        canvas.drawRect(recLeft - RECT_SIZE, recTop - RECT_SIZE, recLeft + RECT_SIZE, recTop + RECT_SIZE, paintRect);
        canvas.drawRect(recRight - RECT_SIZE, recTop - RECT_SIZE, recRight + RECT_SIZE, recTop + RECT_SIZE, paintRect);
        canvas.drawRect(recLeft - RECT_SIZE, recBottom - RECT_SIZE, recLeft + RECT_SIZE, recBottom + RECT_SIZE, paintRect);
        canvas.drawRect(recRight - RECT_SIZE, recBottom - RECT_SIZE, recRight + RECT_SIZE, recBottom + RECT_SIZE, paintRect);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float event_x = event.getX();
        float event_y = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                //判断点击是否在区域中
                flag_slide = PicturePositionUtil.typePosition(event_x, event_y, recLeft, recTop, recRight, recBottom);
                switch (flag_slide) {
                    case 0:
                        break;
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                        lastX = event_x;
                        lastY = event_y;
                        break;
                    default:
                        break;
                }
                break;
            }
            case MotionEvent.ACTION_MOVE: {

                float offsetX = event_x - lastX;
                float offsetY = event_y - lastY;
                if (recScaleY != 0 && recScaleX != 0 && flag_slide != 1) {
                    if (Math.abs(offsetX) > Math.abs(offsetY)) {
                        offsetY = Math.abs(offsetX / recScaleX) * recScaleY;
                        if (flag_slide == 2 || flag_slide == 5)
                            offsetY = offsetX / recScaleX * recScaleY;
                        else if (flag_slide == 3 || flag_slide == 4)
                            offsetY = -offsetX / recScaleX * recScaleY;
                    } else {
                        offsetX = Math.abs(offsetY / recScaleY) * recScaleX;
                        if (flag_slide == 2 || flag_slide == 5)
                            offsetX = offsetY / recScaleY * recScaleX;
                        else if (flag_slide == 3 || flag_slide == 4)
                            offsetX = -offsetY / recScaleY * recScaleX;
                    }
                }

                if (flag_slide == 2) {
                    //左上滑动
                    lastY = event_y;
                    lastX = event_x;

                    if (recRight - Math.max(recLeft + offsetX, borderLeft) < minWidth)
                        offsetX = 0;
                    if (recBottom - Math.max(recTop + offsetY, borderTop) < minHeight)
                        offsetY = 0;


                    if ((recLeft + offsetX < borderLeft || recTop + offsetY < borderTop) && recScaleX != 0) {
                        offsetX = 0;
                        offsetY = 0;
                    }
                    recLeft = Math.max(recLeft + offsetX, borderLeft);
                    recTop = Math.max(recTop + offsetY, borderTop);
                } else if (flag_slide == 3) {
                    //右上
                    if (Math.min(recRight + offsetX, borderRight) - recLeft < minWidth)
                        offsetX = 0;
                    if (recBottom - Math.max(recTop + offsetY, borderTop) < minHeight)
                        offsetY = 0;

                    lastY = event_y;
                    lastX = event_x;

                    if ((recTop + offsetY < borderTop || recRight + offsetX > borderRight) && recScaleX != 0) {
                        offsetX = 0;
                        offsetY = 0;
                    }
                    recTop = Math.max(recTop + offsetY, borderTop);
                    recRight = Math.min(recRight + offsetX, borderRight);
                } else if (flag_slide == 4) {
                    //左下
                    if (recRight - Math.max(recLeft + offsetX, borderLeft) < minWidth)
                        offsetX = 0;
                    if (Math.min(recBottom + offsetY, borderBottom) - recTop < minHeight)
                        offsetY = 0;

                    lastX = event_x;
                    lastY = event_y;

                    if ((recLeft + offsetX < borderLeft || recBottom + offsetY > borderBottom) && recScaleX != 0) {
                        offsetX = 0;
                        offsetY = 0;
                    }
                    recLeft = Math.max(recLeft + offsetX, borderLeft);
                    recBottom = Math.min(recBottom + offsetY, borderBottom);
                } else if (flag_slide == 5) {
                    //右下
                    if (Math.min(recRight + offsetX, borderRight) - recLeft < minWidth)
                        offsetX = 0;
                    if (Math.min(recBottom + offsetY, borderBottom) - recTop < minHeight)
                        offsetY = 0;

                    lastX = event_x;
                    lastY = event_y;

                    if ((recRight + offsetX > borderRight || recBottom + offsetY > borderBottom) && recScaleX != 0) {
                        offsetX = 0;
                        offsetY = 0;
                    }
                    recRight = Math.min(recRight + offsetX, borderRight);
                    recBottom = Math.min(recBottom + offsetY, borderBottom);
                } else if (flag_slide == 1) {
                    lastY = event_y;
                    lastX = event_x;
                    //变更坐标,矩形框的移动
                    if (recLeft + offsetX < borderLeft || recRight + offsetX > borderRight) {
                        offsetX = 0;
                    }
                    if (recTop + offsetY < borderTop || recBottom + offsetY > borderBottom) {
                        offsetY = 0;
                    }

                    recLeft += offsetX;
                    recRight += offsetX;
                    recTop += offsetY;
                    recBottom += offsetY;
                }

                //方框没有暂时没有拉伸等操作，因此数值无变化
                sType = (int) (recRight - recLeft) + " * " + (int) (recBottom - recTop);
                break;
            }
            case MotionEvent.ACTION_UP: {
                break;
            }
            default:
                break;
        }
        //图形重绘
        invalidate();
        return super.onTouchEvent(event);
    }

    public void setFlag(int flag) {
        this.flag = flag;
    }

    public void setRecScale(int x, int y) {
        this.recScaleX = x;
        this.recScaleY = y;
    }

    public void changeRec(int x, int y) {
        float newLeft, newRight, newTop, newBottom;
        flag = 0;
        if (x == 0 && y == 0) {
            x = 1;
            y = 1;
        }
        int mX = (int) (maxHeight / y * x);
        int mY = (int) (maxWidth / x * y);
        if (mX > getWidth()) {
            //以Y占满为基准,即Y>X
            // getHeight/y 每一小份
            newLeft = borderLeft;
            newRight = borderRight;
            newTop = (maxHeight - mY) / 2 + borderTop;
            newBottom = newTop + mY;
            minWidth = minHeight / y * x;
        } else {
            //X为基准 每一小份为 getWidth/x
            newTop = borderTop;
            newBottom = borderBottom;
            newLeft = (maxWidth - mX) / 2 + borderLeft;
            newRight = newLeft + mX;
            minHeight = minWidth / x * y;
        }
        //好像没规律？？？
        ValueAnimator vaLeft = ValueAnimator.ofFloat(recLeft, newLeft);
        vaLeft.setDuration(1000);
        vaLeft.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                recLeft = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        ValueAnimator vaRight = ValueAnimator.ofFloat(recRight, newRight);
        vaRight.setDuration(1000);
        vaRight.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                recRight = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        ValueAnimator vaTop = ValueAnimator.ofFloat(recTop, newTop);
        vaTop.setDuration(1000);
        vaTop.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                recTop = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });

        ValueAnimator vaBottom = ValueAnimator.ofFloat(recBottom, newBottom);
        vaBottom.setDuration(1000);
        vaBottom.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                recBottom = (float) valueAnimator.getAnimatedValue();
                invalidate();
            }
        });
        //动画集合
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(vaTop, vaBottom, vaLeft, vaRight);
        animatorSet.start();
    }

    public void cropPicture() {
        new CropTask().execute((BitmapDrawable) getDrawable());
    }

    //静态内部类
    class CropTask extends AsyncTask<BitmapDrawable, Integer, Bitmap> {


        @Override
        protected Bitmap doInBackground(BitmapDrawable... bitmapDrawables) {
            if (bitmapDrawables[0] != null) {
                float count = (float) (bitmapDrawables[0].getBitmap().getHeight()) / maxHeight;
                onCropListener.onCrop((int) ((recLeft - borderLeft) * count), (int) ((recTop - borderTop) * count),
                        (int) ((recRight - recLeft) * count), (int) ((recBottom - recTop) * count));
                return Bitmap.createBitmap(bitmapDrawables[0].getBitmap(), (int) ((recLeft - borderLeft) * count), (int) ((recTop - borderTop) * count),
                        (int) ((recRight - recLeft) * count), (int) ((recBottom - recTop) * count));
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            setImageBitmap(bitmap);
            setFlag(CHANGE_IMAGE);
        }
    }
}
