package project.youpeng.com.cropproject.util;

public class PicturePositionUtil {

    private static int min_position = 44;

    public static int typePosition(float eventX, float eventY, float recLeft, float recTop, float recRight, float recBottom) {
        int flag_slide = 0;
        if (eventX < recLeft + min_position && eventX > recLeft - min_position && eventY > recTop - min_position && eventY < recTop + min_position) {
            //改变左上坐标
            flag_slide = 2;
        } else if (eventX < recRight + min_position && eventX > recRight - min_position && eventY > recTop - min_position && eventY < recTop + min_position) {
            flag_slide = 3;
        } else if (eventX < recLeft + min_position && eventX > recLeft - min_position && eventY < recBottom + min_position && eventY > recBottom - min_position) {
            flag_slide = 4;
        } else if (eventX < recRight + min_position && eventX > recRight - min_position && eventY < recBottom + min_position && eventY > recBottom - min_position) {
            flag_slide = 5;
        } else if (eventX > recLeft && eventX < recRight && eventY > recTop && eventY < recBottom) {
            //符合滑动规则
            flag_slide = 1;
        } else {
            flag_slide = 0;
        }
        return flag_slide;
    }
}
