package com.quemb.mmitodoapp.model;


import android.support.v4.app.Fragment;

/**
 * Created by tonimoeckel on 28.06.16.
 */
public class FragmentTabItem {

    private String mTitle;
    private Class<? extends Fragment> mFragmentClass;

    public  FragmentTabItem(String title, Class<? extends Fragment> fragmentClass){
        mTitle = title;
        mFragmentClass = fragmentClass;
    }

    public String getTitle() {
        return mTitle;
    }

    public Class<? extends Fragment> getFragmentClass() {
        return mFragmentClass;
    }
}