package dp.vmarkeev.moviedb.utils;

import android.app.Activity;
import android.graphics.Point;
import android.os.Build;
import android.view.Display;

/**
 * Created by Pinta on 02.01.2017.
 */

public class CommonUtils {

    public static boolean isLollipopOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public static int getScreenWidth(Activity ac) {
        Display display = ac.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        return size.x;
    }
}
