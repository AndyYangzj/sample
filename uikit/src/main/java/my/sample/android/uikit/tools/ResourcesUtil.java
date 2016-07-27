package my.sample.android.uikit.tools;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;

/**
 * Created by Sunkist on 2016/4/7.
 * 简化应用资源获取的代码
 */
public class ResourcesUtil {
    /**
     * 简化获取Drawable的书写过程
     *
     * @param drawableResId
     * @return
     */
    public static Drawable getDrawable(Resources resources, int drawableResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return resources.getDrawable(drawableResId, null);
        } else {
            return resources.getDrawable(drawableResId);
        }
    }

    /**
     * 简化获取颜色
     *  @param resources
     * @param colorResId
     */
    public static int getColor(Resources resources, int colorResId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return resources.getColor(colorResId, null);
        } else {
            return resources.getColor(colorResId);
        }
    }
}
