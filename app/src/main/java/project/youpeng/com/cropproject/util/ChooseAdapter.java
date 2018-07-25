package project.youpeng.com.cropproject.util;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import project.youpeng.com.cropproject.R;
import project.youpeng.com.cropproject.widget.RecImageButton;

public class ChooseAdapter extends RecyclerView.Adapter<ChooseAdapter.MyViewHolder> {
    private List<Rectangle> rectangles;
    private RecImageButton lastClick;
    private int flag = 1;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public ChooseAdapter(List<Rectangle> rectangles) {
        this.rectangles = rectangles;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rec_contain, viewGroup, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
        //颜色初始化
        if (flag == 1 && i == 0) {
            flag = 0;
            lastClick = myViewHolder.myImageButton;
        }
        myViewHolder.myImageButton.resetType(rectangles.get(i).getWidth(), rectangles.get(i).getHeight());

        myViewHolder.myImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (lastClick != myViewHolder.myImageButton) {
                    lastClick.setColor();
                    lastClick.setLastClick(null);
                    lastClick = myViewHolder.myImageButton;
                    listener.onItemClick(view, i);
                }
            }
        });
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return rectangles.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private RecImageButton myImageButton;

        private MyViewHolder(@NonNull View itemView) {
            super(itemView);
            myImageButton = itemView.findViewById(R.id.rb_rec);
        }
    }
}
