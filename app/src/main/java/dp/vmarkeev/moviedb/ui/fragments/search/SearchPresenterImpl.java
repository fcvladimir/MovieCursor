package dp.vmarkeev.moviedb.ui.fragments.search;

import android.content.Context;

import dp.vmarkeev.moviedb.models.movies.MovieModel;

/**
 * Created by vmarkeev on 25.05.2017.
 */

class SearchPresenterImpl implements SearchPresenter, SearchInteractor.OnMovieListener, SearchInteractor.OnErrorListener {

    private SearchContract mView;
    private SearchInteractorImpl mInteractor;

    SearchPresenterImpl(SearchContract view) {
        mView = view;
        mInteractor = new SearchInteractorImpl();
    }

    @Override
    public void getMovie(Context context, int page) {
        mInteractor.getMovie(context, page, this, this);
    }

    @Override
    public void onMovieResult(MovieModel model, boolean isLoading) {
        if (mView != null) {
            mView.onMovieResult(model, isLoading);
        }
    }

    @Override
    public void onLoadMoreListener(MovieModel model, boolean isLoading) {
        if (mView != null) {
            mView.onLoadMoreListener(model, isLoading);
        }
    }

    @Override
    public void onError(String error) {
        if (mView != null) {
            mView.onError(error);
        }
    }

    @Override
    public void onDetach() {
        mView = null;
    }
}
