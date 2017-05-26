package dp.vmarkeev.moviedb.ui.fragments.details;

import android.content.Context;

import dp.vmarkeev.moviedb.models.DetailModel;
import dp.vmarkeev.moviedb.models.trailers.TrailerModel;

/**
 * Created by vmarkeev on 28.02.2017.
 */

class DetailsPresenterImpl implements DetailsPresenter,
        DetailsInteractor.OnDetailsListener,
        DetailsInteractor.OnDetailsErrorListener,
        DetailsInteractor.OnTrailersListener,
        DetailsInteractor.OnTrailersErrorListener {

    private DetailsContract mView;
    private DetailsInteractorImpl mInteractor;

    DetailsPresenterImpl(DetailsContract view) {
        mView = view;
        mInteractor = new DetailsInteractorImpl();
    }

    @Override
    public void getDetails(Context context, String id) {
        mInteractor.getDetails(context, id, this, this);
    }

    @Override
    public void getTrailers(Context context, String id) {
        mInteractor.getTrailers(context, id, this, this);
    }

    @Override
    public void onDetailsResult(DetailModel model) {
        if (mView != null) {
            mView.onDetailsResult(model);
        }
    }

    @Override
    public void onDetailsError(String error) {
        if (mView != null) {
            mView.onError(error);
        }
    }

    @Override
    public void onTrailersResult(TrailerModel model) {
        if (mView != null) {
            mView.onTrailersResult(model);
        }
    }

    @Override
    public void onTrailersError(String error) {
        if (mView != null) {
            mView.onTrailersError(error);
        }
    }

    @Override
    public void onDetach() {
        mView = null;
    }
}
