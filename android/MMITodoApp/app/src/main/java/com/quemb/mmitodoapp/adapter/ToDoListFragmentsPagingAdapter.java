package com.quemb.mmitodoapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.controller.ToDoListMapFragment;
import com.quemb.mmitodoapp.controller.TodoListFragment;
import com.quemb.mmitodoapp.model.FragmentTabItem;

import java.util.ArrayList;

/**
 * Created by stefanmeschke on 04.07.16.
 */
public class ToDoListFragmentsPagingAdapter extends FragmentPagerAdapter {

    private final Context mContext;

    private ArrayList<FragmentTabItem> mFragmentTabItems;


    public ToDoListFragmentsPagingAdapter(FragmentManager fragmentManager, Context context) {
        super(fragmentManager);

        mContext = context;

        mFragmentTabItems = new ArrayList<>();
        mFragmentTabItems.add(new FragmentTabItem(context.getString(R.string.tab_title_list), TodoListFragment.class));
        mFragmentTabItems.add(new FragmentTabItem(context.getString(R.string.tab_title_map), ToDoListMapFragment.class));

    }

    @Override
    public int getCount() {
        return mFragmentTabItems.size();
    }

    @Override
    public Fragment getItem(int position) {

        FragmentTabItem fragmentTab = mFragmentTabItems.get(position);
        if (fragmentTab != null){
            Class<? extends Fragment> fragmentClass = fragmentTab.getFragmentClass();

            Fragment fragment = null;
            try {

                fragment = fragmentClass.newInstance();
                Bundle args = new Bundle();
                fragment.setArguments(args);

            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }

            return fragment;


        }

        return null;

    }

    @Override
    public CharSequence getPageTitle(int position) {

        FragmentTabItem fragmentTab = mFragmentTabItems.get(position);
        return fragmentTab.getTitle();

    }
}
