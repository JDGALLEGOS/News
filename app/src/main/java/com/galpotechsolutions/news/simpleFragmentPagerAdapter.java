package com.galpotechsolutions.news;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

public class simpleFragmentPagerAdapter extends FragmentPagerAdapter {
    /** Context of the app */
    private Context mContext;

    /**
     * Create a new {@link simpleFragmentPagerAdapter} object.
     *
     * @param fm is the fragment manager that will keep each fragment's state in the adapter
     *           across swipes.
     */
    public simpleFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        mContext = context;
    }

    /**
     * Return the {@link Fragment} that should be displayed for the given page number.
     */
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new NewFragment();
        } else if(position == 1){
            return new ScienceFragment();
        } else if (position == 2){
            return new MusicFragment();
        } else if (position == 3){
            return new BookFragment();
        } else {
            return new TechFragment();
        }
    }

    /**
     * Return the total number of pages.
     */
    @Override
    public int getCount() {
        return 5;
    }

    /**
     *  Return the title of the pages
     */
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (position == 0) {
            return mContext.getString(R.string.category_new);
        } else if (position == 1){
            return  mContext.getString(R.string.category_science);
        } else if (position == 2){
            return mContext.getString(R.string.category_music);
        } else if (position == 3){
            return mContext.getString(R.string.category_books);
        } else {
            return mContext.getString(R.string.category_tech);
        }
    }

}
