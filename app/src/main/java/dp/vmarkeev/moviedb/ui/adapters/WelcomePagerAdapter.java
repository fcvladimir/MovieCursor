package dp.vmarkeev.moviedb.ui.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import dp.vmarkeev.moviedb.ui.fragments.favorites.FavoritesFragment;
import dp.vmarkeev.moviedb.ui.fragments.search.SearchFragment;

/**
 * Created by Pinta on 30.12.2016.
 */

public class WelcomePagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public WelcomePagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                return new SearchFragment();
            case 1:
                return new FavoritesFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
