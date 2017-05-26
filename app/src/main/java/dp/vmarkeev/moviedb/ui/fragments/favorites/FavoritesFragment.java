package dp.vmarkeev.moviedb.ui.fragments.favorites;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.ViewStub;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import dp.vmarkeev.moviedb.Consts;
import dp.vmarkeev.moviedb.R;
import dp.vmarkeev.moviedb.db.DBHelper;
import dp.vmarkeev.moviedb.interfaces.IShowedFragment;
import dp.vmarkeev.moviedb.models.movies.Results;
import dp.vmarkeev.moviedb.ui.adapters.MovieAdapter;
import dp.vmarkeev.moviedb.ui.fragments.base.BaseFragment;
import dp.vmarkeev.moviedb.ui.views.ItemOffsetDecorationGrid;

/**
 * Created by vmarkeev on 25.05.2017.
 */

public class FavoritesFragment extends BaseFragment implements IShowedFragment, LoaderManager.LoaderCallbacks<Cursor> {

    private boolean mIsLoading = true;
    private int PAGE = 0;

    private TextView noResults;
    private ProgressBar loading;
    private GridLayoutManager mLayoutManager;
    private RecyclerView lvData;
    private DBHelper db;
    private MovieAdapter scAdapter;

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initLinearLayoutManager();

        // открываем подключение к БД
        db = new DBHelper(context);

        // добавляем контекстное меню к списку
        registerForContextMenu(lvData);

        scAdapter = new MovieAdapter(getActivity());
        lvData.setAdapter(scAdapter);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initViews() {
        lvData = (RecyclerView) mView.findViewById(R.id.rv_movie);
        loading = (ProgressBar) mView.findViewById(android.R.id.empty);
    }

    @Override
    protected void getDataFromBundle() {

    }

    @Override
    protected void initListeners() {
        lvData.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy < 0 || !mIsLoading) return;
                final int visibleItemCount = recyclerView.getChildCount();
                final int totalItemCount = mLayoutManager.getItemCount();
                final int firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if ((totalItemCount - visibleItemCount) <= (firstVisibleItem + Consts.VISIBLE_THRESHOLD)) {
                    mIsLoading = false;
                    PAGE++;
                    getMoviesDb();
                }
            }
        });
    }

    private void initLinearLayoutManager() {
        int column = getResources().getInteger(R.integer.num_columns);
        mLayoutManager = new GridLayoutManager(context, column);
        lvData.setLayoutManager(mLayoutManager);
        ItemOffsetDecorationGrid itemDecoration = new ItemOffsetDecorationGrid(context, R.dimen.padding_card_view);
        lvData.addItemDecoration(itemDecoration);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new MyCursorLoader(id, context, db);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        loading.setVisibility(View.GONE);
        ArrayList<Results> resultses = new ArrayList<>();
        Results results;
        log("cursor.getCount() = " + cursor.getCount());
        if (cursor.getCount() == 0) {
            mIsLoading = false;
            if (scAdapter.getItemCount() == 0) {
                setNoResultsVisibility(View.VISIBLE);
            }
            return;
        }
        mIsLoading = true;
        if (cursor.moveToNext()) {
            do {
                String movie_id = cursor.getString(cursor.getColumnIndex("movie_id"));
                String movie_poster = cursor.getString(cursor.getColumnIndex("movie_poster"));
                results = new Results(movie_id, movie_poster);
                resultses.add(results);
                // do what ever you want here
            } while (cursor.moveToNext());
        }
        cursor.close();

        if (PAGE == 0) {
            scAdapter.refresh(resultses);
        } else {
            scAdapter.addAndUpdate(resultses);
        }
    }

    private void setNoResultsVisibility(int visibility) {
        if (visibility == View.VISIBLE) {
            if (noResults == null) {
                noResults = (TextView) ((ViewStub)
                        mView.findViewById(R.id.stub_no_search_results)).inflate();
            }
            String message =
                    getString(R.string.no_results);
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
    public void onLoaderReset(Loader<Cursor> loader) {

    }

    @Override
    public void onShowedFragment() {
        log("onShowedFragment");

        // создаем лоадер для чтения данных
        getMoviesDb();
    }

    static class MyCursorLoader extends CursorLoader {

        DBHelper db;
        int id;

        public MyCursorLoader(int id, Context context, DBHelper db) {
            super(context);
            this.db = db;
            this.id = id;
        }

        @Override
        public Cursor loadInBackground() {
            Cursor cursor = db.getAllData(id);
//            try {
//                TimeUnit.SECONDS.sleep(3);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return cursor;
        }

    }

    private void getMoviesDb() {
        getActivity().getSupportLoaderManager().initLoader(PAGE, null, this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        // закрываем подключение при выходе
        db.close();
    }
}
