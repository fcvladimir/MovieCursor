package dp.vmarkeev.moviedb.ui.fragments.search;

import android.content.Context;

import dp.vmarkeev.moviedb.models.movies.MovieModel;

/**
 * Created by vmarkeev on 25.05.2017.
 */

interface SearchInteractor {

    void getMovie(Context context, int page, OnMovieListener onMovieListener, OnErrorListener onErrorListener);

    interface OnMovieListener {
        void onMovieResult(MovieModel model, boolean isLoading);

        void onLoadMoreListener(MovieModel model, boolean isLoading);
    }

    interface OnErrorListener {
        void onError(String error);
    }
}
