package dp.vmarkeev.moviedb.ui.activities.main;

import dp.vmarkeev.moviedb.models.movies.MovieModel;
import dp.vmarkeev.moviedb.ui.activities.base.BaseContract;

/**
 * Created by vmarkeev on 28.02.2017.
 */

interface MainContract extends BaseContract {

    void onMovieResult(MovieModel model, boolean isLoading);

    void onLoadMoreListener(MovieModel model, boolean isLoading);

    void onFavoritesListener(MovieModel model);
}
