package project.youpeng.com.cropproject.home;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.youpeng.com.cropproject.R;
import project.youpeng.com.cropproject.bean.ImageBean;
import project.youpeng.com.cropproject.util.AlbumAdapter;

public class ChooseActivity extends Activity {

    public static Map<Integer, List<ImageBean>> imageMap;
    private List<ImageBean> firstImage;
    AlbumAdapter albumAdapter;
    @BindView(R.id.rv_album)
    RecyclerView rvAlbum;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.album_activity);
        ButterKnife.bind(this);

        imageMap = new HashMap<>();
        firstImage = new ArrayList<>();
        //放入异步线程
        Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media.DISPLAY_NAME,
                        MediaStore.Images.Media.DATA, MediaStore.Images.Media.BUCKET_ID,
                        MediaStore.Images.Media.DATE_TAKEN},
                null, null, null);
        long now = System.currentTimeMillis();
        Log.d("now time ", "onCreate: "+now);
        while (cursor.moveToNext()) {

            String name = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DISPLAY_NAME));
            String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            int parentId = cursor.getInt(cursor.getColumnIndex(MediaStore.Images.Media.BUCKET_ID));
            long time = cursor.getLong(cursor.getColumnIndex(MediaStore.Images.Media.DATE_TAKEN));
            Log.d("datetaken", "onCreate: "+name+"   "+time);
            File parent = new File(path).getParentFile();
            String parentPath = parent.getAbsolutePath();
            String parentName = parent.getName();

            Log.d("datetaken", "onCreate: "+parentPath);

            ImageBean imageBean = new ImageBean(path, name, parentPath, parentName, parentId);

            if (imageMap.containsKey(parentId)) {
                imageMap.get(parentId).add(imageBean);
            } else {
                List<ImageBean> imageBeanList = new ArrayList<>();
                imageBeanList.add(imageBean);
                imageMap.put(parentId, imageBeanList);

                firstImage.add(imageBean);
            }
        }
        //获取uri 的方法
        //Uri uri = Uri.fromFile(new File(imagePath.get(0)));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rvAlbum.setLayoutManager(linearLayoutManager);
        rvAlbum.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        albumAdapter = new AlbumAdapter(this);
        rvAlbum.setAdapter(albumAdapter);
        initList();

        albumAdapter.setOnItemClickListener(new AlbumAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                //todo 进入相册的 activity
                new AddListTask().execute(position);
            }
        });

    }

    private void initList() {
        albumAdapter.setFirstImage(firstImage);
    }

    //静态内部类
    class AddListTask extends AsyncTask<Integer, Integer, Integer> {

        @Override
        protected Integer doInBackground(Integer... integers) {
            int position = integers[0];
            addPicture(position);
            return position;
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            Intent intent = new Intent(ChooseActivity.this, PictureActivity.class);
            intent.putExtra("position", firstImage.get(integer).getParentId());
            startActivity(intent);
        }
    }

    private void addPicture(int position) {
        int count = 10000;
        List<ImageBean> list = imageMap.get(firstImage.get(position).getParentId());
        int size = list.size();
        while (count-- > 0) {
            list.add(list.get(count % size));
        }
    }

}
