package com.example.dibapp.ui.main;

import android.content.Context;
import android.content.res.Resources;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.dibapp.CreateCourseFragment;
import com.example.dibapp.EditFragment;
import com.example.dibapp.R;

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
public class SectionsPagerAdapter extends FragmentPagerAdapter {

    @StringRes
    private static final int[] TAB_TITLES = new int[]{R.string.tab_text_1, R.string.tab_text_2};
    private final Context mContext;

    public SectionsPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
        switch(position) {
            case 0:
                return new EditFragment();
            case 1:
                return new CreateCourseFragment();
            case 2:
                return new EditFragment();


        }
        return PlaceholderFragment.newInstance(position + 1);
    }

    @Nullable
    @Override
    public String getPageTitle(int position) {
        switch(position) {
            case 0:
                return mContext.getString(R.string.MyCourses);
            case 1:
                return mContext.getString(R.string.AllCourses);
            case 2:
                return mContext.getString(R.string.EditProfile);


        }
        return mContext.getResources().getString(TAB_TITLES[position]);
    }

    @Override
    public int getCount() {
        // Show 2 total pages.
        return 3;
    }
}