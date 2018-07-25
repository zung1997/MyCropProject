package project.youpeng.com.cropproject.util;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import project.youpeng.com.cropproject.R;
import project.youpeng.com.cropproject.bean.ImageBean;

public class AlbumAdapter extends RecyclerView.Adapter<AlbumAdapter.MyRecViewHolder> implements View.OnClickListener {
    private List<ImageBean> firstImage;
    private Context context;
    private OnItemClickListener onItemClickListener;

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null)
            onItemClickListener.onItemClick((Integer) view.getTag());
    }

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public AlbumAdapter(Context context) {
        this.context = context;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public MyRecViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.album_item, viewGroup, false);
        view.setOnClickListener(this);
        return new MyRecViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return firstImage.size();
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecViewHolder myRecViewHolder, int i) {
        File file = new File(firstImage.get(i).getPath());
        myRecViewHolder.imageView.setImageBitmap(ImageRotate.getBitmapRotate(Uri.fromFile(file), 540, 540, context));
        myRecViewHolder.name.setText(firstImage.get(i).getParentName());
        myRecViewHolder.path.setText(firstImage.get(i).getParentPath());

        myRecViewHolder.itemView.setTag(i);
    }

    public void setFirstImage(List<ImageBean> firstImage) {
        this.firstImage = firstImage;
    }

    static class MyRecViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView name;
        TextView path;

        public MyRecViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.rect_iv);
            name = itemView.findViewById(R.id.rect_name);
            path = itemView.findViewById(R.id.rect_path);
        }
    }

    @Override
    public void onViewRecycled(@NonNull MyRecViewHolder holder) {
        super.onViewRecycled(holder);
        
    }
}
