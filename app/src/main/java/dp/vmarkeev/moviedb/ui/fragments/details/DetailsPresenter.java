package dp.vmarkeev.moviedb.ui.fragments.details;

import android.content.Context;

import dp.vmarkeev.moviedb.ui.fragments.base.BasePresenter;

/**
 * Created by vmarkeev on 28.02.2017.
 */

interface DetailsPresenter extends BasePresenter {

    void getDetails(Context context, String id);

    void getTrailers(Context context, String id);
}
