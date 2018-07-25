package project.youpeng.com.cropproject.home;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import project.youpeng.com.cropproject.R;
import project.youpeng.com.cropproject.bean.ImageBean;
import project.youpeng.com.cropproject.util.PictureAdapter;
import project.youpeng.com.cropproject.widget.MyGridLayoutManager;

public class PictureActivity extends Activity {

    @BindView(R.id.rv_picture)
    RecyclerView rvPicture;

    private Intent intent;
    private int position;
    private List<ImageBean> list;
    private PictureAdapter pictureAdapter;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity);
        ButterKnife.bind(this);

        intent = getIntent();
        if (intent != null)
            position = intent.getIntExtra("position", 0);

        list = ChooseActivity.imageMap.get(position);

        rvPicture.setLayoutManager(new MyGridLayoutManager(this, 4));

        pictureAdapter = new PictureAdapter(this);
        rvPicture.setAdapter(pictureAdapter);
        if (list != null)
            pictureAdapter.setImageBeanList(list);

        pictureAdapter.setOnPictureClickListener(new PictureAdapter.OnPictureClickListener() {
            @Override
            public void onPictureClick(int pos) {
                Intent intent = new Intent(PictureActivity.this, CropPictureActivity.class);
                File file = new File(list.get(pos).getPath());
                intent.setData(Uri.fromFile(file));
                startActivity(intent);
            }
        });
    }


}
