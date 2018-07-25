package project.youpeng.com.cropproject.util;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import project.youpeng.com.cropproject.R;
import project.youpeng.com.cropproject.bean.ImageBean;


public class PictureAdapter extends RecyclerView.Adapter<PictureAdapter.PictureViewHolder> implements View.OnClickListener {

    private List<ImageBean> imageBeanList;
    private Context context;
    private OnPictureClickListener onPictureClickListener;

    private int countCreate = 0;
    private int countBind = 0;

    @Override
    public void onClick(View view) {
        onPictureClickListener.onPictureClick((Integer) view.getTag());
    }

    public interface OnPictureClickListener {
        void onPictureClick(int position);
    }

    public PictureAdapter(Context context) {
        this.context = context;
    }

    public void setImageBeanList(List<ImageBean> imageBeanList) {
        this.imageBeanList = imageBeanList;
    }

    public void setOnPictureClickListener(OnPictureClickListener onPictureClickListener) {
        this.onPictureClickListener = onPictureClickListener;
    }

    @NonNull
    @Override
    public PictureViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        countCreate++;
        long start = System.nanoTime();
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.picture_container, viewGroup, false);
        view.setOnClickListener(this);
        PictureViewHolder pictureViewHolder = new PictureViewHolder(view);
        long end = System.nanoTime();
        Log.d("timeCreate", "onCreateViewHolder: time == " + (end - start));
        return pictureViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PictureViewHolder pictureViewHolder, int i) {

        GlideApp.with(context)
                .load(imageBeanList.get(i).getPath())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_launcher_background)
                .centerCrop()
                .into(pictureViewHolder.imageView);
        pictureViewHolder.itemView.setTag(i);
    }

    @Override
    public int getItemCount() {
        return imageBeanList.size();
    }

    static class PictureViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PictureViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iv_picture);
        }
    }

    @Override
    public void onViewRecycled(@NonNull PictureViewHolder holder) {
        super.onViewRecycled(holder);
        GlideApp.with(context).clear(holder.imageView);
    }
}
