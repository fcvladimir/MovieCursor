package dp.vmarkeev.moviedb.utils;

import android.support.v4.app.FragmentManager;

public class FragmentUtils {

    public static boolean isContainerHasFragmentByTag(FragmentManager supportFragmentManager, Class aClass) {
        return supportFragmentManager.findFragmentByTag(aClass.getSimpleName()) == null;
    }
}
