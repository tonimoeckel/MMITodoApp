package com.quemb.mmitodoapp.adapter;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.controller.TodoFormFragment;
import com.quemb.mmitodoapp.model.FragmentTabItem;
import com.quemb.mmitodoapp.model.ToDo;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by tonimoeckel on 28.06.16.
 */
public class ToFragmentsPagingAdapter extends FragmentPagerAdapter {

    public static final String INTENT_EXTRA_TODO_ID = "INTENT_EXTRA_TODO_ID";

    private long mToDoId;
    private ArrayList<FragmentTabItem> mFragmentTabItems;


    public ToFragmentsPagingAdapter(FragmentManager fragmentManager, Context context, long toDoID) {
        super(fragmentManager);

        mToDoId = toDoID;

        mFragmentTabItems = new ArrayList<>();
        mFragmentTabItems.add(new FragmentTabItem(context.getString(R.string.title_details), TodoFormFragment.class));
        mFragmentTabItems.add(new FragmentTabItem(context.getString(R.string.title_contacts), TodoFormFragment.class));
        mFragmentTabItems.add(new FragmentTabItem(context.getString(R.string.title_location), TodoFormFragment.class));

    }

    @Override
    public int getCount() {
        return mFragmentTabItems.size();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public Fragment getItem(int position) {

        FragmentTabItem fragmentTab = mFragmentTabItems.get(position);
        if (fragmentTab != null){
            Class<? extends Fragment> fragmentClass = fragmentTab.getFragmentClass();

            Fragment fragment = null;
            try {

                fragment = fragmentClass.newInstance();
                Bundle args = new Bundle();
                args.putLong(INTENT_EXTRA_TODO_ID, mToDoId);
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
