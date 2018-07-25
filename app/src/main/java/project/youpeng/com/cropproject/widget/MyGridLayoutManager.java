package project.youpeng.com.cropproject.widget;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;

public class MyGridLayoutManager extends GridLayoutManager {
    public static int count = 0;

    public MyGridLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public MyGridLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public MyGridLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
    }

    public static int coun = 0;

    @Override
    public int scrollVerticallyBy(int dy, RecyclerView.Recycler recycler, RecyclerView.State state) {
        coun = 0;
        Log.d("scrollVB", "scrollVerticallyBy: " + dy);
        Log.d("height", "scrollVerticallyBy: " + getItemCount());
        Log.d("height", "scrollVerticallyBy: " + getMinimumHeight());

        return super.scrollVerticallyBy(dy, recycler, state);
    }

    @Override
    public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
        super.onLayoutChildren(recycler, state);
    }
}
