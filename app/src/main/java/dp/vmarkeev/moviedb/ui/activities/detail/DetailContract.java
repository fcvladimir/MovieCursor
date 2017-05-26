package dp.vmarkeev.moviedb.ui.activities.detail;

import dp.vmarkeev.moviedb.models.DetailModel;
import dp.vmarkeev.moviedb.models.trailers.TrailerModel;
import dp.vmarkeev.moviedb.ui.activities.base.BaseContract;

/**
 * Created by vmarkeev on 28.02.2017.
 */

interface DetailContract extends BaseContract {

    void onDetailsResult(DetailModel model);

    void onTrailersResult(TrailerModel model);

    void onTrailersError(String error);
}
