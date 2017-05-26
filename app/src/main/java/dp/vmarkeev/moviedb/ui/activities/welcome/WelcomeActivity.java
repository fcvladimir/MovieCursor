package dp.vmarkeev.moviedb.ui.activities.welcome;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;

import dp.vmarkeev.moviedb.R;
import dp.vmarkeev.moviedb.interfaces.IShowedFragment;
import dp.vmarkeev.moviedb.ui.activities.base.BaseActivity;
import dp.vmarkeev.moviedb.ui.adapters.WelcomePagerAdapter;
import dp.vmarkeev.moviedb.ui.fragments.details.DetailsFragment;
import dp.vmarkeev.moviedb.utils.FragmentUtils;

/**
 * Created by vmarkeev on 25.05.2017.
 */

public class WelcomeActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private WelcomePagerAdapter adapter;

    private DetailsFragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initAdapter();
    }

    @Override
    protected View getContentView() {
        return View.inflate(context, R.layout.activity_welcome, null);
    }

    @Override
    protected void initPresenter() {

    }

    @Override
    protected void initViews() {
        tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        viewPager = (ViewPager) findViewById(R.id.pager);

        tabLayout.addTab(tabLayout.newTab().setText(R.string.wlm_movies_tab));
        tabLayout.addTab(tabLayout.newTab().setText(R.string.wlm_favorites_tab));
    }

    @Override
    protected void initListeners() {
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                Fragment fragment = (Fragment) adapter.instantiateItem(viewPager, tab.getPosition());
                if (fragment instanceof IShowedFragment) {
                    ((IShowedFragment) fragment).onShowedFragment();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    private void initAdapter() {
        adapter = new WelcomePagerAdapter
                (getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        if (!FragmentUtils.isContainerHasFragmentByTag(mFragmentManager, DetailsFragment.class)) {
            getSupportFragmentManager().beginTransaction().
                    remove(getSupportFragmentManager().findFragmentById(R.id.container)).commit();
        } else {
            super.onBackPressed();
        }
    }
}
