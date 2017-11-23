package org.ivmlab.proloop.proloop.bluesourceteam;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

/**
 * Created by Masterkey on 21.06.2015.
 */
public class DisplayUtil {
    private static Display getDisplay(Context c) {
        return ((WindowManager) c.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
    }

    public static int getDisplayWidth(Context c) {
        Point size = new Point();
        getDisplay(c).getSize(size);
        return size.x;
    }

    public static int getDisplayHeight(Context c) {
        Point size = new Point();
    getDisplay(c).getSize(size);
    return size.y;
}

    public static int getPixel(Context c, int dp) {
        Resources resources = c.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (dp * (metrics.densityDpi / 160f));
    }

    public static int getDp(Context c,int pixel) {
        Resources resources = c.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return (int) (pixel / (metrics.densityDpi / 160f));
    }
}
