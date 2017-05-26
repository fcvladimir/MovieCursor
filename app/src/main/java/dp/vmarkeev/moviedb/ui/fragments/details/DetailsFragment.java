package dp.vmarkeev.moviedb.ui.fragments.details;

import android.animation.Animator;
import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.animation.FastOutSlowInInterpolator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewStub;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import dp.vmarkeev.moviedb.Config;
import dp.vmarkeev.moviedb.Consts;
import dp.vmarkeev.moviedb.R;
import dp.vmarkeev.moviedb.db.DBHelper;
import dp.vmarkeev.moviedb.models.DetailModel;
import dp.vmarkeev.moviedb.models.trailers.TrailerModel;
import dp.vmarkeev.moviedb.ui.adapters.TrailerAdapter;
import dp.vmarkeev.moviedb.ui.fragments.base.BaseFragment;
import dp.vmarkeev.moviedb.ui.views.DividerItemDecoration;
import dp.vmarkeev.moviedb.utils.CommonUtils;
import dp.vmarkeev.moviedb.utils.DialogUtils;

/**
 * Created by vmarkeev on 26.05.2017.
 */

public class DetailsFragment extends BaseFragment implements DetailsContract, TrailerAdapter.OnTrailerClickListener, View.OnClickListener {

    private String id;
    private DetailModel detailModel;

    private ImageView noConnection;
    private TextView noResults;
    private ProgressBar loading;
    private ScrollView sv_movie_detail;

    private TextView tv_movie_detail_name;
    private TextView tv_movie_detail_synopsis;
    private TextView tv_movie_detail_year;
    private TextView tv_movie_detail_duration;
    private TextView tv_movie_detail_rating;
    private TextView tv_movie_detail_check_trailers;
    private ImageView iv_movie_detail_poster;
    private RecyclerView rv_movie_detail_trailer;

    private static final int ANIM_DURATION = 400;
    private int color;
    private int cx;
    private int cy;

    private DetailsPresenterImpl mPresenter;

    public DetailsFragment() {

    }

    public static DetailsFragment newInstance(int centerX, int centerY, int color, String id) {
        Bundle args = new Bundle();
        args.putInt("cx", centerX);
        args.putInt("cy", centerY);
        args.putInt("color", color);
        args.putString(Consts.IntentConstant.ID, id);
        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (CommonUtils.isLollipopOrHigher()) {
            // To run the animation as soon as the view is layout in the view hierarchy we add this
            // listener and remove it
            // as soon as it runs to prevent multiple animations if the view changes bounds
            mView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
                @Override
                public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                           int oldRight, int oldBottom) {
                    v.removeOnLayoutChangeListener(this);
                    color = getArguments().getInt("color");
                    cx = getArguments().getInt("cx");
                    cy = getArguments().getInt("cy");

                    // get the hypothenuse so the radius is from one corner to the other
                    int radius = (int) Math.hypot(right, bottom);

                    Animator reveal = ViewAnimationUtils.createCircularReveal(v, cx, cy, 0, radius);
                    reveal.setInterpolator(new FastOutSlowInInterpolator());
                    reveal.setDuration(ANIM_DURATION);
                    reveal.start();
                    startColorAnimation(mView, color, ContextCompat.getColor(context, R.color.white), ANIM_DURATION);
                }
            });
        } else {
            mView.setBackgroundColor(ContextCompat.getColor(context, R.color.white));
        }

        getDetail();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_detail;
    }

    @Override
    protected void initPresenter() {
        mPresenter = new DetailsPresenterImpl(this);
    }

    @Override
    protected void initViews() {
        loading = (ProgressBar) mView.findViewById(android.R.id.empty);
        sv_movie_detail = (ScrollView) mView.findViewById(R.id.sv_movie_detail);
        tv_movie_detail_name = (TextView) mView.findViewById(R.id.tv_movie_detail_name);
        tv_movie_detail_synopsis = (TextView) mView.findViewById(R.id.tv_movie_detail_synopsis);
        iv_movie_detail_poster = (ImageView) mView.findViewById(R.id.iv_movie_detail_poster);
        tv_movie_detail_year = (TextView) mView.findViewById(R.id.tv_movie_detail_year);
        tv_movie_detail_duration = (TextView) mView.findViewById(R.id.tv_movie_detail_duration);
        tv_movie_detail_rating = (TextView) mView.findViewById(R.id.tv_movie_detail_rating);
        tv_movie_detail_check_trailers = (TextView) mView.findViewById(R.id.tv_movie_detail_check_trailers);
        rv_movie_detail_trailer = (RecyclerView) mView.findViewById(R.id.rv_movie_detail_trailer);
    }

    @Override
    protected void getDataFromBundle() {
        id = getArguments().getString(Consts.IntentConstant.ID);
    }

    @Override
    protected void initListeners() {
        tv_movie_detail_check_trailers.setOnClickListener(this);
        mView.findViewById(R.id.btn_movie_detail_mark_as_favorite).setOnClickListener(this);
    }

    private void getDetail() {
        mPresenter.getDetails(context, id);
    }

    private void startColorAnimation(final View view, final int startColor, final int endColor, int duration) {
        ValueAnimator anim = new ValueAnimator();
        anim.setIntValues(startColor, endColor);
        anim.setEvaluator(new ArgbEvaluator());
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                view.setBackgroundColor((Integer) valueAnimator.getAnimatedValue());
            }
        });
        anim.setDuration(duration);
        anim.start();
    }

    @Override
    public void onError(String error) {
        loading.setVisibility(View.GONE);
        if (sv_movie_detail.getVisibility() == View.GONE) {
            if (TextUtils.isEmpty(error)) {
                checkConnectivity();
            } else {
                setNoResultsVisibility(View.VISIBLE, error);
            }
        } else {
            if (TextUtils.isEmpty(error)) {
                DialogUtils.show(context, R.string.err_no_internet_error);
            } else {
                DialogUtils.show(context, error);
            }
        }
    }

    private void checkConnectivity() {
        if (noConnection == null) {
            final ViewStub stub = (ViewStub) mView.findViewById(R.id.stub_no_connection);
            noConnection = (ImageView) stub.inflate();
            noConnection.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getDetail();
                    loading.setVisibility(View.VISIBLE);
                }
            });
        }
    }

    private void setNoResultsVisibility(int visibility, String error) {
        if (visibility == View.VISIBLE) {
            if (noResults == null) {
                noResults = (TextView) ((ViewStub)
                        mView.findViewById(R.id.stub_no_search_results)).inflate();
                noResults.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        getDetail();
                        loading.setVisibility(View.VISIBLE);
                    }
                });
            }
            String message =
                    getString(R.string.no_results);
            if (error != null) {
                message = error;
            }
            SpannableStringBuilder ssb = new SpannableStringBuilder(message);
            ssb.setSpan(new StyleSpan(Typeface.ITALIC),
                    message.indexOf('“') + 1,
                    message.length() - 1,
                    Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            noResults.setText(ssb);
        }
        if (noResults != null) {
            noResults.setVisibility(visibility);
        }
    }

    @Override
    public void onDetailsResult(DetailModel model) {
        sv_movie_detail.setVisibility(View.VISIBLE);
        if (noConnection != null) {
            noConnection.setVisibility(View.GONE);
        }
        if (noResults != null) {
            noResults.setVisibility(View.GONE);
        }
        loading.setVisibility(View.GONE);
        fillViews(model);
    }

    private void fillViews(DetailModel model) {
        detailModel = model;
        tv_movie_detail_name.setText(model.getTitle());
        tv_movie_detail_synopsis.setText(model.getOverview());
        tv_movie_detail_year.setText(model.getRelease_date());
        tv_movie_detail_duration.setText(getString(R.string.dt_movie_detail_duration, model.getRuntime()));
        tv_movie_detail_rating.setText(getString(R.string.dt_movie_detail_vote, model.getVote_average()));

        Picasso.with(context)
                .load(Config.IMAGE_URL + model.getPoster_path())
                .into(iv_movie_detail_poster);
    }

    @Override
    public void onTrailersResult(TrailerModel model) {
        tv_movie_detail_check_trailers.setVisibility(View.GONE);
        initLinearLayoutManager();
        TrailerAdapter trailerAdapter = new TrailerAdapter(this);
        rv_movie_detail_trailer.setHasFixedSize(true);
        rv_movie_detail_trailer.setAdapter(trailerAdapter);
        trailerAdapter.refresh(model.getResults());
    }

    public void initLinearLayoutManager() {
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(context);
        rv_movie_detail_trailer.setLayoutManager(mLayoutManager);
        rv_movie_detail_trailer.addItemDecoration(new DividerItemDecoration(context));
    }

    @Override
    public void onTrailersError(String error) {
        DialogUtils.show(context, error);
    }

    @Override
    public void onTrailerClick(String position) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(Config.YOUTUBE_URL + position)));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_movie_detail_check_trailers:
                mPresenter.getTrailers(context, id);
                break;
            case R.id.btn_movie_detail_mark_as_favorite:
                DBHelper dbHelper = new DBHelper(context);
                boolean isAdded = dbHelper.addMovie(id, detailModel.getPoster_path());
                DialogUtils.show(context, isAdded ? R.string.dt_movie_detail_mark_as_favorite_success : R.string.dt_movie_detail_mark_as_favorite_failure);
                break;
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mPresenter.onDetach();
    }
}
