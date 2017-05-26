package dp.vmarkeev.moviedb.ui.fragments.details;

import dp.vmarkeev.moviedb.models.DetailModel;
import dp.vmarkeev.moviedb.models.trailers.TrailerModel;
import dp.vmarkeev.moviedb.ui.fragments.base.BaseContract;

/**
 * Created by vmarkeev on 28.02.2017.
 */

interface DetailsContract extends BaseContract {

    void onDetailsResult(DetailModel model);

    void onTrailersResult(TrailerModel model);

    void onTrailersError(String error);
}
