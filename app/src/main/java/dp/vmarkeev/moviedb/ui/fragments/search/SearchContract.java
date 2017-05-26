package dp.vmarkeev.moviedb.ui.fragments.search;

import dp.vmarkeev.moviedb.models.movies.MovieModel;
import dp.vmarkeev.moviedb.ui.fragments.base.BaseContract;

/**
 * Created by vmarkeev on 25.05.2017.
 */

interface SearchContract extends BaseContract {

    void onMovieResult(MovieModel model, boolean isLoading);

    void onLoadMoreListener(MovieModel model, boolean isLoading);
}
