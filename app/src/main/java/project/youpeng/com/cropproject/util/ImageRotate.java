package project.youpeng.com.cropproject.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class ImageRotate {
    public static Bitmap getBitmapRotate(Uri uri, float width, float height, Context context) {
        try {
            InputStream inputStream = context.getContentResolver().openInputStream(uri);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            float originWidth = options.outWidth;
            float originHeight = options.outHeight;


            float rotate = originHeight / width;
            if (height / rotate > height)
                rotate = originWidth / height;


            BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
            bitmapOptions.inSampleSize = (int) rotate;
            bitmapOptions.inDither = true;
            bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
            inputStream = context.getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream, null, bitmapOptions);
            inputStream.close();
            return bitmap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
