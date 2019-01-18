package com.galpotechsolutions.news;

import android.support.v4.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewLoader extends AsyncTaskLoader<List<New>> {

    /** Tag for log messages */
    private static final String LOG_TAG = NewLoader.class.getName();

    /** Query URL */
    private String mUrl;

    /**
     * Constructs a new {@link NewLoader}.
     *
     * @param context of the activity
     * @param url to load data from
     */
    public NewLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    /**
     *  This is on a background thread
     */
    @Override
    public List<New> loadInBackground() {
        if (mUrl == null){
            return null;
        }

        // Perform a network request, parse the response, and extract a list of news.
        List<New> news = QueryUtils.fetchNewData(mUrl);
        return news;
    }
}
