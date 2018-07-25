package project.youpeng.com.cropproject.util;

import android.graphics.Rect;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import java.util.HashMap;

public class SpaceDecoration extends RecyclerView.ItemDecoration {
    private HashMap<Integer, Integer> mSpaceValueMap;
    public static final int TOP_DECORATION = 1;
    public static final int BOTTOM_DECORATION = 2;
    public static final int LEFT_DECORATION = 3;
    public static final int RIGHT_DECORATION = 4;

    public SpaceDecoration(HashMap<Integer, Integer> mSpaceValueMap) {
        this.mSpaceValueMap = mSpaceValueMap;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (mSpaceValueMap.get(TOP_DECORATION) != null)
            outRect.top = mSpaceValueMap.get(TOP_DECORATION);
        if (mSpaceValueMap.get(LEFT_DECORATION) != null)
            outRect.left = mSpaceValueMap.get(LEFT_DECORATION);
        if (mSpaceValueMap.get(RIGHT_DECORATION) != null)
            outRect.right = mSpaceValueMap.get(RIGHT_DECORATION);
        if (mSpaceValueMap.get(BOTTOM_DECORATION) != null)
            outRect.bottom = mSpaceValueMap.get(BOTTOM_DECORATION);
    }
}
