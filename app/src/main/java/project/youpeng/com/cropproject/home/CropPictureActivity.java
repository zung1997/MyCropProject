package project.youpeng.com.cropproject.home;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import project.youpeng.com.cropproject.R;
import project.youpeng.com.cropproject.util.ChooseAdapter;
import project.youpeng.com.cropproject.util.Rectangle;
import project.youpeng.com.cropproject.util.ScaleType;
import project.youpeng.com.cropproject.util.SpaceDecoration;
import project.youpeng.com.cropproject.widget.ChooseView;

public class CropPictureActivity extends AppCompatActivity {

    @BindView(R.id.rv_rectangle)
    RecyclerView rvRectangle;

    List<Rectangle> rectangles;
    ChooseAdapter chooseAdapter;
    @BindView(R.id.utv_crop)
    TextView utvCrop;
    @BindView(R.id.utv_rotate)
    TextView utvRotate;
    @BindView(R.id.utv_correct)
    TextView utvCorrect;
    TextView lastClick;
    @BindView(R.id.bt_reset)
    Button btReset;
    @BindView(R.id.bt_crop)
    Button btCrop;
    @BindView(R.id.iv_choose)
    ChooseView ivChoose;

    RelativeLayout relativeLayout;
    @BindView(R.id.ib_done)
    ImageButton ibDone;

    private int oriCropFlag = 1;
    private int crX = 0;
    private int crY = 0;
    private int crWidth = 0;
    private int crHeight = 0;

    private float originWidth;
    private float originHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.crop_picture_activity);
        ButterKnife.bind(this);

        relativeLayout = findViewById(R.id.rl_crop);

        //todo 传入intent的uri
        Intent intent = getIntent();

        initRv();
        initBottom();

        new ImageTask().execute(intent.getData());
    }

    private void initBottom() {
        lastClick = utvCrop;
        lastClick.setSelected(true);
    }

    private void initRectangle() {
        rectangles = new ArrayList<>();

        Rectangle rectangle1 = new Rectangle(ScaleType.scaleZero, ScaleType.scaleZero);
        Rectangle rectangle2 = new Rectangle(ScaleType.scaleOne, ScaleType.scaleOne);
        Rectangle rectangle3 = new Rectangle(ScaleType.scaleTwo, ScaleType.scaleThree);
        Rectangle rectangle4 = new Rectangle(ScaleType.scaleThree, ScaleType.scaleTwo);
        Rectangle rectangle5 = new Rectangle(ScaleType.scaleThree, ScaleType.scaleFour);
        Rectangle rectangle6 = new Rectangle(ScaleType.scaleFour, ScaleType.scaleThree);
        Rectangle rectangle7 = new Rectangle(ScaleType.scaleNine, ScaleType.scaleSixteen);
        Rectangle rectangle8 = new Rectangle(ScaleType.scaleSixteen, ScaleType.scaleNine);
        rectangles.add(rectangle1);
        rectangles.add(rectangle2);
        rectangles.add(rectangle3);
        rectangles.add(rectangle4);
        rectangles.add(rectangle5);
        rectangles.add(rectangle6);
        rectangles.add(rectangle7);
        rectangles.add(rectangle8);
    }

    private void initRv() {
        HashMap<Integer, Integer> stringIntegerHashMap = new HashMap<>();
        stringIntegerHashMap.put(SpaceDecoration.LEFT_DECORATION, 25);
        stringIntegerHashMap.put(SpaceDecoration.RIGHT_DECORATION, 25);
        SpaceDecoration spaceDecoration = new SpaceDecoration(stringIntegerHashMap);
        initRectangle();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rvRectangle.setLayoutManager(linearLayoutManager);
        rvRectangle.addItemDecoration(spaceDecoration);
        chooseAdapter = new ChooseAdapter(rectangles);

        //设置
        chooseAdapter.setListener(new ChooseAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Log.d("cropActivity", "onItemClick: " + rectangles.get(position).getWidth() + rectangles.get(position).getHeight());
                //重设比例尺
                ivChoose.setRecScale(rectangles.get(position).getWidth(), rectangles.get(position).getHeight());
                ivChoose.changeRec(rectangles.get(position).getWidth(), rectangles.get(position).getHeight());
            }
        });

        rvRectangle.setAdapter(chooseAdapter);

        ivChoose.setOnCropListener(new ChooseView.onCropListener() {
            @Override
            public void onCrop(int cropX, int cropY, int cropWidth, int cropHeight) {
                crX += cropX;
                crY += cropY;
                crWidth += cropWidth;
                crHeight += cropHeight;
                Log.d("cr", "onCrop: " + crX + "  " + crY + "  " + crWidth + "  " + crHeight);
            }
        });
    }

    @OnClick({R.id.utv_crop, R.id.utv_rotate, R.id.utv_correct, R.id.bt_reset, R.id.bt_crop, R.id.ib_done})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.utv_crop:
                resetUnderline(utvCrop);
                break;
            case R.id.utv_rotate:
                resetUnderline(utvRotate);
                break;
            case R.id.utv_correct:
                resetUnderline(utvCorrect);
                break;
            case R.id.bt_reset:
                ivChoose.setImageBitmap(ivChoose.getBaseImage());
                ivChoose.setFlag(ChooseView.CHANGE_IMAGE);
                oriCropFlag = 1;
                crX = 0;
                crY = 0;
                crHeight = 0;
                crWidth = 0;
                break;
            case R.id.bt_crop:
                oriCropFlag = 0;
                ivChoose.cropPicture();
                break;
            case R.id.ib_done:
                Log.d("done", "onViewClicked: ");
                if (oriCropFlag != 1)
                    new OriginalCropTask().execute(getIntent().getData());
                else
                    Toast.makeText(getApplicationContext(), "无裁剪", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    private void resetUnderline(TextView now) {
        if (lastClick != now) {
            lastClick.setSelected(false);
            now.setSelected(true);
            lastClick = now;
        }
    }

    class ImageTask extends AsyncTask<Uri, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(Uri... uris) {
            if (uris[0] != null) {
                return getBitmapRotate(uris[0], 1080, 1500);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            Log.d("bm", "onPostExecute: " + bitmap.getWidth() + " " + bitmap.getHeight());
            ivChoose.setFlag(ChooseView.DEFAULT_IMAGE);
            ivChoose.setImageBitmap(bitmap);
        }

        private Bitmap getBitmapRotate(Uri uri, float width, float height) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();

                originWidth = options.outWidth;
                originHeight = options.outHeight;


                Log.d("rotate", "getBitmapRotate: oWidth" + originWidth + " " + width);
                float rotate = originHeight / width;
                if (height / rotate > height)
                    rotate = originWidth / height;

                Log.d("rotate", "getBitmapRotate: " + rotate);

                BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
                bitmapOptions.inSampleSize = (int) rotate;
                bitmapOptions.inDither = true;
                bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;
                inputStream = getContentResolver().openInputStream(uri);
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

    //静态内部类
    class OriginalCropTask extends AsyncTask<Uri, Integer, Bitmap> {

        @Override
        protected Bitmap doInBackground(Uri... uris) {
            try {
                InputStream inputStream = getContentResolver().openInputStream(uris[0]);
                BitmapRegionDecoder bitmapRegionDecoder = BitmapRegionDecoder.newInstance(inputStream, false);
                BitmapFactory.Options options = new BitmapFactory.Options();
                float crRotate = originWidth / ivChoose.getBaseImage().getWidth();
                Bitmap bitmap = bitmapRegionDecoder.decodeRegion(new Rect((int) (crX * crRotate), (int) (crY * crRotate),
                        (int) ((crX + crWidth) * crRotate), (int) ((crY + crHeight) * crRotate)), options);
                return bitmap;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, "hello", null);
            Intent intent = new Intent(CropPictureActivity.this, SuccessActivity.class);
            startActivity(intent);
        }

    }
}
