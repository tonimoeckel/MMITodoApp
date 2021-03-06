package com.quemb.mmitodoapp.adapter;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.quemb.mmitodoapp.R;
import com.quemb.mmitodoapp.controller.ToDoMapFragment;
import com.quemb.mmitodoapp.controller.TodoContactsFragment;
import com.quemb.mmitodoapp.controller.TodoFormFragment;
import com.quemb.mmitodoapp.model.FragmentTabItem;

import java.util.ArrayList;

/**
 * Created by tonimoeckel on 28.06.16.
 */
public class ToFragmentsPagingAdapter extends FragmentPagerAdapter {

    public static final String INTENT_EXTRA_TODO_ID = "INTENT_EXTRA_TODO_ID";

    private final Context mContext;

    private long mToDoId;
    private ArrayList<FragmentTabItem> mFragmentTabItems;


    public ToFragmentsPagingAdapter(FragmentManager fragmentManager, Context context, long toDoID) {
        super(fragmentManager);

        mToDoId = toDoID;
        mContext = context;

        mFragmentTabItems = new ArrayList<>();
        mFragmentTabItems.add(new FragmentTabItem(context.getString(R.string.title_details), TodoFormFragment.class));

        addExtendedTabs(mToDoId);

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

    public void addExtendedTabs(Long todoId) {

        if (todoId > 0) {
            mToDoId = todoId;
            mFragmentTabItems
                    .add(new FragmentTabItem(getContext().getString(R.string.title_contacts),
                            TodoContactsFragment.class));
            mFragmentTabItems
                    .add(new FragmentTabItem(getContext().getString(R.string.title_location),
                            ToDoMapFragment.class));
        }

    }

    public boolean isExtendMode() {
        return mFragmentTabItems.size()>1;
    }

    public Context getContext() {
        return mContext;
    }
}
