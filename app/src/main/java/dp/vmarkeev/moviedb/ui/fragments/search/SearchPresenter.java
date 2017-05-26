package dp.vmarkeev.moviedb.ui.fragments.search;

import android.content.Context;

import dp.vmarkeev.moviedb.ui.fragments.base.BasePresenter;

/**
 * Created by vmarkeev on 25.05.2017.
 */

interface SearchPresenter extends BasePresenter {

    void getMovie(Context context, int page);
}
