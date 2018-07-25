package project.youpeng.com.cropproject.util;

import android.graphics.Bitmap;
import android.graphics.Matrix;

public class PictureScaleUtil {
    public static Bitmap scaleBitmap(Bitmap origin, float ratio) {
        if (origin == null)
            return null;
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.preScale(ratio, ratio);
        Bitmap newBitmap = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBitmap.equals(origin))
            return newBitmap;
        origin.recycle();
        return newBitmap;
    }
}
