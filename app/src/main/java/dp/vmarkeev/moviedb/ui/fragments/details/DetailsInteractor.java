package dp.vmarkeev.moviedb.ui.fragments.details;

import android.content.Context;

import dp.vmarkeev.moviedb.models.DetailModel;
import dp.vmarkeev.moviedb.models.trailers.TrailerModel;

/**
 * Created by vmarkeev on 28.02.2017.
 */

interface DetailsInteractor {

    void getDetails(Context context, String id, OnDetailsListener onDetailsListener, OnDetailsErrorListener onDetailsErrorListener);

    void getTrailers(Context context, String id, OnTrailersListener onTrailersListener, OnTrailersErrorListener onTrailersErrorListener);

    interface OnDetailsListener {
        void onDetailsResult(DetailModel model);
    }

    interface OnTrailersListener {
        void onTrailersResult(TrailerModel model);
    }

    interface OnDetailsErrorListener {
        void onDetailsError(String error);
    }

    interface OnTrailersErrorListener {
        void onTrailersError(String error);
    }
}
