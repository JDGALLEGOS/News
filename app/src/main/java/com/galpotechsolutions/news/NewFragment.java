package com.galpotechsolutions.news;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class NewFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<New>> {

    /** Adapter for the list of news */
    private NewAdapter newAdapter;
    /** URL for new data from the guardian dataset
     * https://content.guardianapis.com/search?section=technology
     * &format=json
     * &order-by=newest
     * &page-size=5
     * &show-fields=all
     * &production-office=uk
     * &api-key=test
     */
    private static final String GUARDIAN_REQUEST_URL =
            "https://content.guardianapis.com/search";

    /**
     * Constant value for the new loader ID. We can choose any integer.
     * This really only comes into play if you're using multiple loaders.
     */
    private static final int NEW_LOADER_ID = 1;

    /** TextView that is displayed when the list is empty */
    private TextView emptyStateTextView;

    /** loadingIndicator that is displayed when connection is slow*/
    private View loadingIndicator;

    public NewFragment() {
        // Required empty public constructor
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.news_activity, container, false);

        // Create a new adapter that takes an empty list of earthquakes as input
        newAdapter = new NewAdapter(getActivity(), new ArrayList<New>());

        final NewAdapter itemAdapter = newAdapter;

        final ListView listView = rootView.findViewById(R.id.list);

        emptyStateTextView = rootView.findViewById(R.id.empty_view);
        listView.setEmptyView(emptyStateTextView);

        loadingIndicator = rootView.findViewById(R.id.loading_spinner);

        listView.setAdapter(itemAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Find the current new that was clicked on
                New currentNew = itemAdapter.getItem(i);

                // Convert the String URL into a URI object(to pass into the Intent constructor)
                Uri newUrl = Uri.parse(currentNew.getUrl());

                // Create a new Intent to view the new URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newUrl);

                // Send the intent to launch a new activity
                startActivity(websiteIntent);
            }
        });

        // Get a reference to the ConnectivityManager to check state of newtwork connectivity
        ConnectivityManager connMgr = (ConnectivityManager) getContext().getSystemService(getContext().CONNECTIVITY_SERVICE) ;

        // Get details on the currently active default data network
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        // If there is a network connection, fetch data
        if (networkInfo != null && networkInfo.isConnected()){
            // Get a reference to the LoaderManager, in order to interact with loaders.
            LoaderManager loaderManager = getLoaderManager();

            // Initialize the loader. Pass in the int ID constant defined above and pass in null for
            // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
            //
            loaderManager.initLoader(NEW_LOADER_ID, null, this);
        } else {
            // Otherwise, display error
            // First, hide loading indicator so error message will be visible
            loadingIndicator.setVisibility(View.GONE);

            // Update empty state with no connection error message
            emptyStateTextView.setText(R.string.no_internet_connection);
        }

        return rootView;
    }

    @NonNull
    @Override
    public android.support.v4.content.Loader<List<New>> onCreateLoader(int id, @Nullable Bundle args) {

        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String location = sharedPreferences.getString(
          getString(R.string.settings_location_key),
          getString(R.string.settings_location_default)
        );

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String limit = sharedPreferences.getString(
                getString(R.string.settings_limit_key),
                getString(R.string.settings_limit_default)
        );

        // getString retrieves a String value from the preferences. The second parameter is the default value for this preference.
        String orderBy  = sharedPreferences.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        // Parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        /** URL for new data from the guardian dataset
         * https://content.guardianapis.com/search?section=news
         * &format=json
         * &order-by=newest
         * &page-size=5
         * &show-fields=all
         * &production-office=uk
         * &api-key=test
         */
        uriBuilder.appendQueryParameter("section", "news");
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("ordery-by", orderBy);
        uriBuilder.appendQueryParameter("page-size", limit);
        uriBuilder.appendQueryParameter("show-fields", "all");
        uriBuilder.appendQueryParameter("production-office", location);
        uriBuilder.appendQueryParameter("api-key", "test");

        Log.v("uriBuilder", uriBuilder.toString());
        String urlText = uriBuilder.toString();

        return new NewLoader(getActivity(), urlText);
    }

    @Override
    public void onLoadFinished(@NonNull Loader<List<New>> loader, List<New> data) {

        //Hide loading indicator because the data has been loaded
        loadingIndicator.setVisibility(View.GONE);

        // Set empty state text to display "No earthquakes found.
        emptyStateTextView.setText(R.string.no_news);

        // Clear the adapter of previous new data
        newAdapter.clear();

        // If there is a valid list of {@list New}s, then add them to the adapter's
        // data set.This will trigger the ListView to update
        if (data != null && !data.isEmpty()){
            newAdapter.addAll(data);
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<List<New>> loader) {
        //Loader reset, so we can clear out our existing data.
        newAdapter.clear();
    }
}
