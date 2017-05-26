package dp.vmarkeev.moviedb.ui.fragments.base;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import dp.vmarkeev.moviedb.utils.L;

abstract public class BaseFragment extends Fragment {

    protected Context context;
    protected View mView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        log(getClass().getSimpleName());

        if (mView == null) {
            mView = inflater.inflate(getLayoutId(), container, false);
            context = getContext();
            initPresenter();
            initViews();
            getDataFromBundle();
            initListeners();
        }
        return mView;
    }

    protected abstract int getLayoutId();

    protected abstract void initPresenter();

    protected abstract void initViews();

    protected abstract void getDataFromBundle();

    protected abstract void initListeners();

    protected void log(Object o) {
        L.d(o);
    }
}
